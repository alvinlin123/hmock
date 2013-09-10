package hmock.http.impl;

import hmock.HMockException;
import hmock.http.ResponseBodyProvider;
import hmock.http.ResponseBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultResponseBuilder implements ResponseBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultResponseBuilder.class);
	
	public static final String CONTENT_TYPE_HEADER = "Content-Type";
	
	private Map<String, String> _headers = new HashMap<String, String>();
	private byte[] _buffer;
	private int status = 200;
	
	@Override
	public ResponseBuilder status(final int status) {

		this.status = status;
		
		return this;
	}

	@Override
	public ResponseBuilder header(final String name, final String value) {
		
		_headers.put(name, value);
		
		return this;
	}

	@Override
	public ResponseBuilder body(final String body) {
		
		ResponseBuilder rBuilder = null;
		try {
			rBuilder = body(new ByteArrayInputStream(body.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			
			throw new HMockException("should never get this error", e);
		}
		
		return rBuilder;
	}


	@Override
	public ResponseBuilder body(final File body) {

		InputStream stream = null; 
		
		try {
			stream = new FileInputStream(body);
			body(stream);
		} catch (IOException e) {
			throw new HMockException("cannot read file", e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					
					LOGGER.error("Error while closing file input stream: " + body.getAbsolutePath(), e);
				}
			}
		}
		
		return this;
	}

	@Override
	public ResponseBuilder body(final ResponseBodyProvider body) {

		contentType(body.getContentType(), body.getCharset());
		
		return body(body.getResponseBody());
	}
	
	@Override
	public ResponseBuilder body(final InputStream body) {
		
		try {
			_buffer = IOUtils.toByteArray(body);
		} catch (IOException e) {
			throw new HMockException("can't create response", e);
		}
		
		return this;
	}
	
	@Override
	public ResponseBuilder contentType(String contentType) {

		header(CONTENT_TYPE_HEADER, contentType);
		
		return this;
	}

	@Override
	public ResponseBuilder contentType(String contentType, String charset) {

		if (StringUtils.isBlank(contentType)) {
			return this;
		}
		
		header(CONTENT_TYPE_HEADER, 
			   contentType + (StringUtils.isBlank(charset) ? "" : "; charset=" + charset));
		
		return this;
	}

	public Map<String, String> headers() {
		
		HashMap<String, String> copy = new HashMap<String, String>();
		
		copy.putAll(_headers);
		
		return copy;
	}
	
	public InputStream body() {
		
		return new ByteArrayInputStream(_buffer);
	}
	
	public int status() {
		
		return this.status;
	}
}
