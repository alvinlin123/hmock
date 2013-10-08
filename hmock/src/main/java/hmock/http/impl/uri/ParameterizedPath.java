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
package hmock.http.impl.uri;

import java.util.HashMap;
import java.util.Map;

public final class ParameterizedPath {

	public static String toCanonicalForm(final String path) {
		
		String cForm = path;
		
		cForm = cForm.trim().replaceAll("//+", "/");
		
		if ("/".equals(cForm)) {
		
			return cForm;
		}
		
		cForm = cForm.startsWith("/") ? cForm : "/" + cForm;
		cForm = cForm.endsWith("/") ? cForm.substring(0, cForm.length() - 1) : cForm;
		
		return cForm;
	}
	
	/* converted to canonical form */
	private String _path;
	
	public ParameterizedPath(final String path) {
		
		_path = toCanonicalForm(path);
	}
	
	public String[] pathElements() {
		
		return _path.substring(1).split("/");
	}
	
	public PathMatchResult matches(final String otherpath) {
		
		String cForm = toCanonicalForm(otherpath);
		
		if (this._path.equals(cForm)) {
			return new PathMatchResult(true);
		}
		
		String[] otherpathElements = cForm.substring(1).split("/");
		String[] myElements = this.pathElements();
		
		if (otherpathElements.length != myElements.length) {
			return new PathMatchResult(false);
		}

		boolean matched = true;
		Map<String, String> parameters = new HashMap<String, String>();
		for (int i = 0; i < myElements.length; i++) {
			
			String myElement = myElements[i];
			String otherElement = otherpathElements[i];
			
			if (isParameter(myElement)) {
				String paramName = extractParamName(myElement);
				parameters.put(paramName, otherElement);
			} else if (!myElement.equals(otherElement)) {
				matched = false;
				break;
			}
		}
		
		PathMatchResult result = null;
		
		if (matched) {
			result = new PathMatchResult(true);
			result.putParameters(parameters);
		} else {
			result = new PathMatchResult(false);
		}
				
		return result;
	}
	
	public class PathMatchResult {
		
		private boolean _matches;
		private Map<String, String> _parameters = new HashMap<String, String>();
		
		public PathMatchResult(boolean matches) {
			
			this._matches = matches;
		}
		
		public boolean matched() {
			
			return this._matches;
		}
		
		public Map<String, String> getParameters() {
			
			return _parameters;
		}
		
		void putParameters(Map<String, String> params) {
			
			_parameters.putAll(params);
		}
	}
	
	private String extractParamName(String element) {

		/*assume input had been validated*/
		
		return element.substring(1, element.length() - 1).trim();
	}

	private boolean isParameter(String element) {
		
		return element != null && element.startsWith("{") && element.endsWith("}");
	}

	@Override
	public String toString() {
		
		return _path;
	}
}
