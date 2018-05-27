package As;

import java.net.Socket;
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
        String K_C_TGS=new Tool().KeyCreate(64);
        Map<String, String> map=unpack(message);
        String Head=map.get("HEAD");
        String ID_C=map.get("USERACCOUNT");
        String ID_TGS=map.get("TGS_NAME");
        String TS1=map.get("TIME");
        String AD_C=getIP(connection.getSocket());
        String TS2=new Tool().getTime();
        String Lifetime="1000";
        String Ticket_TGS=new Des().Encrypt(K_C_TGS+" "+ID_C+" "+AD_C+" "+ID_TGS+" "+TS2+" "+Lifetime, "cccccccc") ;
        String K_C=new ConnectionMysql().getPassword(ID_C);
        String returnMessage=new Des().Encrypt("000100000 "+K_C_TGS+" "+ID_TGS+" "+TS2+" "+Lifetime+" "+Ticket_TGS, K_C);
        System.out.println(new ConnectionMysql().userAccountExsits(ID_C));//检查用户名在数据库中是否存在
        System.out.println(new ConnectionMysql().getPassword(ID_C));//检查用户名在数据库中对应的密码
        System.out.println(Head);
        System.out.println(ID_C);
        System.out.println(ID_TGS);
        System.out.println(TS1);
        System.out.println("Send back the message back to the client.");
        connection.println(returnMessage);
        
    }
    public Map<String, String> unpack(String message) {//将client传得消息分离
    	  String[] strArr = message.split(" ");
    	  Map<String,String> map=new HashMap<String, String>();    
    	  String[] key= {"HEAD","USERACCOUNT","TGS_NAME","TIME"};
    	  for(int i=0; i<strArr.length; i++) {
    	   map.put(key[i], strArr[i]);
    	  }
    	  return map;
    }
    public Boolean verifyHead(String HEAD) {//检查client发送的包头是否合格
    	return true;
    }
    public Boolean verifyUserAccount(String userAccount) {//检查用户名在数据库中是否存在
    	return true;
    }
    public Boolean verifyTGS_NAME(String TGS_NAME) {//检查TGS_NAME是否正确
    	return true;
    }
  	public String getIP(Socket s){
  		String ip = null;
  		String temp="";
  		ip = s.getInetAddress().getHostAddress();
  		String[] te = ip.split("\\.");
  		for(int i = 0; i < te.length;i++){
  			for(int j = 0; j < 3-te[i].length(); j++){
  				temp+="0";
  			}
  			temp+=te[i];
  		}
  		return temp;
  	}
}