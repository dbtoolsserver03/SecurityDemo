/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baizhi.config;

import static org.springframework.security.config.Customizer.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.baizhi.service.MyUserDetailService;

/**
 * An example of explicitly configuring Spring Security with the defaults.
 *
 * @author Rob Winch
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigure {
	
    @Autowired
    private MyUserDetailService myUserDetailsService;
    @Bean
    DaoAuthenticationProvider  authenticationProvider() {
    	DaoAuthenticationProvider  authenticationProvider = new DaoAuthenticationProvider ();
        authenticationProvider.setUserDetailsService(myUserDetailsService);
        return authenticationProvider;
    }
    
    
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.authorizeHttpRequests((authorize) -> authorize
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
					.requestMatchers("/login.html").permitAll()
					.anyRequest().authenticated())
			.formLogin((form) -> form
					.loginPage("/login.html")
					.loginProcessingUrl("/doLogin")
					.usernameParameter("uname")
					.passwordParameter("passwd")
					//						.successForwardUrl("/home")   //不会跳转到之前请求路径
					.defaultSuccessUrl("/index.html", true)//如果之前有请求路径，会优先跳转之前请求路径，可以传入第二个参数进行修改。
					.failureUrl("/login.html")//重定向到登录页面 失败之后redirect跳转
					.permitAll())
			.logout((form) -> form.logoutUrl("/logout")
					.logoutSuccessUrl("/login.html"))
	
			.csrf((csrf) -> csrf.disable())//csrf 关闭
			//				.httpBasic(withDefaults())
			.formLogin(withDefaults());

		// @formatter:on
		return http.build();
	}

}
