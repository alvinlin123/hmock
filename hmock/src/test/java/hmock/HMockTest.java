/*
 * #%L
 * HMock
 * %%
 * Copyright (C) 2013 nappingcoder
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
package hmock;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class HMockTest {

	@Test
	public void testParamlessGetRequest() throws Exception {
		
		HMock
		.respond()
			.body("A,B,C")
		.when()
			.get("/employees");
		
		URL request = new URL("http://localhost:7357/employees");
		HttpURLConnection conn = (HttpURLConnection) request.openConnection();
		
		String response = IOUtils.toString(conn.getInputStream());
		
		assertEquals(200, conn.getResponseCode());
		assertEquals("A,B,C", response);
	}
	
	@Test
	public void testCanSetResponseCode() throws Exception {
		
		HMock
		.respond()
			.status(400)
			.body("bad request")
		.when()
			.get("/statuscode");
		
		URL request = new URL("http://localhost:7357/statuscode");
		HttpURLConnection conn = (HttpURLConnection) request.openConnection();
		
		assertEquals(400, conn.getResponseCode());
		
		/* 
		 * need to read error after invoke a method that will initiate the
		 * http connection (.getResponseCode). Oh HttpURLConnection API...
		 *
		 */
		String responseBody = IOUtils.toString(conn.getErrorStream());
		assertEquals("bad request", responseBody);
	}
	
	@Test
	public void testParamGetRequest() throws Exception {
		
		HMock
		.respond()
			.body("A,B,C")
		.when()
			.get("/employees")
			.param("name", equalTo("John"));
		
		URL request = new URL("http://localhost:7357/employees");
		HttpURLConnection conn = (HttpURLConnection) request.openConnection();
		
		assertEquals(404, conn.getResponseCode());
		
		request = new URL("http://localhost:7357/employees?name=John");
		conn = (HttpURLConnection) request.openConnection();
		
		String response = IOUtils.toString(conn.getInputStream());
		assertEquals(200, conn.getResponseCode());
		assertEquals("A,B,C", response);
	}
	
	@Test
	public void testPathParamGetRequest() throws Exception {
		
		HMock
		.respond()
			.body("A,B,C")
		.when()
			.get("/employees/{name}")
			.pathparam("name", equalTo("John"));
		
		URL request = new URL("http://localhost:7357/employees");
		HttpURLConnection conn = (HttpURLConnection) request.openConnection();
		
		assertEquals(404, conn.getResponseCode());
		
		request = new URL("http://localhost:7357/employees/John");
		conn = (HttpURLConnection) request.openConnection();
		
		String response = IOUtils.toString(conn.getInputStream());
		assertEquals(200, conn.getResponseCode());
		assertEquals("A,B,C", response);
	}
	
	@Test
	public void testRequestWithHeader() throws Exception {
	
		HMock
			.respond()
				.body("A,B,C")
			.when()
				.get("/headertest")
				.header("X-TEST-HEADER", equalTo("test-value"));
		
		URL request = new URL("http://localhost:7357/headertest");
		HttpURLConnection conn = (HttpURLConnection) request.openConnection();
		conn.setRequestProperty("X-TEST-HEADER", "test-value");
		String response = IOUtils.toString(conn.getInputStream());
		assertEquals(200, conn.getResponseCode());
		assertEquals("A,B,C", response);
	}
	
	@Test
	public void test404IfWrongHttpMethod() throws Exception {
		
		HMock
			.respond()
				.body("A,B,C")
			.when()
				.get("/employees/nopost");
		
		URL request = new URL("http://localhost:7357/employees/nopost");
		HttpURLConnection conn = (HttpURLConnection) request.openConnection();
		conn.setRequestMethod("POST");
		assertEquals(404, conn.getResponseCode());
	}
	
}
