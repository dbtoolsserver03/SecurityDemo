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

import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.baizhi.security.filter.LoginKaptchaFilter;
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
    DataSource dataSource;
    
	@Autowired
	MyUserDetailService myUserDetailService;
	
	@Autowired
	AuthenticationConfiguration authenticationConfiguration;

	public AuthenticationManager authenticationManager() throws Exception {
	    AuthenticationManager authenticationManager = 
	        authenticationConfiguration.getAuthenticationManager();
	    return authenticationManager;
	}

    @Bean
    public LoginKaptchaFilter kaptchaFilter() throws Exception {
    	LoginKaptchaFilter loginKaptchaFilter = new LoginKaptchaFilter();
    	
        return loginKaptchaFilter;
    }

    
    @Bean
    public RememberMeServices rememberMeServices() {
        return new PersistentTokenBasedRememberMeServices(UUID.randomUUID().toString(), myUserDetailService, persistentTokenRepository());
    }
    
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
		.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.requestMatchers("/login.html").permitAll()
				.requestMatchers("/vc.jpg").permitAll()
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
			
			
			.sessionManagement((sessionManagement) -> sessionManagement.maximumSessions(1).expiredUrl("/login?expired"))

//			.sessionManagement((sessions) -> sessions
//					.sessionConcurrency((concurrency) -> concurrency
//							.maximumSessions(1)
//							.expiredUrl("/login?expired")
//					))
					
					
//			.sessionManagement((session)->session.maximumSessions(1) 
//					.expiredUrl("/login")
//					//.sessionRegistry(sessionRegistry()) //将 session 交给谁管理
//					//.maxSessionsPreventsLogin(true)// 一旦登录 禁止再次登录
//					)
			.csrf(AbstractHttpConfigurer::disable);//csrf 关闭
		
		
        // at: 用来某个 filter 替换过滤器链中哪个 filter
        // before: 放在过滤器链中哪个 filter 之前
        // after: 放在过滤器链中那个 filter 之后
        http.addFilterBefore(kaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
   
        // 开启记住我
		http.rememberMe((rememberMe) -> rememberMe
				.tokenRepository(persistentTokenRepository())
				.rememberMeServices(rememberMeServices())//　
				//.alwaysRemember(true)
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

    
	// @formatter:off
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails user = User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("password")
//				.roles("USER")
//				.build();
//		return new InMemoryUserDetailsManager(user);
//	}
	// @formatter:on
	
	

}
