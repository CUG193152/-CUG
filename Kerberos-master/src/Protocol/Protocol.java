package Protocol;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Protocol {

	/**
	 * @param args
	 */

	public LinkedHashMap<String, Integer>TICKET_TGS(){//Ticket_TGS格式规定
		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put("KEY_C_TGS", 8);
		map.put("ID_C",4);
		map.put("AD_C",12);
		map.put("ID_TGS",4);
		map.put("TS_2",10);
		map.put("LifeTime_2",4);
		return map;
	}
 
	public LinkedHashMap<String, Integer>TICKET_V(){//Ticket_V格式规定
		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put("KEY_C_V", 8);
		map.put("ID_C",4);
		map.put("AD_C",12);
		map.put("ID_V",4);
		map.put("TS_4",10);
		map.put("LifeTime_4",4);
		return map;
	}
	
	public LinkedHashMap<String, Integer>AUTHENTICATOR_C_TGS(){// Authenticator_C_TGS格式规定
		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put("ID_C",4);
		map.put("AD_C",12);
		map.put("TS_3",10);
		return map;
	}
	
	public LinkedHashMap<String, Integer>AUTHENTICATOR_C_V(){// Authenticator_C_V格式规定
		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put("ID_C",4);
		map.put("AD_C",12);
		map.put("TS_5",10);
		return map;
	}
	
	public LinkedHashMap<String, Integer> Unpack(String message){
		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		if(message.length()>=10){
			if(message.substring(4, 6).equals("01")){
				map.put("Prelude",10);
				map.put("ERROR", 10);
			}else if(message.substring(4, 6).equals("10")){
				map.put("Prelude", 10);
				map.put("Message", 10);
			}
			else if(message.substring(4, 6).equals("00")){
				String symbol=message.substring(0, 4);
				switch(symbol){
				case "0001":
					map.put("Prelude", 10);
					map.put("ID_C",4);
					map.put("ID_TGS",4);
					map.put("TS_1",10);
					break;
				case "0100":
					map.put("Prelude", 10);
					map.put("KEY_C_TGS",8);
					map.put("ID_TGS",4);
					map.put("TS_2",10);
					map.put("LifeTime_2",4);
					map.put("Ticket_TGS",384);
					break;
				case "0010":
					map.put("Prelude", 10);
					map.put("ID_V",4);
					map.put("Ticket_TGS",384);
					map.put("Authenticator_C",256);
					break;
				case "1000":
					map.put("Prelude", 10);
					map.put("KEY_C_V",8);
					map.put("ID_V",4);
					map.put("TS_4",10);
					map.put("Ticket_V",384);
					break;
				case "0011":
					map.put("Prelude", 10);
					map.put("Ticket_V",384);
					map.put("Authenticator_C",256);
					break;
				case "1100":
					map.put("Prelude", 10);
					map.put("TS_6",16);
					break;
				}
			}
		}
		else{
			System.out.println("解析包过程中出错，收到的字符串长度小于10");
		}
		for (Entry<String, Integer> entry : map.entrySet()) {  
		    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  	  
		}  
		return map;
	}
	

}
