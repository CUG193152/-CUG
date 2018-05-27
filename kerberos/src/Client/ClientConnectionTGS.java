package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
		System.out.println(ID_V + " " + ticket + " " + Authenticator_C);
		String string = ID_V + " " + ticket + " " + Authenticator_C;
		client.println(string);

		System.out.println("Got the following message from the server:");
		System.out.println(client.readLine());

		// System.out.println("Please type anything and press enter to close the
		// client...");
		// inputScanner.next();
		client.close();
	}

	/**
	 * Authoriticator认证标签生成
	 */
	// Authenticator_C= E(K_C_TGS)[IDc||ADc||TS3]
	public String Authenticator_C(String IDc, String ADc, String TS3, String K_C_TGS) {
		String Authenticator_C = new Des().Encrypt(IDc + " " + ADc + " " + TS3, K_C_TGS);
		return Authenticator_C;
	}
}
