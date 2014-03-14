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
package hmock.http.impl.uri;

import static org.junit.Assert.*;
import hmock.http.impl.uri.ParameterizedPath.PathMatchResult;

import org.junit.Test;

public class ParameterizedPathTest {

    @Test
    public void testExactMatchRoot() {
        
        ParameterizedPath path = new ParameterizedPath("/");
        
        assertEquals(true, path.matches("/").matched());
        assertEquals(true, path.matches("/").getParameters().isEmpty());
        assertEquals(false, path.matches("/some/path").matched());
        
    }
    
    @Test
    public void testExactMatchNonCanonicalPath() {
        
        ParameterizedPath path = new ParameterizedPath("//some//path/");
        
        assertEquals(true, path.matches("/some/path").matched());
        assertEquals(true, path.matches("/some//path/").matched());
    }
    
    @Test
    public void testParamPathNonCanonicalPath() {
        
        ParameterizedPath path = new ParameterizedPath("/employees//{first-name}///{last-name}");
        
        PathMatchResult result = path.matches("employees//john/doe");
        assertEquals(true, result.matched());
        assertEquals("john", result.getParameters().get("first-name"));
        assertEquals("doe", result.getParameters().get("last-name"));
        
        result = path.matches("employees/john/doe");
        assertEquals(true, result.matched());
        assertEquals("john", result.getParameters().get("first-name"));
        assertEquals("doe", result.getParameters().get("last-name"));
    }
    
    @Test
    public void testParamPathCanonicalPath() {
        
        ParameterizedPath path = new ParameterizedPath("/employees/{first-name}/{last-name}");
    
        PathMatchResult result = path.matches("employees//john/doe");
        assertEquals(true, result.matched());
        assertEquals("john", result.getParameters().get("first-name"));
        assertEquals("doe", result.getParameters().get("last-name"));
        
        result = path.matches("employees/john/doe");
        assertEquals(true, result.matched());
        assertEquals("john", result.getParameters().get("first-name"));
        assertEquals("doe", result.getParameters().get("last-name"));
    }    
    
    @Test
    public void testToStringNoCanonicalForm() {
        
        ParameterizedPath path = new ParameterizedPath("/this//is///path/");
        
        assertEquals("/this/is/path", path.toString());
    }
}
