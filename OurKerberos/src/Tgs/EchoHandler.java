package Tgs;

import java.util.HashMap;
import java.util.Map;

import Helper.Des;
import Helper.Tool;
import Log.AppendContentToFile;
import Socket.Connection;
import Socket.MessageHandler;

class EchoHandler implements MessageHandler {
	TgsDisplay tgsDisplay = new TgsDisplay();

	@Override
	public void onReceive(Connection connection, String message) {
		System.out.println("Got a message from a client:");
		System.out.println(message);
		Map<String, String> map = unpack(message);
		String Head = map.get("HEAD");
		if (Head.equals("000010000")) {
			Tool tool = new Tool();
			Des des = new Des();
			String K_C_V = tool.KeyCreate(64);
			String ID_V = map.get("ID_V");
			String TICKET_TGS = map.get("TICKET_TGS");
			String Authenticator_C = map.get("Authenticator_C");

			Map<String, String> ticketMap = unpackTicket_TGS(des.Decrypt(TICKET_TGS, "cccccccc"));
			Map<String, String> authenticator_cMap = unpackAuthenticator_C(
					des.Decrypt(Authenticator_C, ticketMap.get("K_C_TGS")));
			int Lifetime2 = Integer.parseInt(ticketMap.get("Lifetime2"));
			int TS2 = Integer.parseInt(ticketMap.get("TS2"));
			int TS3 = Integer.parseInt(authenticator_cMap.get("TS3"));
			String TicketAD_C = ticketMap.get("AD_C");
			String AuthenticatorAD_C = authenticator_cMap.get("ADC");
			String TicketID_C = ticketMap.get("ID_C");
			String AuthenticatorID_C = authenticator_cMap.get("ID_C");
			AppendContentToFile a = new AppendContentToFile();
			a.method("/Users/apple/desktop/TGS.txt", TS2+" "+TicketID_C+" "+TicketAD_C+" "+ID_V);
			if(TicketID_C.equals(AuthenticatorID_C)&&TicketAD_C.equals(AuthenticatorAD_C)&&(TS3-TS2)<=Lifetime2) {
			String TS4 = tool.getTime();
			// Ticketv = EKV[Kc,v||IDC||ADC|| IDv||TS4||Lifetime4]
			// Tickettgs = EKtgs[Kc,tgs|| IDC|| ADC|| IDtgs || TS2 || Lifetime2]
			String LifeTime4 = "300";
			String Ticket_V = des.Encrypt(K_C_V + " " + ticketMap.get("ID_C") + " " + ticketMap.get("AD_C") + " "
					+ ticketMap.get("ID_TGS") + " " + TS4 + " " + LifeTime4, "cccccccc");
			String returnMessage = des.Encrypt("001000000 " + K_C_V + " " + ID_V + " " + TS4 + " " + Ticket_V,
					ticketMap.get("K_C_TGS"));
			System.out.println("K_C_TGS is " + ticketMap.get("K_C_TGS"));
			System.out.println("Send  message  to the client.");
			connection.println(returnMessage);
		}
		}
	}

	public Map<String, String> unpack(String message) {// 将client传得消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "HEAD", "ID_V", "TICKET_TGS", "Authenticator_C" };
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
			tgsDisplay.appenedMessage(key[i] + " : " + strArr[i] + "\n");
		}
		return map;
	}

	public Map<String, String> unpackTicket_TGS(String message) {// 将Ticket_TGS消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "K_C_TGS", "ID_C", "AD_C", "ID_TGS", "TS2", "Lifetime2" };
		tgsDisplay.appenedMessage("EncryptTGT:" + "\n");
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
			tgsDisplay.appenedMessage(key[i] + " : " + strArr[i] + "\n");
		}
		return map;
	}

	public Map<String, String> unpackAuthenticator_C(String message) {// 将Authenticator_C消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "ID_C", "ADC", "TS3" };
		tgsDisplay.appenedMessage("EncryptAuthenticator_C:" + "\n");
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
			tgsDisplay.appenedMessage(key[i] + " : " + strArr[i] + "\n");
		}
		tgsDisplay.appenedMessage("\n\n");
		return map;
	}

}