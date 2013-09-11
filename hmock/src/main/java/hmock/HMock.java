package hmock;

import hmock.http.GetRequestBuilder;
import hmock.http.impl.GetRequestBuilderImpl;

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
	
	public static GetRequestBuilder get(String path) {
		
		ensureInitialized();
		
		GetRequestBuilderImpl request = new GetRequestBuilderImpl(path);
		_requestHandler.addRequest(request);
		
		return request;
	}
	
	public static void post() {
		
	}
	
	public static void put() {
		
	}
	
	public static void delete() {
		
	}
	
	/* instantiate this class does not make any sense */
	private HMock() {};
}
