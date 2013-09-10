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
