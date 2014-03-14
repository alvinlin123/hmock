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
package hmock.http.impl;

import hmock.http.RequestSpec;
import hmock.http.HttpMethods;
import hmock.http.impl.uri.ParameterizedPath;
import hmock.http.impl.uri.ParameterizedPath.PathMatchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matcher;

public class DefaultRequestSpec implements RequestSpec {

    private ParameterizedPath _path;
    private String _supportedMethod;
    
    private Map<String, List<Matcher<String>>> _parameterMatchers = new HashMap<String, List<Matcher<String>>>();
    private Map<String, List<Matcher<String>>> _pathParamMatchers = new HashMap<String, List<Matcher<String>>>();
    private Map<String, List<Matcher<String>>> _headerMatchers = new HashMap<String, List<Matcher<String>>>();
    
    @Override
    public RequestSpec head(String path) {
        
        this._path = new ParameterizedPath(path);
        this._supportedMethod = HttpMethods.HEAD.name();
        return this;
    }
    
    @Override
    public RequestSpec delete(String path) {
        
        this._path = new ParameterizedPath(path);
        this._supportedMethod = HttpMethods.DELETE.name();
        return this;
    }
    
    @Override
    public RequestSpec get(String path) {
        
        this._path = new ParameterizedPath(path);
        this._supportedMethod = HttpMethods.GET.name();
        return this;
    }
    
    @Override
    public RequestSpec post(String path) {
        
        this._path = new ParameterizedPath(path);
        this._supportedMethod = HttpMethods.POST.name();
        return this;
    }
    
    @Override
    public RequestSpec put(String path) {
        
        this._path = new ParameterizedPath(path);
        this._supportedMethod = HttpMethods.PUT.name();
        return this;
    }
    
    @Override
    public RequestSpec pathparam(final String name, final Matcher<String> matcher) {

        addMatcherToMap(name, matcher, _pathParamMatchers);
        
        return this;
    }

    @Override
    public RequestSpec param(final String name, final  Matcher<String> matcher) {

        addMatcherToMap(name, matcher, _parameterMatchers);
        
        return this;
    }

    @Override
    public RequestSpec header(final String name, final Matcher<String> matcher) {
        
        addMatcherToMap(name, matcher,_headerMatchers);
        
        return this;
    }
    
    public boolean canHandle(final HttpServletRequest request) {
        
        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();
        
        return _supportedMethod.equals(httpMethod) 
                && isPathMatches(uri)
                && isHeaderMatches(request)
                && isParameterMatches(request);
    }

    private boolean isHeaderMatches(HttpServletRequest request) {
        
        boolean matches = true;
        
        for (Map.Entry<String, List<Matcher<String>>> entry : _headerMatchers.entrySet()) {
            
            String headerName = entry.getKey();
            String headerValue = request.getHeader(headerName);
            
            matches = matchesAll(headerValue, entry.getValue());        
            
            if (!matches) {
                break;
            }
        }
        
        return matches;
    }

    private boolean isPathMatches(final String otherPath) {

        if (otherPath == null || otherPath.isEmpty()) {
            return false;
        }
        
        PathMatchResult result = _path.matches(otherPath);
        
        if (!result.matched()) {
            return false;
        }

        boolean matches = true;
        for (Map.Entry<String, List<Matcher<String>>> entry : _pathParamMatchers.entrySet()) {
            
            String paramName = entry.getKey();
            String value = result.getParameters().get(paramName);
            
            matches = matchesAll(value, entry.getValue());
            
            if (!matches) {
                break;
            }
                         
        }
        
        return matches;
    }
    
    private boolean isParameterMatches(final HttpServletRequest request) {
        
        boolean matches = true;
        for (Map.Entry<String, List<Matcher<String>>> entry : _parameterMatchers.entrySet()) {
            
            String name = entry.getKey();
            String value = request.getParameter(name);
            matches = matchesAll(value, entry.getValue());
            
            if (!matches) {
                break;
            }
        }
        
        return matches;
    }
    
    private boolean matchesAll(final String value, final List<Matcher<String>> matchers) {
        
        boolean matches = true;
        
        for (Matcher<String> matcher : matchers) {
            
            if (!matcher.matches(value)) {
                matches = false;
                break;
            }
        }
        
        return matches;
    }
    
    private void addMatcherToMap(
            final String name, 
            final Matcher<String> matcher, 
            final Map<String, List<Matcher<String>>> map) {
        
        List<Matcher<String>> matchers = map.get(name);
        
        if (matchers == null) {
            matchers = new ArrayList<Matcher<String>>();
            map.put(name, matchers);
        }
        
        matchers.add(matcher);
    }
}
