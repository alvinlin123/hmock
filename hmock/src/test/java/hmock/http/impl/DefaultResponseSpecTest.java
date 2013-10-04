/*
 * #%L
 * HMock
 * %%
 * Copyright (C) 2013 Alvin Lin
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package hmock.http.impl;

import static org.easymock.EasyMock.*;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import hmock.http.CommonHttpHeaders;
import hmock.http.ResponseDetail;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class DefaultResponseSpecTest {

	@Test
	public void testReponseWithoutCondition() throws Exception {
		
		DefaultResponseSpec responseSpec = new DefaultResponseSpec(null);
		
		responseSpec
			.body("hello world")
			.status(400)
			.header("header1", "value1")
			.header("header2", "value2");
		
		ResponseDetail response = responseSpec.generateResponse(null);
		
		assertEquals(400, response.status());
		assertEquals("hello world", IOUtils.toString(response.body()));
		assertEquals("value1", response.headers().get("header1"));
		assertEquals("value2", response.headers().get("header2"));
		assertEquals("text/plain;charset=us-ascii", 
				 response.headers().get(CommonHttpHeaders.CONTENT_TYPE.toHttpString()));
	}
	
	@Test
	public void testReponseWithOneConditionExactMatch() throws Exception {
		
		DefaultResponseSpec responseSpec = new DefaultResponseSpec(null);
		
		responseSpec
			.on()
				.param("param1", "value1")
			.reply()
				.body("hello world")
				.status(200)
				.header("header1", "value1")
				.header("header2", "value2");
		
		HttpServletRequest request = createNiceMock(HttpServletRequest.class);
		expect(request.getParameter("param1")).andReturn("value1").once();
		replay(request);
		
		ResponseDetail response = responseSpec.generateResponse(request);
		
		assertEquals(200, response.status());
		assertEquals("hello world", IOUtils.toString(response.body()));
		assertEquals("value1", response.headers().get("header1"));
		assertEquals("value2", response.headers().get("header2"));
		assertEquals("text/plain;charset=us-ascii", 
					 response.headers().get(CommonHttpHeaders.CONTENT_TYPE.toHttpString()));
	}
}
