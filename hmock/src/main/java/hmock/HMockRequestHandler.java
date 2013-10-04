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

import hmock.http.impl.BaseRequestSpec;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.DefaultHandler;

class HMockRequestHandler extends DefaultHandler {

	private ArrayList<BaseRequestSpec> _mockRequests = new ArrayList<BaseRequestSpec>();
	
	@Override
	public void handle(
			String target, 
			Request baseRequest,
			HttpServletRequest request, 
			HttpServletResponse response)
	throws IOException, ServletException {
	
		try {
			for (BaseRequestSpec mockRequest : _mockRequests) {
				
				if (!mockRequest.canHandle(request)) {
					continue;
				}
				
				mockRequest.handle(request, response);
				baseRequest.setHandled(true);
				return;
			}
		} catch (Throwable e) {
			//if anything goes wrong does requesting handling, print out the stack trace for debugging.
			e.printStackTrace();
		}
	}

	public void addRequest(BaseRequestSpec req) {
		
		_mockRequests.add(req);
	}
}