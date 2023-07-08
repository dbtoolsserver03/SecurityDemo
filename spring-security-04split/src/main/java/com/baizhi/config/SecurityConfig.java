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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

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
    private MyUserDetailService myUserDetailsService;
    @Bean
    DaoAuthenticationProvider  authenticationProvider() {
    	DaoAuthenticationProvider  authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(myUserDetailsService);
        return authenticationProvider;
    }

    //自定义 filter 交给工厂管理
    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setFilterProcessesUrl("/doLogin");//指定认证 url
        loginFilter.setUsernameParameter("uname");//指定接收json 用户名 key
        loginFilter.setPasswordParameter("passwd");//指定接收 json 密码 key
        
        
//		CasAuthenticationFilter filter = new CasAuthenticationFilter();
//		CasAuthenticationProvider casAuthenticationProvider = casAuthenticationProvider(userDetailsService);
//		filter.setAuthenticationManager(new ProviderManager(casAuthenticationProvider));
        
        
//		loginFilter.setAuthenticationManager(authenticationManagerBean());
        
        loginFilter.setAuthenticationManager(new ProviderManager(authenticationProvider()));
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
        return loginFilter;
    }
    
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.authorizeHttpRequests((authorize) -> authorize
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
					.requestMatchers("/login.html").permitAll()
					.anyRequest().authenticated())
			.exceptionHandling((exception) -> exception
					.authenticationEntryPoint((req, resp, ex) -> {
	                    resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
	                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
	                    resp.getWriter().println("请认证之后再去处理!");
	                }))
//			.formLogin(null)
//			.formLogin((form) -> form
//					.loginPage("/login.html")
//					.loginProcessingUrl("/doLogin")
//					.usernameParameter("uname")
//					.passwordParameter("passwd")
//					//						.successForwardUrl("/home")   //不会跳转到之前请求路径
//					.defaultSuccessUrl("/index.html", true)//如果之前有请求路径，会优先跳转之前请求路径，可以传入第二个参数进行修改。
//					.failureUrl("/login.html")//重定向到登录页面 失败之后redirect跳转
//					.permitAll())
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
	
			.csrf((csrf) -> csrf.disable())//csrf 关闭
			//				.httpBasic(withDefaults())
			.formLogin(withDefaults());

		// @formatter:on
		return http.build();
	}

}
