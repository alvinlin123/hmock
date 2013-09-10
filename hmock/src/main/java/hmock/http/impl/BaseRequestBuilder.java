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
