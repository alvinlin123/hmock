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
package hmock.http;

import java.io.File;
import java.io.InputStream;

public interface ResponseBuilder {

	public ResponseBuilder status(int status);
	
	/**
	 * Sets the HTTP "Content-Type" header.
	 * 
	 * @param contentType
	 *  A non-empty string.
	 * 
	 * @return
	 *  This builder.
	 */
	public ResponseBuilder contentType(String contentType);
	
	/**
	 * Sets the HTTP "Content-Type" header with charset. 
	 *  
	 * @param contentType
	 * 	A non-empty string.
	 * 
	 * @param charset
	 *  A non-empty string. If it's empty then it's effectively like calling 
	 *  {@link #contentType(String)}
	 * 
	 * @return
	 *  This builder.
	 */
	public ResponseBuilder contentType(String contentType, String charset);
	
	public ResponseBuilder header(String name, String value);
	public ResponseBuilder body(String body);
	public ResponseBuilder body(File body);
	
	/**
	 * Build a HTTP response body with the given {@link ResponseBodyProvider}. 
	 * 
	 * Previously set content type will be overwritten with {@link ResponseBodyProvider#getContentType()} if
	 * the returned value is not null. 
	 * 	   
	 * @param provider
	 * 
	 * @return
	 *  This builder.
	 */
	public ResponseBuilder body(ResponseBodyProvider provider);
	
	public ResponseBuilder body(InputStream body);
}
