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

import hmock.http.RequestSpec;
import hmock.http.ResponseDetail;
import hmock.http.ResponseSpec;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class BaseRequestSpec implements RequestSpec {

	private DefaultResponseSpec _responseSpec;
	private String _path;
	
	public BaseRequestSpec(final String path) {
		
		_path = path;
	}
	
	@Override
	public RequestSpec pathparam(final String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestSpec requiredParam(final String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestSpec requiredHeader(final String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseSpec respond() {
		
		_responseSpec= new DefaultResponseSpec(this);
		
		return _responseSpec;
	}
	
	public boolean canHandle(final HttpServletRequest request) {
		
		return _path.equals(request.getRequestURI());
	}
	
	public void handle(final HttpServletRequest servRequest, final HttpServletResponse servResponse) 
	throws IOException {
		
		ResponseDetail detail = _responseSpec.generateResponse(servRequest);
		
		servResponse.setStatus(detail.status());
		injectHeaders(servResponse, detail.headers());
		IOUtils.copy(detail.body(), servResponse.getOutputStream());
	}

	private void injectHeaders(HttpServletResponse servResponse, Map<String, String> headers) {

		for (Map.Entry<String, String> entry : headers.entrySet()) {
			
			servResponse.addHeader(entry.getKey(), entry.getValue());
		}
	}
}
