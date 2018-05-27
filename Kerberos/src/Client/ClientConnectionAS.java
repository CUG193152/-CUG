package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import Socket.SocketClient;
import Tools.Des;
import Tools.Tool;

public class ClientConnectionAS {
	// private static Scanner inputScanner = new Scanner(System.in);
	// public static void main(String[] args) throws IOException {
	// startClient();
	// }
	private String ticket;
	private String KEY_C_TGS;
	private SocketClient client;

	ClientConnectionAS(SocketClient client) {
		this.client = client;
		try {
			startClient();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private static void startClient() throws UnknownHostException {
	private void startClient() throws UnknownHostException {

//		System.out.println("Send time to the server...");
		String string = "000001000 " + "chencong " + "T001 " + new Tool().getTime();// 首部+C→AS:IDC ||IDtgs||TS1
		client.println(string);

		System.out.println("Got the following message from the server:");
		// System.out.println(client.readLine());
		String Decrypt_News = new Des().Decrypt(client.readLine(), "123123zz");
		System.out.println(Decrypt_News);
		// AS → C : EKC[ Kc,tgs || IDtgs || TS2 || Lifetime2 || Tickettgs ]
		Map<String, String> map = unpack(Decrypt_News);
		ticket = map.get("TICKET_TGS");
		KEY_C_TGS = map.get("KEY_C_TGS");
		// System.out.println("Please type anything and press enter to close the
		// client...");
		// inputScanner.next();
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

}
