package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthDemoSecurityApplication {

	public static void main(String[] args) {

		System.setProperty("spring.security.strategy","MODE_INHERITABLETHREADLOCAL");
		SpringApplication.run(AuthDemoSecurityApplication.class, args);
	}

}
