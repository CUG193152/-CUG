package Client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import Socket.SocketClient;
import Tools.Des;
import Tools.Tool;

public class MyClient {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
        System.out.println("Start a client.");
        SocketClient clientToAs = new SocketClient(InetAddress.getLocalHost(), 5556);
        ClientConnectionAS clientConnectionAS= new ClientConnectionAS(clientToAs);
//		InetAddress.getLocalHost().getHostAddress();获取本机IP
        System.out.println("Start a client.");
        SocketClient clientToTgs = new SocketClient(InetAddress.getLocalHost(), 5557);
		String K_C_TGS=clientConnectionAS.getKEY_C_TGS();
		String ticket=clientConnectionAS.getTicket();
        ClientConnectionTGS clientConnectionTGS= new ClientConnectionTGS(clientToTgs,K_C_TGS,ticket);
//		String IDc="chencong";
//		String Adc=InetAddress.getLocalHost().getHostAddress();
//		String TS3=new Tool().getTime();
//		String K_C_TGS=clientConnectionAS.getKEY_C_TGS();
//		
//		String ID_V="V001";
//		String ticket=clientConnectionAS.getTicket();
//		String Authenticator_C=Authenticator_C(IDc,Adc,TS3,K_C_TGS);
//		System.out.println(ID_V+" "+ticket+" "+Authenticator_C);
		
	}
	/**
	 * Authoriticator认证标签生成
	 */
	//Authenticator_C= E(K_C_TGS)[IDc||ADc||TS3]	
//	public static String Authenticator_C(String IDc,String ADc,String TS3,String K_C_TGS) {
//		String Authenticator_C=new Des().Encrypt(IDc+" "+ADc+" "+TS3, K_C_TGS);		
//		return Authenticator_C;
//	}
	}
