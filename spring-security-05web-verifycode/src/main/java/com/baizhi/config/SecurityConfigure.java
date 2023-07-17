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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.baizhi.security.filter.KaptchaFilter;

/**
 * An example of explicitly configuring Spring Security with the defaults.
 *
 * @author Rob Winch
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigure {
	
	@Autowired
	AuthenticationConfiguration authenticationConfiguration;

	public AuthenticationManager authenticationManager() throws Exception {
	    AuthenticationManager authenticationManager = 
	        authenticationConfiguration.getAuthenticationManager();
	    return authenticationManager;
	}
	
    @Bean
    public KaptchaFilter kaptchaFilter() throws Exception {
        KaptchaFilter kaptchaFilter = new KaptchaFilter();
        kaptchaFilter.setFilterProcessesUrl("/doLogin");
        kaptchaFilter.setUsernameParameter("uname");
        kaptchaFilter.setPasswordParameter("passwd");
        kaptchaFilter.setKaptchaParameter("kaptcha");
        
        //指定认证管理器
        kaptchaFilter.setAuthenticationManager(authenticationManager());

        //指定认证成功处理
        kaptchaFilter.setAuthenticationSuccessHandler((req, resp, auth) -> {
            resp.sendRedirect("/index.html");
            
        });
        //指定认证失败处理
        kaptchaFilter.setAuthenticationFailureHandler((req, resp, ex) -> {
            resp.sendRedirect("/login.html");
        });
        return kaptchaFilter;
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
					)
		
//		.formLogin((form) -> form
//				.loginPage("/login.html")
//				.loginProcessingUrl("/doLogin")
//				.usernameParameter("uname")
//				.passwordParameter("passwd")
//				//						.successForwardUrl("/home")   //不会跳转到之前请求路径
//				.defaultSuccessUrl("/index.html", true)//如果之前有请求路径，会优先跳转之前请求路径，可以传入第二个参数进行修改。
//				.failureUrl("/login.html")//重定向到登录页面 失败之后redirect跳转
//				.permitAll())
//		
		
			.logout((form) -> form.logoutUrl("/logout")
					.logoutSuccessUrl("/login.html"))
	
			.csrf(AbstractHttpConfigurer::disable);//csrf 关闭
			//				.httpBasic(withDefaults())
//			.formLogin(withDefaults());
		
        // at: 用来某个 filter 替换过滤器链中哪个 filter
        // before: 放在过滤器链中哪个 filter 之前
        // after: 放在过滤器链中那个 filter 之后
        http.addFilterAt(kaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
   
		// @formatter:on
		return http.build();
	}
	

	 
	
//	
//    @Autowired
//    private MyUserDetailService myUserDetailsService;
//    @Bean
//    AuthenticationManager  authenticationManager() {
//    	DaoAuthenticationProvider  authenticationProvider = new DaoAuthenticationProvider ();
//        authenticationProvider.setUserDetailsService(myUserDetailsService);
//        ProviderManager pm = new ProviderManager(authenticationProvider);
//        return pm;
//    }
	
	
    /**
     * 调用loadUserByUsername获得UserDetail信息，在AbstractUserDetailsAuthenticationProvider里执行用户状态检查
     *
     * @return
     */
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        // DaoAuthenticationProvider 从自定义的 userDetailsService.loadUserByUsername 方法获取UserDetails
//        authProvider.setUserDetailsService(myUserDetailsService);
//        // 设置密码编辑器
//        //authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
 
//    /**
//     * 登录时需要调用AuthenticationManager.authenticate执行一次校验
//     *
//     * @param config
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }

}
