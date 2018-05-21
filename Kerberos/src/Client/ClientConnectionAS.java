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
		private static Scanner inputScanner = new Scanner(System.in);
		public static void main(String[] args) throws IOException {
			 startClient();
		}
	private static void startClient() throws UnknownHostException {
        System.out.println("Start a client.");
        SocketClient client = new SocketClient(InetAddress.getLocalHost(), 5556);

        System.out.println("Send time to the server...");
        String string ="000001000&"+"chencong&"+"T001&"+ new Tool().getTime();//首部+C→AS:IDC ||IDtgs||TS1
        client.println(string);

        System.out.println("Got the following message from the server:");
//        System.out.println(client.readLine());
        String Decrypt_News=new Des().Decrypt(client.readLine(), "123123zz");
        System.out.println(Decrypt_News);
        Map<String, String> map=unpack(Decrypt_News);
        System.out.println("Please type anything and press enter to close the client...");
        inputScanner.next();
        client.close();
    }
	public static Map<String, String> unpack(String message) {//将client传得消息分离
  	  String[] strArr = message.split("&");
  	  Map<String,String> map=new HashMap<String, String>();    
  	  String[] key= {"HEAD","KEY_C_TGS","ID_TGS","TS2","LIFETIME2","TICKET_TGS"};
  	  for(int i=0; i<strArr.length; i++) {
  	   map.put(key[i], strArr[i]);
  	  }
  	  return map;
  }
}
