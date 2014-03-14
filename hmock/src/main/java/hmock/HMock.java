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
package hmock;

import hmock.http.ResponseSpec;
import hmock.http.impl.DefaultRequestSpec;
import hmock.http.impl.DefaultResponseSpec;
import hmock.http.impl.DefaultServiceSpec;

import org.eclipse.jetty.server.Server;

public class HMock {

    /* default port is 7357 (test) */
    public static final int DEFAULT_PORT = 7357;
    
    private static int port = DEFAULT_PORT;
    
    private static Server _mockServer;
    
    private static HMockRequestHandler _requestHandler = new HMockRequestHandler();
    
    public static void setPort(int port) {
        
        HMock.port = port;
    }
    
    private synchronized static void ensureInitialized() {
        
        setupShutdownHook();
        setupServer();
    }
    
    private static void setupServer() {
        
        if (_mockServer != null) {
            return;
        }
        
        _mockServer = new Server(HMock.port);
        _mockServer.setThreadPool(new DaemonThreadPool());
        _mockServer.setHandler(_requestHandler);
        try {
            _mockServer.start();
        } catch (Exception e) {
            throw new HMockException("unable to start server", e);
        }
    }

    private static void setupShutdownHook() {
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            
            @Override
            public void run() {
                
                if (_mockServer == null) {
                    return;
                }
                
                try {
                    _mockServer.stop();
                } catch (Exception e) {
                    /*Yea just print something to the console if jetty shutdown failed*/
                    e.printStackTrace();
                }
            }
        }));
    }
    
    /**
     * Removes currently registered mock request handlers.
     */
    public static void reset() {
        
        _requestHandler = new HMockRequestHandler();
    }
    
    public static ResponseSpec respond() {
        
        ensureInitialized();
        
        DefaultRequestSpec requestSpec = new DefaultRequestSpec();
        DefaultResponseSpec responseSpec = new DefaultResponseSpec(requestSpec);
        DefaultServiceSpec serviceSpec = new DefaultServiceSpec(requestSpec, responseSpec);
        
        _requestHandler.addSpec(serviceSpec);
        
        return responseSpec;
    }
    
    /* instantiate this class does not make any sense */
    private HMock() {};
}
