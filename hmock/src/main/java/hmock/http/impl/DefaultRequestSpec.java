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
import hmock.http.SupportedHttpMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matcher;

public class DefaultRequestSpec implements RequestSpec {

	private String _path;
	private String _supportedMethod;
	
	private Map<String, List<Matcher<String>>> _parameterMatchers = new HashMap<String, List<Matcher<String>>>();
	
	@Override
	public RequestSpec get(String path) {
		
		this._path = path;
		this._supportedMethod = SupportedHttpMethods.GET.name();
		return this;
	}
	
	@Override
	public RequestSpec pathparam(final String name, final Matcher<String> matcher) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestSpec param(final String name, final  Matcher<String> matcher) {

		List<Matcher<String>> matchers = _parameterMatchers.get(name);
		
		if (matchers == null) {
			matchers = new ArrayList<Matcher<String>>();
			_parameterMatchers.put(name, matchers);
		}
		
		matchers.add(matcher);
		
		return this;
	}

	@Override
	public RequestSpec header(final String name, final Matcher<String> matcher) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean canHandle(final HttpServletRequest request) {
		
		String httpMethod = request.getMethod();
		String uri = request.getRequestURI();
		boolean parameterMatches = isParameterMatches(request);
		
		return _supportedMethod.equals(httpMethod) && _path.equals(uri) && parameterMatches;
	}

	private boolean isParameterMatches(HttpServletRequest request) {
		
		boolean matches = true;
		for (Map.Entry<String, List<Matcher<String>>> entry : _parameterMatchers.entrySet()) {
			
			String name = entry.getKey();
			String value = request.getParameter(name);
			
			for (Matcher<String> matcher : entry.getValue()) {
				
				if (!matcher.matches(value)) {
					matches = false;
					break;
				}
			}
			
			if (!matches) {
				break;
			}
		}
		
		return matches;
	}
}
