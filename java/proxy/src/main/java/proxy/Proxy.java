package proxy;

import client.Client;
import proxy.Server;
import utilities.Parameters;

public class Proxy {
	
	private Server CoAPServer;
	private Client[] CoAPClients;
	
	private String[] array;	// Cache

	public static void main(String args[]) {
		
		System.out.println("Proxy is starting...");
		
		switch(args.length) {
		
		case 1:
			if(args[0].equals("z1"))
				new Proxy(Parameters.MOTE_NUMBER, 0);
			else if(args[0].equals("cooja"))
				new Proxy(Parameters.MOTE_NUMBER, 1);
			else {
				System.err.println("[ERR] Proxy requires 1 mandatory arg: the type of mote used (z1 or cooja)");
				System.exit(-1);
			}
			break;			
			
		case 2:
			if(args[0].equals("z1"))
				new Proxy(Integer.parseInt(args[1]), 0);
			else if(args[0].equals("cooja"))
				new Proxy(Integer.parseInt(args[1]), 1);
			else {
				System.err.println("[ERR] Proxy requires 2 mandatory args: the number of motes and the type of mote used (z1 or cooja)");
				System.exit(-1);
			}
			Parameters.MOTE_NUMBER = Integer.parseInt(args[1]);
			break;
			
		default:
			System.out.println("[ERR] Arguments not valid");
			System.exit(-1);
	}

		System.out.println("Proxy started...");
		
	}
	
	Proxy(int n, int mote){
		
		array = new String[n];
		for(int i=0; i<n; i++) {
			array[i] = "";
		}
		
		CoAPServer = new Server(array);
		Client[] CoAPClients = new Client[n];
		
		int address;
		String addr;
		for (int i = 0; i < n; i++) {
			
			address =  i + Parameters.START_INTERFACE_ID;
			addr = Integer.toHexString(address);
			
			if(mote == 0) {
				
				CoAPClients[i] = new Client(i, Parameters.PREFIX_Z1 + addr, array);
				
			}else {
			
				if(address < 16) 
					CoAPClients[i] = new Client(i, Parameters.PREFIX_COOJA + "0" + addr + ":" + addr + ":" + addr + ":" + addr, array);
				else
					CoAPClients[i] = new Client(i, Parameters.PREFIX_COOJA + addr + ":" + addr + ":" + addr + ":" + addr, array);
				
			}
			
		}
		
	}	
}