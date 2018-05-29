package Client;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import Helper.Des;
import Helper.Tool;
import Socket.SocketClient;

public class ClientConnectionAS {
	private String ticket;
	private String KEY_C_TGS;
	private SocketClient client;
	private String Head;
	ClientConnectionAS(SocketClient client) {
		this.client = client;
		try {
			startClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	private void startClient() throws UnknownHostException {

		String string = "000001000 " + "chencong " + "T001 " + new Tool().getTime();// 首部+C→AS:IDC ||IDtgs||TS1
		client.println(string);

		System.out.println("Got the following message from the server:");
		String Decrypt_News = new Des().Decrypt(client.readLine(), "123123zz");
		System.out.println(Decrypt_News);
		// AS → C : EKC[ Kc,tgs || IDtgs || TS2 || Lifetime2 || Tickettgs ]
		Map<String, String> map = unpack(Decrypt_News);
		Head=map.get("HEAD");
		ticket = map.get("TICKET_TGS");
		KEY_C_TGS = map.get("KEY_C_TGS");
		client.close();
	}

	public static Map<String, String> unpack(String message) {// 将client传得消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "HEAD", "KEY_C_TGS", "ID_TGS", "TS2", "LIFETIME2", "TICKET_TGS" };
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
		}
		return map;
	}

	public String getTicket() {
		return ticket;
	}

	public String getKEY_C_TGS() {
		return KEY_C_TGS;
	}

	public String getHead() {
		return Head;
	}

}
