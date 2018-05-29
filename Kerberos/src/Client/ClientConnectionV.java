package Client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import Helper.Des;
import Helper.Tool;
import Socket.SocketClient;

public class ClientConnectionV {
	private SocketClient client;
	private String K_C_V;
	private String Ticket_V;

	ClientConnectionV(SocketClient client, String K_C_V, String Ticket_V) throws UnknownHostException {
		this.client = client;
		this.K_C_V = K_C_V;
		this.Ticket_V = Ticket_V;
		startClient();
	}

	private void startClient() throws UnknownHostException {

		String IDc = "chencong";
		String Adc = InetAddress.getLocalHost().getHostAddress();
		String TS5 = new Tool().getTime();
		String Authenticator_C = Authenticator_C(IDc, Adc, TS5, K_C_V);
		System.out.println(Ticket_V + " " + Authenticator_C);
		String string ="000011000 "+Ticket_V + " " + Authenticator_C;
		client.println(string);
		System.out.println("Got the following message from the server:");
		String message=client.readLine();
		message=new Des().Decrypt(message, K_C_V);
		System.out.println(message);
		if(message.equals(TS5+1)) {//用户认证服务器成功
			
		}
		client.close();
	}
    
	/**
	 * Authenticator认证标签生成
	 */
	public String Authenticator_C(String IDc, String ADc, String TS5, String K_C_V) {
		String Authenticator_C = new Des().Encrypt(IDc + " " + ADc + " " + TS5, K_C_V);
		return Authenticator_C;
	}
}