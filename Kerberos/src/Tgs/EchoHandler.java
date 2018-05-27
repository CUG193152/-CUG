package Tgs;

import java.util.HashMap;
import java.util.Map;

import Socket.Connection;
import Socket.MessageHandler;
import Tools.Des;
import Tools.Tool;

class EchoHandler implements MessageHandler {
    @Override
    public void onReceive(Connection connection, String message) {
        System.out.println("Got a message from a client:");
        System.out.println(message);
        Map<String,String> map=unpack(message);
        Tool tool=new Tool();
        String K_C_V=tool.KeyCreate(64);
        String ID_V=map.get("ID_V");
        String TS4=tool.getTime();
//        Ticketv = EKV[Kc,v||IDC||ADC|| IDv||TS4||Lifetime4]
        String Ticket_V=new Des().Encrypt(K_C_V, "cccccccc");
        String returnMessage=null;
        System.out.println("Send  message  to the client.");
        connection.println(returnMessage);
    }
    public Map<String, String> unpack(String message) {//将client传得消息分离
  	  String[] strArr = message.split(" ");
  	  Map<String,String> map=new HashMap<String, String>();    
  	  String[] key= {"ID_V","TICKET_TGS","Authenticator_C"};
  	  for(int i=0; i<strArr.length; i++) {
  	   map.put(key[i], strArr[i]);
  	  }
  	  return map;
  }
}