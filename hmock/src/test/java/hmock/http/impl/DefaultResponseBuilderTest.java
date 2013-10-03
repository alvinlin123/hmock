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
package hmock.http.impl;

import static org.junit.Assert.assertEquals;
import hmock.http.ResponseBodyProvider;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class DefaultResponseBuilderTest {

	@Test
	public void testCanUseStringAsResponse() throws Exception {
		
		DefaultResponseBuilder builder = new DefaultResponseBuilder();
		
		builder.body("hello");
		
		assertEquals("hello", IOUtils.toString(builder.body()));
	}
	
	@Test
	public void testCanUseFileAsResponse() throws Exception {
		
		DefaultResponseBuilder builder = new DefaultResponseBuilder();
		
		builder.body(new File("src/test/resources/sample-response"));
		
		assertEquals("sample-file-response", IOUtils.toString(builder.body()));
	}
	
	@Test
	public void testCanUseResponseBodyProviderAsResponse() throws Exception {
		
		DefaultResponseBuilder builder = new DefaultResponseBuilder();
		builder.body(new ResponseBodyProvider() {
			
			@Override
			public String getContentType() {
				return "text/json";
			}

			@Override
			public String getCharset() {

				return "utf-8";
			}

			@Override
			public InputStream getResponseBody() {
			
				return new ByteArrayInputStream("{prop=\"response-from-provider\"}".getBytes());
				
			}
		});
		
		Map<String, String> headers = builder.headers();
		
		assertEquals("{prop=\"response-from-provider\"}", IOUtils.toString(builder.body()));
		assertEquals("text/json; charset=utf-8", headers.get("Content-Type"));
	}
	
	@Test
	public void testCanUseEmptyContentTypeResponseBodyProviderAsResponse() throws Exception {
		
		DefaultResponseBuilder builder = new DefaultResponseBuilder();
		builder.body(new ResponseBodyProvider() {
			
			@Override
			public String getContentType() {
				return null;
			}

			@Override
			public String getCharset() {

				return "something";
			}

			@Override
			public InputStream getResponseBody() {
			
				return new ByteArrayInputStream("{prop=\"response-from-provider\"}".getBytes());
				
			}
		});
		
		Map<String, String> headers = builder.headers();
		
		assertEquals("{prop=\"response-from-provider\"}", IOUtils.toString(builder.body()));
		assertEquals(null, headers.get("Content-Type"));
	}
	
	@Test
	public void testCanUseInputStreamAsResponse() throws Exception {
		
		DefaultResponseBuilder builder = new DefaultResponseBuilder();
		
		builder.body(new ByteArrayInputStream("response-from-stream".getBytes()));
		
		assertEquals("response-from-stream", IOUtils.toString(builder.body()));
	}
	
	@Test
	public void testCanAddHader() {
		
		DefaultResponseBuilder builder = new DefaultResponseBuilder();
		
		builder.header("Custom-Header", "Custom-Value");
		
		Map<String, String> headers = builder.headers();
		
		assertEquals("Custom-Value", headers.get("Custom-Header"));
	}
	
	@Test
	public void testSetContentType() {
		
		DefaultResponseBuilder builder = new DefaultResponseBuilder();
		
		builder.contentType("application/xml");
		
		Map<String, String> headers = builder.headers();
		
		assertEquals("application/xml", headers.get("Content-Type"));
	}
	
	@Test
	public void testSetContentTypeWithCharSet() {
		
		DefaultResponseBuilder builder = new DefaultResponseBuilder();
		
		builder.contentType("application/xml", "utf-8");
		
		Map<String, String> headers = builder.headers();
		
		assertEquals("application/xml; charset=utf-8", headers.get("Content-Type"));
	}
	
	@Test
	public void testSetContentTypeWithNullCharSet() {
		
		DefaultResponseBuilder builder = new DefaultResponseBuilder();
		
		builder.contentType("application/xml", null);
		
		Map<String, String> headers = builder.headers();
		
		assertEquals("application/xml", headers.get("Content-Type"));
	}
	
	@Test
	public void testSetContentTypeWithBlankCharSet() {
		
		DefaultResponseBuilder builder = new DefaultResponseBuilder();
		
		builder.contentType("application/xml", "  ");
		
		Map<String, String> headers = builder.headers();
		
		assertEquals("application/xml", headers.get("Content-Type"));
	}
}
