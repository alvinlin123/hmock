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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HMockTest {

	private static final HttpClient _httpclient = HttpClients.createDefault();
	
	@Test
	public void testParamHeadRequest() throws Exception {
		
		HMock
		.respond()
			.status(201)
		.when()
			.head("/employees")
			.param("name", equalTo("John"));
	
		HttpHead head = new HttpHead("http://localhost:7357/employees?name=John");
		
		HttpResponse response = _httpclient.execute(head);
		
		assertEquals(201, response.getStatusLine().getStatusCode());
		
		head = new HttpHead("http://localhost:7357/employees?name=John2");
		response = _httpclient.execute(head);
		
		assertEquals(404, response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testParamDeleteRequest() throws Exception {
		
		HMock
		.respond()
			.body("A,B,C")
		.when()
			.delete("/employees")
			.param("name", equalTo("John"));
	
		HttpDelete delete = new HttpDelete("http://localhost:7357/employees?name=John");
		
		HttpResponse response = _httpclient.execute(delete);
		
		String responsebody = IOUtils.toString(response.getEntity().getContent());
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("A,B,C", responsebody);
		
		delete = new HttpDelete("http://localhost:7357/employees?name=John2");
		response = _httpclient.execute(delete);
		
		assertEquals(404, response.getStatusLine().getStatusCode());
	}
	
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
	
	@Test
	public void testUrlEncodedFormPostRequest() throws Exception {
		
		HMock
		.respond()
			.body("A,B,C")
		.when()
			.post("/employees")
			.param("name", equalTo("John"));
	
		HttpPost post = new HttpPost("http://localhost:7357/employees");
		NameValuePair[] parameters = {new BasicNameValuePair("name", "John")};
		post.setEntity(new UrlEncodedFormEntity(Arrays.asList(parameters)));
		
		HttpResponse response = _httpclient.execute(post);
		
		String responsebody = IOUtils.toString(response.getEntity().getContent());
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("A,B,C", responsebody);
		
		post = new HttpPost("http://localhost:7357/employees");
		parameters = new NameValuePair[]{new BasicNameValuePair("name", "John2")};
		post.setEntity(new UrlEncodedFormEntity(Arrays.asList(parameters)));
		response = _httpclient.execute(post);
		EntityUtils.consume(response.getEntity());
		assertEquals(404, response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testUrlEncodedFormPutRequest() throws Exception {
		
		HMock
		.respond()
			.body("A,B,C")
		.when()
			.put("/employees")
			.param("name", equalTo("John"));
	
		HttpPut put = new HttpPut("http://localhost:7357/employees");
		NameValuePair[] parameters = {new BasicNameValuePair("name", "John")};
		put.setEntity(new UrlEncodedFormEntity(Arrays.asList(parameters)));
		
		HttpResponse response = _httpclient.execute(put);
		
		String responsebody = IOUtils.toString(response.getEntity().getContent());
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("A,B,C", responsebody);
		
		put = new HttpPut("http://localhost:7357/employees");
		parameters = new NameValuePair[]{new BasicNameValuePair("name", "John2")};
		put.setEntity(new UrlEncodedFormEntity(Arrays.asList(parameters)));
		response = _httpclient.execute(put);
		EntityUtils.consume(response.getEntity());
		assertEquals(404, response.getStatusLine().getStatusCode());
	}
}
