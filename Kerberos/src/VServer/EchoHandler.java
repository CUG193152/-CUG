package VServer;

import Socket.Connection;
import Socket.MessageHandler;

class EchoHandler implements MessageHandler {
    @Override
    public void onReceive(Connection connection, String message) {
        System.out.println("Got a message from a client:");
        System.out.println(message);
        System.out.println("Send back the same message back to the client.");
        connection.println(message);
    }
}