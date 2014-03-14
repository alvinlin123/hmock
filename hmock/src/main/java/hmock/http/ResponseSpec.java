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

import java.io.InputStream;

public interface ResponseSpec {

    public ResponseSpec status(int status);
    public ResponseSpec header(String name, String value);
    public ResponseSpec contentType(String contentTyep);
    public ResponseSpec body(InputStream body);
    public ResponseSpec body(String body);
    public ResponseSpec body(ResponseBodyProvider body);
    public RequestSpec when();
}
