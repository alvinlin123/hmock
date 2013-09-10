package hmock.http;

import java.io.InputStream;

public interface ResponseBodyProvider {

	public String getContentType();
	public String getCharset();
	public InputStream getResponseBody();
}
