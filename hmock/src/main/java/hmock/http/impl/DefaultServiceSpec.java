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

import hmock.http.RequestSpec;
import hmock.http.ResponseDetail;
import hmock.http.ResponseSpec;
import hmock.http.ServiceSpec;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class DefaultServiceSpec implements ServiceSpec {

    private DefaultRequestSpec _requestSpec;
    private DefaultResponseSpec _responseSpec;
    
    public DefaultServiceSpec(DefaultRequestSpec requestSpec, DefaultResponseSpec responseSpec) {
        
        this._requestSpec = requestSpec;
        this._responseSpec = responseSpec;
    }

    @Override
    public RequestSpec getRequestSpec() {

        return _requestSpec;
    }

    @Override
    public ResponseSpec getResponseSpec() {

        return _responseSpec;
    }

    @Override
    public boolean canHandle(HttpServletRequest request) {
        
        return _requestSpec.canHandle(request);
        
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) 
    throws IOException {
        
        ResponseDetail detail = _responseSpec.generateResponse(request);
        
        response.setStatus(detail.status());
        injectHeaders(response, detail.headers());
        
        InputStream body = null;
        try {
             body = detail.body();
            IOUtils.copy(body, response.getOutputStream());
        } finally {
            if (body != null) {
                body.close();
            }
        }
        
    }
    
    private void injectHeaders(HttpServletResponse servResponse, Map<String, String> headers) {

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            
            servResponse.addHeader(entry.getKey(), entry.getValue());
        }
    }
    
}
