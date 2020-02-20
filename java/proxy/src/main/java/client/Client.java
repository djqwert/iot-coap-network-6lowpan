package client;

import java.util.Scanner;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;

import utilities.*;

public class Client extends CoapClient {
	
	private String[] resource;
	private int id;
	private int connection_attempts;
	
	private CoapClient client;
	private CoapHandler handler;
	private CoapObserveRelation relation;
	
	public static void main(String args[]) {
		
		CoapClient client = new CoapClient("coap://127.0.0.1/database");
		client.setTimeout(3000);
		
		System.out.println("[INFO] Node ID have an ID between 1 and " + Parameters.MOTE_NUMBER);
		System.out.println("Type a node ID to get its value or 'bye' to close the application.");		

		while(true){
			
            System.out.print(">> ");
			Scanner input = new Scanner(System.in);
			
            String value = input.nextLine();
            
            if(value.compareTo("bye") == 0) {
            	System.out.println("Client closed");
            	input.close();
            	System.exit(0);
            }
			
            try {
            	
				Request req = new Request(Code.GET);
				req.getOptions().addUriQuery("mote=" + value);
				req.getOptions().setAccept(MediaTypeRegistry.APPLICATION_JSON);
				CoapResponse response = client.advanced(req);
				
				if(response != null)
					Utilities.fromSenMLToText(value, response.getResponseText());
				else
					System.out.println("[WARN] Server is not responding...");
           
            } catch(NumberFormatException nfe) {
        		System.out.println("[WARN] You have to insert a mote ID between 1 and " + Parameters.MOTE_NUMBER);
        	}
		}
		
	}

	public Client(int cid, String address, String[] array){
		
		resource = array;		// Accedo alla sola risorsa del client
		id = new Integer(cid);
		client = new CoapClient("coap://[" + address + "]/" + Parameters.PATH);
		System.out.println("[INFO] ClientID=" + id + " bind with " + address + " address");
		client.setTimeout(5000);
		
		handler = new CoapHandler() {
			
			public void onLoad(CoapResponse response) {
				
					System.out.println("[INFO] ClientID=" + id + " observes from MoteID=" + Integer.toString(id + 1));
					
					connection_attempts = 0;
					
					if(response.getCode() == ResponseCode.NOT_ACCEPTABLE)
						
						Utilities.fromSenMLToText(Integer.toString(id+ 1),response.getResponseText());
					
					else {
						
						resource[id] = response.getResponseText();
						if(Parameters.DEBUG)
							System.out.println("[DEBUG] MoteID gave value:\n" + resource[id] );
						} 
				
				}
			
			public void onError() {
				
				if(connection_attempts <= Parameters.ATTEMPTS) {
					System.out.println("[INFO] MoteID=" + Integer.toString(id + 1) + " unreachable. ClientID=" + id + " tries again to observe from MoteID=" + Integer.toString(id + 1));
					relation = client.observe(handler);
					connection_attempts++;
				} else {
					System.err.println("[ERR] ClientID=" + id + " stopped observing node with MoteID=" + Integer.toString(id + 1));
				}
			
			}
			
		};
		
		connection_attempts = 0;
		
		getObserve();
		
	}
	
	void deleteObserve() {
		
		relation.proactiveCancel();
		
	}
	
	void getObserve() {
			
		relation = client.observe(handler);
	
	}
}