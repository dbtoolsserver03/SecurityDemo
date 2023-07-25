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

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * An example of explicitly configuring Spring Security with the defaults.
 *
 * @author Rob Winch
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigure {
	
	
	@Autowired
    DataSource dataSource;
    
	@Autowired
	AuthenticationConfiguration authenticationConfiguration;

	public AuthenticationManager authenticationManager() throws Exception {
	    AuthenticationManager authenticationManager = 
	        authenticationConfiguration.getAuthenticationManager();
	    return authenticationManager;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
		.authorizeHttpRequests((authorize) -> authorize
					.anyRequest().authenticated())

		
		.formLogin((form) -> form
				.permitAll())
//		.rememberMe((form)-> form.tokenRepository(null))
//			.logout((form) -> form.logoutUrl("/logout")
//					.logoutSuccessUrl("/login.html"))
	
			.csrf(AbstractHttpConfigurer::disable);//csrf 关闭
		
        // 开启记住我
		http.rememberMe((rememberMe) -> rememberMe
				.tokenRepository(persistentTokenRepository())
				);
        
		// @formatter:on
		return http.build();
	}
    //指定数据库持久化
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);//启动创建表结构
        return jdbcTokenRepository;
    }
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder()
				.username("admin")
				.password("1234567")
				.roles("USER")
				.build();
		return new InMemoryUserDetailsManager(user);
	}
	
}
