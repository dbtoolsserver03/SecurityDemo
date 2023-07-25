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


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import com.baizhi.security.filter.LoginKaptchaFilter;
import com.baizhi.service.MyUserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An example of explicitly configuring Spring Security with the defaults.
 *
 * @author Rob Winch
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
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
	
    //自定义 filter 交给工厂管理
    @Bean
    public LoginKaptchaFilter loginKaptchaFilter() throws Exception {
    	LoginKaptchaFilter loginFilter = new LoginKaptchaFilter();
        loginFilter.setFilterProcessesUrl("/doLogin");//指定认证 url
        loginFilter.setUsernameParameter("uname");//指定接收json 用户名 key
        loginFilter.setPasswordParameter("passwd");//指定接收 json 密码 key
        loginFilter.setRememberMeServices(rememberMeServices()); //设置认证成功时使用自定义rememberMeService
        loginFilter.setAuthenticationManager(authenticationManager());
        //认证成功处理
        loginFilter.setAuthenticationSuccessHandler((req, resp, authentication) -> {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("msg", "登录成功");
            result.put("用户信息", authentication.getPrincipal());
            resp.setContentType("application/json;charset=UTF-8");
            resp.setStatus(HttpStatus.OK.value());
            String s = new ObjectMapper().writeValueAsString(result);
            resp.getWriter().println(s);
        });
        //认证失败处理
        loginFilter.setAuthenticationFailureHandler((req, resp, ex) -> {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("msg", "登录失败: " + ex.getMessage());
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setContentType("application/json;charset=UTF-8");
            String s = new ObjectMapper().writeValueAsString(result);
            resp.getWriter().println(s);
        });
        
        loginFilter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        return loginFilter;
    }
    
    @Bean
    public RememberMeServices rememberMeServices() {
        return new MyPersistentTokenBasedRememberMeServices(UUID.randomUUID().toString(), myUserDetailService, persistentTokenRepository());
    }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.authorizeHttpRequests((authorize) -> authorize
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
					.requestMatchers("/vc.jpg").permitAll()
					.anyRequest().authenticated())
			.exceptionHandling((exception) -> exception
					.authenticationEntryPoint((req, resp, ex) -> {
	                    resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
	                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
	                    resp.getWriter().println("请认证之后再去处理!");
	                }))
			.logout((form) -> form.logoutRequestMatcher(new OrRequestMatcher(
                    new AntPathRequestMatcher("/logout", HttpMethod.DELETE.name()),
                    new AntPathRequestMatcher("/logout", HttpMethod.GET.name())
            ))
			.logoutSuccessHandler((req, resp, auth) -> {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("msg", "注销成功");
                result.put("用户信息", auth.getPrincipal());
                resp.setContentType("application/json;charset=UTF-8");
                resp.setStatus(HttpStatus.OK.value());
                String s = new ObjectMapper().writeValueAsString(result);
                resp.getWriter().println(s);
            }))
	
			.csrf((csrf) -> csrf.disable());//csrf 关闭
		
        // at: 用来某个 filter 替换过滤器链中哪个 filter
        // before: 放在过滤器链中哪个 filter 之前
        // after: 放在过滤器链中那个 filter 之后
        http.addFilterAt(loginKaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
   
        
        // 开启记住我
		http.rememberMe((rememberMe) -> rememberMe
				.tokenRepository(persistentTokenRepository())
//				.rememberMeServices(rememberMeServices())
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

}