package proxy;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;

import utilities.*;

public class Resource extends CoapResource {
	
	private String[] resource;
	
	public Resource(String name, String[] array) {
		
		super(name);
		resource = array;
		
		getAttributes().setTitle("Database Resource");
		setObservable(false);
		
		System.out.println("[INFO] Resource created");
		
	}
	
	public void handleGET(CoapExchange exchange) {
		
		Response response = null;
		
		try {
		
			if(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON) {
	
					String value = exchange.getQueryParameter("mote");
				
					int id_mote = Integer.parseInt(value);
					
					if((id_mote > 0 && id_mote <= Parameters.MOTE_NUMBER) && !resource[id_mote - 1].equals("")) {
						
						response = new Response(ResponseCode.CONTENT);
						response.getOptions().setContentFormat(MediaTypeRegistry.APPLICATION_JSON);
						if(Parameters.DEBUG)
							System.out.println("[DEBUG] Access to resource: " + resource[id_mote - 1] + " from MoteID=" + id_mote);
						response.setPayload(resource[id_mote - 1]);
						System.out.println("[INFO] Resource sent to the client");
						
					}else if(resource[id_mote - 1].equals("")) {
						response = new Response(ResponseCode.NOT_ACCEPTABLE);
						response.setPayload(Utilities.setErrorSenML("Content of moteID not present"));
					}else{
						response = new Response(ResponseCode.NOT_ACCEPTABLE);
						response.setPayload(Utilities.setErrorSenML("MoteID not valid"));
						System.out.println("[ERR] Client sent wrong ModeID (" + value + ")");
					}
				
			}else{
				response = new Response(ResponseCode.NOT_ACCEPTABLE);
				response.setPayload(Utilities.setErrorSenML("Supporting content-types application json"));
				System.out.println("[ERR] Client sent wrong type (" + exchange.getRequestOptions().getAccept() + ")");
			}
				
			exchange.respond(response);
		
		}catch(Exception e){
			
			response = new Response(ResponseCode.NOT_ACCEPTABLE);
			response.setPayload(Utilities.setErrorSenML("Input not valid"));
			System.out.println("[EXC] Client sent wrong input");
			exchange.respond(response);
			
		}
		
	}
	
}