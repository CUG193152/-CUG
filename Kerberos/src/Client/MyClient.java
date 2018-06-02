package Client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import Socket.SocketClient;

public class MyClient {
	private Boolean flag=false;
//	public static void main(String[] args) throws UnknownHostException {
	public MyClient(String userAccount,String userPassword){
		try {
        System.out.println("Start a AS client.");
        SocketClient clientToAs = new SocketClient(InetAddress.getLocalHost(), 5556);
        ClientConnectionAS clientConnectionAS= new ClientConnectionAS(clientToAs,userAccount,userPassword);
        String Head=clientConnectionAS.getHead();
        if(Head.equals("000100000")) {
        String K_C_TGS=clientConnectionAS.getKEY_C_TGS();
		String Ticket_tgs=clientConnectionAS.getTicket();
        System.out.println("Start a TGS client.");
        SocketClient clientToTgs = new SocketClient(InetAddress.getLocalHost(), 5557);
        ClientConnectionTGS clientConnectionTGS= new ClientConnectionTGS(clientToTgs,K_C_TGS,Ticket_tgs,userAccount);
        if(clientConnectionTGS.getHead().equals("001000000")) {
				String K_C_V = clientConnectionTGS.getK_C_V();
				String Ticket_V = clientConnectionTGS.getTicket_V();
		        SocketClient clientToV = new SocketClient(InetAddress.getLocalHost(), 5558);
		        System.out.println("Start a V client.");
//		        ClientConnectionV clientConnectionV= new ClientConnectionV(clientToV,K_C_V,Ticket_V);
		        ClientConnectionV clientConnectionV=new ClientConnectionV(clientToV,K_C_V,Ticket_V,userAccount);
		        flag=true;
        }
        }}catch(Exception e) {
        	e.printStackTrace();
        	flag=false;
        }
//	}
        
	
	}
	public Boolean getFlag() {
		return flag;
	}
}