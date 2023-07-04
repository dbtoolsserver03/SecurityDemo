package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LogoutController {


	@RequestMapping("/logout.html")
	public String logout() {
		log.info("/logout 请求进来了....");
		return "logout";
	}
}
