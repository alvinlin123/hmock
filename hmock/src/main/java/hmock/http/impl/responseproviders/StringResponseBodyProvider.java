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
package hmock.http.impl.responseproviders;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringResponseBodyProvider extends ResponseBodyContentTypeProvider {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StringResponseBodyProvider.class);
    
    public static final String DEFAULT_CHARSET = "us-ascii";
    public static final String DEFAULT_CONTENT_TYPE = "text/plain";
    
    private String _body = "";
    
    public StringResponseBodyProvider(final String body) {
        
        super(DEFAULT_CONTENT_TYPE, DEFAULT_CHARSET);
        this._body = body;
    }
    
    public StringResponseBodyProvider(final String body, final String charset) {
    
        super(DEFAULT_CONTENT_TYPE, charset);
        this._body = body;
    }
    
    public StringResponseBodyProvider(
            final String body, 
            final String contentType, 
            final String charset) {
        
        super(contentType, charset);
        this._body = body;
    }

    @Override
    public InputStream getResponseBody() {

        InputStream stringIs = null;
        try {
            stringIs = new ByteArrayInputStream(_body.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("we don't have utf-8!?");
        }
        
        return stringIs;
    }
}
