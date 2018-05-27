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

public class ClientConnectionTGS {
	// private static Scanner inputScanner = new Scanner(System.in);
	// public static void main(String[] args) throws IOException {
	// startClient();
	// }
	private SocketClient client;
	private String K_C_TGS;
	private String ticket;
	private String ticket_v;
	private String K_C_V;
	ClientConnectionTGS(SocketClient client, String K_C_TGS, String ticket) throws UnknownHostException {
		this.client = client;
		this.K_C_TGS = K_C_TGS;
		this.ticket = ticket;
		startClient();
	}

	private void startClient() throws UnknownHostException {
		String IDc = "chencong";
		String Adc = InetAddress.getLocalHost().getHostAddress();
		String TS3 = new Tool().getTime();
		// System.out.println("Please type something to send to the server...");
		String ID_V = "V001";
		String Authenticator_C = Authenticator_C(IDc, Adc, TS3, K_C_TGS);
		String Head="000010000";
		System.out.println(Head+" "+ID_V + " " + ticket + " " + Authenticator_C);
		String string =Head +" "+ID_V + " " + ticket + " " + Authenticator_C;
		client.println(string);

		System.out.println("Got the following message from the server:");
		Des des=new Des();
		System.out.println("K_C_TGS is "+K_C_TGS);
		String message=des.Decrypt(client.readLine(), K_C_TGS);
		Map<String,String> map=unpack(message);
		this.ticket_v=map.get("TICKET_V");
		this.K_C_V=map.get("K_C_V");
		// System.out.println("Please type anything and press enter to close the
		// client...");
		// inputScanner.next();
		client.close();
	}

	
	public String getTicket_V() {
		return ticket_v;
	}

	public String getK_C_V() {
		return K_C_V;
	}
	/**
	 * Authoriticator认证标签生成
	 */
	// Authenticator_C= E(K_C_TGS)[IDc||ADc||TS3]

	public String Authenticator_C(String IDc, String ADc, String TS3, String K_C_TGS) {
		String Authenticator_C = new Des().Encrypt(IDc + " " + ADc + " " + TS3, K_C_TGS);
		return Authenticator_C;
	}
	public static Map<String, String> unpack(String message) {// 将client传得消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "HEAD","K_C_V", "ID_V", "TS4", "TICKET_V"};
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
		}
		return map;
	}
	
}
