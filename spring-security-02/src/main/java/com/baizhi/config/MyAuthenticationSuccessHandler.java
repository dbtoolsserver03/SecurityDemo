package com.baizhi.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO 自動生成されたメソッド・スタブ
		Map<String,Object> res = new HashMap<>();
		res.put("msg", "登陆成功");
		res.put("status", "200");
		res.put("authentication", authentication);

		response.setContentType("application/json;charset=UTF-8");
		String s = new ObjectMapper().writeValueAsString(res);
		response.getWriter().println(s);
	}

}
