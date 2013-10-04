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

import static org.hamcrest.Matchers.equalTo;
import hmock.http.ResponseCondition;
import hmock.http.ResponseSpec;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matcher;

public class DefaultResponseCondition implements ResponseCondition {

	private DefaultResponseSpec _responseSpec;

	private Map<String, Matcher<String>> _paramMatchers = new HashMap<String, Matcher<String>>();
	
	public DefaultResponseCondition(final DefaultResponseSpec responseSpec) {
		
		this._responseSpec = responseSpec;
	}
	
	@Override
	public ResponseCondition param(final String name, final Matcher<String> matcher) {

		_paramMatchers.put(name, matcher);
		
		return this;
	}

	@Override
	public ResponseCondition param(final String name, final String exactMatch) {

		return param(name, equalTo(exactMatch));
	}

	@Override
	public ResponseSpec reply() {

		return _responseSpec;
	}
	
	public boolean satisfied(HttpServletRequest request) {
		
		if (_paramMatchers.isEmpty()) {
			/* vacuously true */
			return true;
		}
		
		boolean satisfied = true;
		
		for (Map.Entry<String, Matcher<String>> entry : _paramMatchers.entrySet()) {
			
			String value = request.getParameter(entry.getKey());
			
			if (!entry.getValue().matches(value)) {
				satisfied = false;
				break;
			}
		}
		
		return satisfied;
	}
}
