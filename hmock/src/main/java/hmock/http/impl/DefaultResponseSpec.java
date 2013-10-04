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

import hmock.http.ResponseBodyProvider;
import hmock.http.ResponseCondition;
import hmock.http.ResponseDetail;
import hmock.http.ResponseSpec;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultResponseSpec implements ResponseSpec {

private static final Logger LOGGER = LoggerFactory.getLogger(DefaultResponseSpec.class);
	
	public static final String CONTENT_TYPE_HEADER = "Content-Type";

	private BaseRequestSpec _requestSpec;
	private DefaultResponseCondition _currentCondition;
	private DefaultResponseDetail _currentResponseDetail;
	
	/* to be used if no condition is fulfilled */
	private DefaultResponseDetail _fallbackResponseDetail = new DefaultResponseDetail();

	private HashMap<DefaultResponseCondition, DefaultResponseDetail> _responseDetailCache = 
			new HashMap<DefaultResponseCondition, DefaultResponseDetail>();
	
	public DefaultResponseSpec(BaseRequestSpec requestSpec) {
		
		this._requestSpec = requestSpec;
	}
	
	@Override
	public ResponseCondition on() {

		_currentCondition = new DefaultResponseCondition(this);
		_currentResponseDetail = new DefaultResponseDetail();
		
		/* for user configured (non-default) response, it makes more sense to return 200*/
		_currentResponseDetail.status(200);
		
		this._responseDetailCache.put(_currentCondition, _currentResponseDetail);
		
		return _currentCondition;
	}

	@Override
	public ResponseSpec status(final int status) {

		if (_currentCondition == null) {
			_fallbackResponseDetail.status(status);
		} else {
			_currentResponseDetail.status(status);
		}
		
		return this;
	}

	@Override
	public ResponseSpec header(final String name, final String value) {

		if (_currentCondition == null) {
			_fallbackResponseDetail.header(name, value);
		} else {
			_currentResponseDetail.header(name, value);
		}
		
		return this;
	}
	
	@Override
	public ResponseSpec contentType(String value) {
		
		if (_currentCondition == null) {
			_fallbackResponseDetail.header(CONTENT_TYPE_HEADER, value);
		} else {
			_currentResponseDetail.header(CONTENT_TYPE_HEADER, value);
		}
		
		return this;
	}

	@Override
	public ResponseSpec body(final InputStream body) {

		if (_currentCondition == null) {
			_fallbackResponseDetail.body(body);
		} else {
			_currentResponseDetail.body(body);
		}
		
		return this;
	}

	@Override
	public ResponseSpec body(final String body) {
		
		InputStream bodyIs = null;
		try {
			bodyIs = new ByteArrayInputStream(body.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			
			/*should never come here*/
			LOGGER.error("We don't have UTF-8?", e);
		}
		
		return body(bodyIs);
	}

	@Override
	public ResponseSpec body(final ResponseBodyProvider body) {
		
			
		return null;
	}
	
	public ResponseDetail generateResponse(final HttpServletRequest request) {
		
		ResponseDetail respDetail = null;
		
		if (request == null || _responseDetailCache.isEmpty()) {
			
			respDetail = this._fallbackResponseDetail;
		} else {
			
			respDetail = getResponseDetailWithConditions(request);
		}
		
		return respDetail;
	}

	private ResponseDetail getResponseDetailWithConditions(final HttpServletRequest request) {

		ResponseDetail detail = null;
		
		for (Map.Entry<DefaultResponseCondition, DefaultResponseDetail> entry : _responseDetailCache.entrySet()) {
			
			if (entry.getKey().satisfied(request)) {
				detail = entry.getValue();
				break;
			}
		}
		
		if (detail == null) {
			detail = this._fallbackResponseDetail;
		}
		
		return detail;
	}
}
