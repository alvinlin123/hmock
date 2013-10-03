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

import hmock.http.RequestBuilder;
import hmock.http.ResponseBuilder;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matcher;

public class BaseRequestBuilder implements RequestBuilder {

	private DefaultResponseBuilder _responseBuilder;
	private String _path;
	
	public BaseRequestBuilder(final String path) {
		
		_path = path;
	}
	
	@Override
	public RequestBuilder pathparam(final String name, final Matcher<String> value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestBuilder param(final String name, final Matcher<String> value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestBuilder header(final String name, final Matcher<String> value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseBuilder respond() {
		
		_responseBuilder = new DefaultResponseBuilder();
		
		return _responseBuilder;
	}
	
	public boolean canHandle(final HttpServletRequest request) {
		
		return _path.equals(request.getRequestURI());
	}
	
	public void handle(final HttpServletRequest servRequest, final HttpServletResponse servResponse) 
	throws IOException {
		
		InputStream response = _responseBuilder.body();
		
		servResponse.setStatus(_responseBuilder.status());
		IOUtils.copy(response, servResponse.getOutputStream());
	}
}
