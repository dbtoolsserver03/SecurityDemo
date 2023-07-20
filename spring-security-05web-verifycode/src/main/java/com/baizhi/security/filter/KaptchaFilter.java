package com.baizhi.security.filter;


import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.baizhi.controller.VerifyCodeController;
import com.baizhi.security.exception.KaptchaNotMatchException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//自定义验证码的 filter
public class KaptchaFilter extends OncePerRequestFilter {


    private String kaptchaParameter = VerifyCodeController.FORM_KAPTCHA_KEY;

    public String getKaptchaParameter() {
        return kaptchaParameter;
    }

    public void setKaptchaParameter(String kaptchaParameter) {
        this.kaptchaParameter = kaptchaParameter;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 必须是登录的post请求才能进行验证，其他的直接放行
        if(StringUtils.equals("/doLogin", request.getRequestURI())) {
        	
            try {
            	//1.验证谜底与用户输入是否匹配
            	validate(request);
			} catch (AuthenticationException e) {
				throw e;
				//2.捕获步骤1中校验出现异常，交给失败处理类进行进行处理
				// TODO: handle exception
			}
           
        } 
        		
     	//通过校验，就放行
        filterChain.doFilter(request,response);
	}
	
    private void validate(HttpServletRequest request) throws AuthenticationException {

        //1.从请求中获取验证码
        String verifyCode = request.getParameter(getKaptchaParameter());
        //2.与 session 中验证码进行比较
        String sessionVerifyCode = (String) request.getSession().getAttribute(VerifyCodeController.FORM_KAPTCHA_KEY);
        
        if(StringUtils.isEmpty(verifyCode)){
            throw new KaptchaNotMatchException("验证码不能为空");
        }
        
        if (!StringUtils.equals(verifyCode, sessionVerifyCode)) {
        	 throw new KaptchaNotMatchException("验证码不匹配!");
        } 
        
    }
}
