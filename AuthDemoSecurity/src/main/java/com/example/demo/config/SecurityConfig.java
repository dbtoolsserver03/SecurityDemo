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

package com.example.demo.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

/**
 * An example of explicitly configuring Spring Security with the defaults.
 *
 * @author Rob Winch
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/login.html").permitAll()
						.requestMatchers("/index").permitAll()
						.anyRequest().authenticated()
				)
				.formLogin((form) -> form
						.loginPage("/login.html")
						.loginProcessingUrl("/doLogin")
						.usernameParameter("uname")
						.passwordParameter("pwd")
//						.successForwardUrl("/home")   //不会跳转到之前请求路径
						.defaultSuccessUrl("/home",true)//如果之前有请求路径，会优先跳转之前请求路径，可以传入第二个参数进行修改。
						.successHandler(new MyAuthenticationSuccessHandler())
//						.failureForwardUrl("/login.html")
//						.failureUrl("/login.html")// 默认谁失败之后redirect跳转
						.failureHandler(new MyAuthenticationFailureHandler())

						.permitAll()
				)
				.logout((form)->form.logoutRequestMatcher(new OrRequestMatcher(
						new OrRequestMatcher(new AntPathRequestMatcher("/aa","GET")),
						new OrRequestMatcher(new AntPathRequestMatcher("/bb","POST")),
						new OrRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
						)).logoutSuccessUrl("/login.html")
				)

				.csrf((csrf) -> csrf.disable())
//				.httpBasic(withDefaults())
				.formLogin(withDefaults());
//		http.exceptionHandling((exceptions) -> exceptions
//                .authenticationEntryPoint(
//                        new LoginUrlAuthenticationEntryPoint("/login"))
//        );
		// @formatter:on
		return http.build();
	}

	// @formatter:off
//	@Bean
//	public InMemoryUserDetailsManager userDetailsService() {
//		UserDetails user = User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("password")
//				.roles("USER")
//				.build();
//		return new InMemoryUserDetailsManager(user);
//	}
	// @formatter:on

}
