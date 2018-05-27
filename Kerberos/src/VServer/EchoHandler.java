package VServer;

import java.util.HashMap;
import java.util.Map;

import Socket.Connection;
import Socket.MessageHandler;
import Tools.Des;
import Tools.Tool;

class EchoHandler implements MessageHandler {
	@Override
	public void onReceive(Connection connection, String message) {
		System.out.println("Got a message from a client:");
		System.out.println(message);
		Map<String, String> map = unpack(message);
		
		
		Tool tool = new Tool();
		Des des=new Des();
		String TICKET_V = map.get("TICKET_V");
		String Authenticator_C = map.get("Authenticator_C");
		Map<String, String> ticketMap=unpackTicket_V(des.Decrypt(TICKET_V, "cccccccc"));
		Map<String, String> authenticator_cMap=unpackAuthenticator_C(des.Decrypt(Authenticator_C,ticketMap.get("K_C_V")));
		String TS5=authenticator_cMap.get("TS5");
		String returnMessage =des.Encrypt(TS5+1, ticketMap.get("K_C_V")) ;
		System.out.println("K_C_V is "+ticketMap.get("K_C_V"));
		System.out.println("Send  message  to the client.");
		connection.println(returnMessage);
	}

	public Map<String, String> unpack(String message) {// 将client传得消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = {"TICKET_V", "Authenticator_C" };
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
		}
		return map;
	}
	public Map<String, String> unpackTicket_V(String message) {// 将client传得消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "K_C_V", "ID_C", "AD_C","ID_V","TS4","Lifetime4"};
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
		}
		return map;
	}
	public Map<String, String> unpackAuthenticator_C(String message) {// 将client传得消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "ID_C", "ADC","TS5"};
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
		}
		return map;
	}
}