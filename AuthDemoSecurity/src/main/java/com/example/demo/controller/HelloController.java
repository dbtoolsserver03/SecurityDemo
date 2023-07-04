package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HelloController {


	@RequestMapping("/login.html")
	public String hanle01() {
		log.info("/login 请求进来了....");


		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		log.info("身份信息：" +authentication.getPrincipal());
		log.info("权限信息：" +authentication.getAuthorities());
		return "login";
	}
}
