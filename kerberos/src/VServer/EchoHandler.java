package VServer;

import java.util.HashMap;
import java.util.Map;

import As.AsDisplay;
import Helper.Des;
import Socket.Connection;
import Socket.MessageHandler;

class EchoHandler implements MessageHandler {
	VDisplay frame = new VDisplay();
	@Override
	public void onReceive(Connection connection, String message) {
		System.out.println("Got a message from a client:");
		System.out.println(message);
		frame.setVisible(true);
		Map<String, String> map = unpack(message);
		String Head=map.get("HEAD");
		
		switch(Head){
		case "000011000"://认证
			Des des=new Des();
			String TICKET_V = map.get("TICKET_V");
			String Authenticator_C = map.get("Authenticator_C");
			Map<String, String> ticketMap=unpackTicket_V(des.Decrypt(TICKET_V, "cccccccc"));
			Map<String, String> authenticator_cMap=unpackAuthenticator_C(des.Decrypt(Authenticator_C,ticketMap.get("K_C_V")));
			int Lifetime4 = Integer.parseInt(ticketMap.get("Lifetime4"));
			int TS4 = Integer.parseInt(ticketMap.get("TS4"));
			int TS5 = Integer.parseInt(authenticator_cMap.get("TS5"));
			String TicketAD_C = ticketMap.get("AD_C");
			String AuthenticatorAD_C = authenticator_cMap.get("ADC");
			String TicketID_C = ticketMap.get("ID_C");
			String AuthenticatorID_C = authenticator_cMap.get("ID_C");
			if(TicketID_C.equals(AuthenticatorID_C)&&TicketAD_C.equals(AuthenticatorAD_C)&&(TS5-TS4)<=Lifetime4)
			{
				String TS5_SEND=authenticator_cMap.get("TS5");
				String returnMessage =des.Encrypt("001100000 "+TS5_SEND+1, ticketMap.get("K_C_V")) ;
				System.out.println("K_C_V is "+ticketMap.get("K_C_V"));
				System.out.println("Send  message  to the client.");
				connection.println(returnMessage);
			}
			break;
			
		case "000011001"://获取客户端发送的电影场次信息
			
			break;
			
		case "000011010"://获取客户端发送的电影座位信息
			
			break;

		default:
			break;
		}
		
		
	}

	public Map<String, String> unpack(String message) {// 将client传得消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = {"HEAD","TICKET_V", "Authenticator_C" };
		for (int i = 0; i < strArr.length; i++) {
			frame.appenedMessage(key[i]+" "+strArr[i]+"\n");
			map.put(key[i], strArr[i]);
		}
		return map;
	}
	public Map<String, String> unpackTicket_V(String message) {// 将Ticket_V消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "K_C_V", "ID_C", "AD_C","ID_V","TS4","Lifetime4"};
		frame.appenedMessage("EncryptedTicket_V:"+"\n");
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
			frame.appenedMessage(key[i]+" "+strArr[i]+"\n");
		}
		return map;
	}
	public Map<String, String> unpackAuthenticator_C(String message){ // 将Authenticator_C消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "ID_C", "ADC","TS5"};
		frame.appenedMessage("EncryptedAuthenticator_C:"+"\n");
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
			frame.appenedMessage(key[i]+" "+strArr[i]+"\n");
		}
		frame.appenedMessage("\n\n");
		return map;
	}
}