package utilities;

import java.io.IOException;

import teamethernet.senmlapi.JsonFormatter;
import teamethernet.senmlapi.Label;
import teamethernet.senmlapi.SenMLAPI;

public class Utilities {
		
	public static void fromSenMLToText(String value, String json){
		
		if(Parameters.DEBUG)
			System.out.println("[DEBUG] Msg received: " + json);
		
		try {
			SenMLAPI<?> senMLAPI = SenMLAPI.initJson(json.getBytes());
			
			if(senMLAPI.getValue(Label.NAME, 0).equals("error"))
				System.out.println("[ERR] " + senMLAPI.getValue(Label.STRING_VALUE, 0));
			else
				System.out.println("Mote " + value + " gave value: " + senMLAPI.getValue(Label.VALUE, 0).toString());
		
		} catch(Exception e) {
			e.printStackTrace();
	    	System.out.println("[ERR] Something went wrong extracting from SenML");
		}
			
	}
	
	public static double fromSenMLToInt(String json) throws IOException{

		SenMLAPI<?> senMLAPI = SenMLAPI.initJson(json.getBytes());
    	
    	return (double) senMLAPI.getValue(Label.VALUE, 0);
	
	}
	
	public static String setErrorSenML(String error) {
		
		SenMLAPI<JsonFormatter> senMLAPI = SenMLAPI.initJson();
		senMLAPI.addRecord(Label.NAME.attachValue("error"), Label.STRING_VALUE.attachValue(error));
		String ret = null;
		try {
			ret = new String(senMLAPI.getSenML());
		}catch(Exception e) {
			ret = new String("[{\"n\":\"error\",\"sv\":\"Generic error\"}]");
		}
		return ret;
		
	}

}