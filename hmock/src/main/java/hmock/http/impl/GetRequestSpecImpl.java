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

import javax.servlet.http.HttpServletRequest;

import hmock.http.GetRequestSpec;

public class GetRequestSpecImpl extends BaseRequestSpec implements GetRequestSpec {

	public GetRequestSpecImpl(String path) {
		
		super (path);
	}

	@Override
	public boolean canHandle(HttpServletRequest request) {

		return "GET".equalsIgnoreCase(request.getMethod()) && super.canHandle(request);
	}
}
