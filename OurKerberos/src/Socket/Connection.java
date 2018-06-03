package Socket;

import java.net.Socket;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Connection {
    private Socket socket;
    private String K_C_V;
    private String TicketID_C;
	public Connection(Socket socket) {
        this.socket = socket;
    }

    public void println(String message) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(new OutputStreamWriter(
                                     socket.getOutputStream()), true);
            writer.println(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
	public Socket getSocket() {
		return socket;
	}
	
	public void setK_C_V(String K_C_V) {
		this.K_C_V = K_C_V;
	}
	
	public void setTicketID_C(String TicketID_C) {
		this.TicketID_C = TicketID_C;
	}
	

	public String getK_C_V() {
		return K_C_V;
	}
	
	public String getTicketID_C() {
		return TicketID_C;
	}
}