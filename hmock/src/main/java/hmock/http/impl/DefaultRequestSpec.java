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
import hmock.http.SupportedHttpMethods;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class DefaultRequestSpec implements RequestSpec {

	private String _path;
	private String _supportedMethod;
	
	@Override
	public RequestSpec get(String path) {
		
		this._path = path;
		this._supportedMethod = SupportedHttpMethods.GET.name();
		return this;
	}
	
	@Override
	public RequestSpec pathparam(final String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestSpec param(final String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestSpec header(final String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean canHandle(final HttpServletRequest request) {
		
		String httpMethod = request.getMethod();
		String uri = request.getRequestURI();
		
		return _supportedMethod.equals(httpMethod) && _path.equals(uri);
	}
}
