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
        System.out.println("Start a client.");
        SocketClient clientToTgs = new SocketClient(InetAddress.getLocalHost(), 5557);
		String K_C_TGS=clientConnectionAS.getKEY_C_TGS();
		String Ticket_tgs=clientConnectionAS.getTicket();
        ClientConnectionTGS clientConnectionTGS= new ClientConnectionTGS(clientToTgs,K_C_TGS,Ticket_tgs);
        SocketClient clientToV = new SocketClient(InetAddress.getLocalHost(), 5558);
		String K_C_V=clientConnectionTGS.getK_C_V();
		String Ticket_V=clientConnectionTGS.getTicket_V();
        ClientConnectionV clientConnectionV= new ClientConnectionV(clientToV,K_C_V,Ticket_V);
	}
	
	}