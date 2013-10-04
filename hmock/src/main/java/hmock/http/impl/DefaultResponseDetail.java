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

import hmock.http.CommonHttpHeaders;
import hmock.http.ResponseBodyProvider;
import hmock.http.ResponseDetail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.concurrent.ConcurrentException;

public class DefaultResponseDetail implements ResponseDetail {

	private static final String CONTENT_TYPE_HEADER_TEMPLATE = "%s;charset=%s";
	private ResponseBodyProvider body = null;
	private Map<String, String> headers = new HashMap<String, String>();
	private int status = 204;
	
	@Override
	public InputStream body() {

		return body.getResponseBody();
	}
	
	public DefaultResponseDetail body(final ResponseBodyProvider body) {
		
		this.body = body;

		if (this.status == 204) {
			/* 
			 * assume user gives non-empty content, so we must set the code to non-204 
			 * per HTTP spec
			 */
			this.status = 200;
		}
		
		String contentType = String.format(
								CONTENT_TYPE_HEADER_TEMPLATE, 
								body.getContentType(), 
								body.getCharset());
		
		header(CommonHttpHeaders.CONTENT_TYPE.toHttpString(), contentType);
		
		return this;
	}

	@Override
	public Map<String, String> headers() {

		return headers;
	}
	
	public DefaultResponseDetail headers(final Map<String, String> headers) {
		
		this.headers = headers;
		
		return this;
	}
	
	public DefaultResponseDetail header(final String name, final String value) {
		
		this.headers.put(name, value);
		
		return this;
	}
	
	@Override
	public int status() {

		return status;
	}
	
	public DefaultResponseDetail status(int status) {
		
		this.status = status;
		
		return this;
	}

}
