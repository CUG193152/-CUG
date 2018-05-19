package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import Socket.SocketClient;

public class ClientConnectionAS {
		private static Scanner inputScanner = new Scanner(System.in);
		public static void main(String[] args) throws IOException {
			 startClient();
		}
	private static void startClient() throws UnknownHostException {
        System.out.println("Start a client.");
        SocketClient client = new SocketClient(InetAddress.getLocalHost(), 5556);

        System.out.println("Please type something to send to the server...");
        String string = inputScanner.next();
        client.println(string);

        System.out.println("Got the following message from the server:");
        System.out.println(client.readLine());

        System.out.println("Please type anything and press enter to close the client...");
        inputScanner.next();
        client.close();
    }
}
