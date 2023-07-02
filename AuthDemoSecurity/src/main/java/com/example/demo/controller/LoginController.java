package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LoginController {


	@RequestMapping("/hello")
	public String hanle01() {
		log.info("/hello 请求进来了....");
		return "Hello,Spring Boot 2!" + "你好";
	}
}
