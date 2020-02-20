package proxy;

import org.eclipse.californium.core.CoapServer;

public class Server extends CoapServer {
	
	public Server(String[] array) {
		
		add(new Resource("database",array));
		start();
		
		System.out.println("[INFO] Server started...");
		
	}	
	
}