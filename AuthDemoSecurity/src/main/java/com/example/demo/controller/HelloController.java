package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HelloController {


	@RequestMapping("/login.html")
	public String hanle01() {
		log.info("/login 请求进来了....");
		return "login";
	}
}
