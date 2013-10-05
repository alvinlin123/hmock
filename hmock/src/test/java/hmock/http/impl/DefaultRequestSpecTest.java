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

import static org.hamcrest.Matchers.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

public class DefaultRequestSpecTest {
	
	@Test
	public void testCanHandleParameterlessGetRequest() {
		
		HttpServletRequest mockrequest = createMock(HttpServletRequest.class);
		expect(mockrequest.getRequestURI()).andReturn("/service1").anyTimes();
		expect(mockrequest.getMethod()).andReturn("GET").anyTimes();
		replay(mockrequest);
		
		DefaultRequestSpec requestSpec = new DefaultRequestSpec();
		requestSpec.get("/service1");
		assertEquals(true, requestSpec.canHandle(mockrequest));
		
		requestSpec = new DefaultRequestSpec();
		requestSpec.get("/service2");
		assertEquals(false, requestSpec.canHandle(mockrequest));
	}
	
	@Test
	public void testCanHandleParameteredGetRequest() {
		
		HttpServletRequest mockrequest = createMock(HttpServletRequest.class);
		expect(mockrequest.getRequestURI()).andReturn("/service1").anyTimes();
		expect(mockrequest.getMethod()).andReturn("GET").anyTimes();
		expect(mockrequest.getParameter("param1")).andReturn("value1").anyTimes();
		expect(mockrequest.getParameter("param2")).andReturn("value2").anyTimes();
		replay(mockrequest);
		
		DefaultRequestSpec requestSpec = new DefaultRequestSpec();
		requestSpec
			.get("/service1")
			.param("param1", equalTo("value1"))
			.param("param2", equalTo("value2"));
			
		assertEquals(true, requestSpec.canHandle(mockrequest));
		
		requestSpec = new DefaultRequestSpec();
		requestSpec
			.get("/service1")
			.param("param1", equalTo("value1"))
			.param("param2", equalTo("value5"));
		assertEquals(false, requestSpec.canHandle(mockrequest));
	}
}
