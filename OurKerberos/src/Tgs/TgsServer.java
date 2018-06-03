package Tgs;

import java.io.IOException;
import java.util.Scanner;

import Socket.SocketServer;

public class TgsServer {
	private static Scanner inputScanner = new Scanner(System.in);
	public static void main(String[] args) throws IOException {
		 startServer();
	}
	private static void startServer() {
        System.out.println("Start a server.");
        SocketServer server = new SocketServer(5557, new EchoHandler());

        System.out.println("Please type anything and press enter to close the server...");
        inputScanner.next();
        server.close();
    }
}
