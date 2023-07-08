package com.baizhi.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO 自動生成されたメソッド・スタブ
		// TODO 自動生成されたメソッド・スタブ
		Map<String,Object> res = new HashMap<>();
		res.put("msg", "登陆失败： "+exception.getMessage());
		res.put("status", "500");

		response.setContentType("application/json;charset=UTF-8");
		String s = new ObjectMapper().writeValueAsString(res);
		response.getWriter().println(s);
	}


}
