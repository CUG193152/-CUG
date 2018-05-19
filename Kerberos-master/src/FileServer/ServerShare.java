package FileServer;

import java.io.IOException;
import java.net.*;
import java.util.*;


public class ServerShare{
	public static List<Socket> socketList=Collections.synchronizedList(new ArrayList());
	


	public static void main(String[] args) { 
		ServerShareSurface ui =new ServerShareSurface();
			ui.getFileName();
        try {
			ServerSocket ss = new ServerSocket(33456);
			while(true){				
				ui.textarea_2_show("开始监听...");
				Socket s=ss.accept();
				ui.textarea_2_show("有链接");				
				socketList.add(s);				
				new Thread(new ServerShareThread(s,ui)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}  
