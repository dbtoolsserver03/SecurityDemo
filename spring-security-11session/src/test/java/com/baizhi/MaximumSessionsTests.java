/*
 * Copyright 2023 the original author or authors.
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

package com.baizhi;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MaximumSessionsTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void loginOnSecondLoginThenFirstSessionTerminated() throws Exception {
		// @formatter:off
		MvcResult mvcResult = this.mvc.perform(formLogin())
				.andExpect(authenticated())
				.andReturn();

		MockHttpSession firstLoginSession = (MockHttpSession) mvcResult.getRequest().getSession();

		this.mvc.perform(get("/").session(firstLoginSession))
				.andExpect(authenticated());

		this.mvc.perform(formLogin()).andExpect(authenticated());

		// first session is terminated by second login
		this.mvc.perform(get("/").session(firstLoginSession))
				.andExpect(unauthenticated());
		// @formatter:on
	}

}