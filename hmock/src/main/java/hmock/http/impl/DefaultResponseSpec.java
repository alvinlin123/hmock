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
import hmock.http.RequestSpec;
import hmock.http.ResponseBodyProvider;
import hmock.http.ResponseDetail;
import hmock.http.ResponseSpec;
import hmock.http.impl.responseproviders.InputStreamResponseBodyProvider;
import hmock.http.impl.responseproviders.StringResponseBodyProvider;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class DefaultResponseSpec implements ResponseSpec {

	private DefaultRequestSpec _requestSpec;

	private int status = 200;
	private ResponseBodyProvider body = new StringResponseBodyProvider("");
	private Map<String, String> headers = new HashMap<String, String>();
	
	public DefaultResponseSpec(DefaultRequestSpec requestSpec) {
		
		this._requestSpec = requestSpec;
	}

	@Override
	public ResponseSpec status(final int status) {

		this.status = status;
		
		return this;
	}

	@Override
	public ResponseSpec header(final String name, final String value) {

		this.headers.put(name, value);
		
		return this;
	}
	
	@Override
	public ResponseSpec contentType(String value) {
		
		this.headers.put(CommonHttpHeaders.CONTENT_TYPE.toHttpString(), value);
		
		return this;
	}

	@Override
	public ResponseSpec body(final InputStream body) {

		InputStreamResponseBodyProvider provider = new InputStreamResponseBodyProvider(body);
				
		return body(provider);
	}

	@Override
	public ResponseSpec body(final String body) {
		
		StringResponseBodyProvider provider = new StringResponseBodyProvider(body);
		
		return body(provider);
	}

	@Override
	public ResponseSpec body(final ResponseBodyProvider body) {
		
		this.body = body;
			
		return this;
	}
	
	@Override
	public RequestSpec when() {
		
		return _requestSpec;
	}
	
	public ResponseDetail generateResponse(final HttpServletRequest request) {
		
		DefaultResponseDetail respDetail = new DefaultResponseDetail();
		
		/* note that oder of setting the properties is important */
		respDetail.status(this.status);
		respDetail.headers(this.headers);
		respDetail.body(this.body);
		
		return respDetail;
	}
}
