package com.example.demo.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ThreadController {


	@RequestMapping("/thread")
	public String hanle01() {
		log.info("/thread 请求进来了....");


        System.out.println("Hello Spring Security!");
        new Thread(() -> {
            Authentication Authentication = SecurityContextHolder.getContext().getAuthentication();
            User childPrincipal = (User) Authentication.getPrincipal();
            System.out.println("Principal.getUsername() = " + childPrincipal.getUsername());
            System.out.println("Principal.getPassword() = " + childPrincipal.getPassword());
            System.out.println("Principal.getAuthorities() = " + childPrincipal.getAuthorities());
        }).start();
        return "hello spring security!";
    }
}
