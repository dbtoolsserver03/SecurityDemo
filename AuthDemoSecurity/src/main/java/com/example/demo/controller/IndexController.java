package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class IndexController {


	@RequestMapping("/index")
	public String hanle01() {
		log.info("/index 请求进来了....");
		return "Index HTML";
	}
}
