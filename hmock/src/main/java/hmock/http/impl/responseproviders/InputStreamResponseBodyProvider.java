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

public class InputStreamResponseBodyProvider extends ResponseBodyContentTypeProvider {

    private InputStream _body = new ByteArrayInputStream("".getBytes());
    
    public InputStreamResponseBodyProvider(InputStream body) {
        
        this._body = body;
    }

    @Override
    public InputStream getResponseBody() {

        return this._body;
    }
    
    
}
