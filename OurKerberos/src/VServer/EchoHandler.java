package VServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import As.AsDisplay;
import As.ConnectionMysql;
import Helper.Des;
import Socket.Connection;
import Socket.MessageHandler;

public class EchoHandler implements MessageHandler {
	public static Map<String,Connection> onlineMap=new HashMap<String,Connection>();
	VDisplay frame = new VDisplay();
	@Override
	public void onReceive(Connection connection, String message) {
		System.out.println("Got a message from a client:");
		System.out.println(message);
		Map<String, String> map0 = unpackHead(message);
		String Head=map0.get("HEAD");
		String Content = map0.get("CONTENT");
		
		if(Head.equals("000011000")) 
		{
			Des des=new Des();
			Map<String, String> map = unpack(Content);
			String TICKET_V = map.get("TICKET_V");
			String Authenticator_C = map.get("Authenticator_C");
			Map<String, String> ticketMap=unpackTicket_V(des.Decrypt(TICKET_V, "cccccccc"));
			Map<String, String> authenticator_cMap=unpackAuthenticator_C(des.Decrypt(Authenticator_C,ticketMap.get("K_C_V")));
			int Lifetime4 = Integer.parseInt(ticketMap.get("Lifetime4"));
			int TS4 = Integer.parseInt(ticketMap.get("TS4"));
			int TS5 = Integer.parseInt(authenticator_cMap.get("TS5"));
			String TicketAD_C = ticketMap.get("AD_C");
			String AuthenticatorAD_C = authenticator_cMap.get("ADC");
			String TicketID_C = ticketMap.get("ID_C");
			String AuthenticatorID_C = authenticator_cMap.get("ID_C");
			if(TicketID_C.equals(AuthenticatorID_C)&&TicketAD_C.equals(AuthenticatorAD_C)&&(TS5-TS4)<=Lifetime4) {
				String TS5_SEND=authenticator_cMap.get("TS5");
				String returnMessage =des.Encrypt("001100000 "+TS5_SEND+1, ticketMap.get("K_C_V")) ;
				System.out.println("K_C_V is "+ticketMap.get("K_C_V"));
				System.out.println("Send  message  to the client.");
				connection.println(returnMessage);
				connection.setK_C_V(ticketMap.get("K_C_V"));
				connection.setTicketID_C(TicketID_C);
				onlineMap.put(AuthenticatorID_C,connection);
				for (Map.Entry<String, Connection> entry : onlineMap.entrySet()) {  
					String key = entry.getKey();  
					Connection value = entry.getValue();  
					System.out.println("key =" + key +"value"+value.toString());  
				}  
			}
		}
		
		if(Head.equals("000011001"))//获取电影信息请求
		{
			System.out.println(Content);
			//通过连接数据库得到座位
			String num = new Des().Decrypt(Content,connection.getK_C_V());
			System.out.println("num:"+num);
			frame.appenedMessage("ID: "+connection.getTicketID_C()+" want NUM:"+num+"\n");
			String seat = new ConnectionMysqlMovies().getSeat(num);
			System.out.println("seat:"+seat);
			seat = new Des().Encrypt(seat, connection.getK_C_V());
			connection.println(seat);
		}
		if(Head.equals("000011010"))//电影座位信息更新
		{
			System.out.println(Content);
			String DeContent = new Des().Decrypt(Content,connection.getK_C_V());
			String[] strArr = DeContent.split(" ",2);
			String num = strArr[0];
			String seat = strArr[1];
			System.out.println("num:"+num);
			System.out.println("seat"+seat);
			frame.appenedMessage("ID: "+connection.getTicketID_C()+" SEATS: "+seat+" NUM:"+num+"\n");
			//更新到数据库，得到反馈
			String sign;
			if(new ConnectionMysqlMovies().updateSeat(num, seat))
			{
				sign = "yes";
			}
			else
			{
				sign = "no";
			}
			sign = new Des().Encrypt(sign, connection.getK_C_V());
			connection.println(sign);
		}
		
		
		if(Head.equals("000011110"))//client退出，更新onlinemap
		{
			System.out.println(Content);
			String userAccount = new Des().Decrypt(Content,connection.getK_C_V()).trim();
			frame.appenedMessage("User: "+userAccount+" cancel "+"\n");
			onlineMap.remove(userAccount);
			for (Map.Entry<String, Connection> entry : onlineMap.entrySet()) {  
				String key = entry.getKey();  
				Connection value = entry.getValue();  
				System.out.println("key =" + key +"value"+value.toString());  
			}  
		}
		
		if(Head.equals("000011011"))//在线用户查看
		{
			synchronized(this){
			
			System.out.println(Content);
			String userAccount = new Des().Decrypt(Content,connection.getK_C_V());
			frame.appenedMessage("User: "+userAccount.trim()+" find online users "+"\n");
			System.out.println("size="+onlineMap.size());
			String onlineusers = "";
			for (Map.Entry<String, Connection> entry : onlineMap.entrySet()) {  
				String key = entry.getKey();  
				Connection value = entry.getValue();  
				System.out.println("key = " + key +" value = "+value.toString());
				onlineusers = onlineusers + " " + key;
				
			}
			frame.appenedMessage("onlineUser: "+onlineusers+"\n");
			connection.println(new Des().Encrypt(onlineusers, connection.getK_C_V()));
			}
		}
		
		if(Head.equals("000011100"))//通信
		{
			System.out.println(Content);
			String IDandMovie = new Des().Decrypt(Content,connection.getK_C_V());
			String[] strArr = IDandMovie.split(" ");
			String ID = strArr[0];
			String movie = strArr[1];
		}
		
		
		
	}

	public static Map<String, Connection> getOnlineMap() {
		return onlineMap;
	}

	public static void setOnlineMap(Map<String, Connection> onlineMap) {
		EchoHandler.onlineMap = onlineMap;
	}

	
	public Map<String, String> unpackHead(String message) {// 将client传得消息分离HEAD\CONTENT
		String[] strArr = message.split(" ",2);
		Map<String, String> map = new HashMap<String, String>();
		String[] key = {"HEAD","CONTENT" };
		for (int i = 0; i < strArr.length; i++) {
			frame.appenedMessage(key[i]+" "+strArr[i]+"\n");
			map.put(key[i], strArr[i]);
		}
		return map;
	}

	public Map<String, String> unpack(String message) {// 将client传得消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = {"TICKET_V", "Authenticator_C" };
		for (int i = 0; i < strArr.length; i++) {
			frame.appenedMessage(key[i]+" "+strArr[i]+"\n");
			map.put(key[i], strArr[i]);
		}
		return map;
	}
	public Map<String, String> unpackTicket_V(String message) {// 将Ticket_V消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "K_C_V", "ID_C", "AD_C","ID_V","TS4","Lifetime4"};
		frame.appenedMessage("EncryptedTicket_V:"+"\n");
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
			frame.appenedMessage(key[i]+" "+strArr[i]+"\n");
		}
		return map;
	}
	public Map<String, String> unpackAuthenticator_C(String message){ // 将Authenticator_C消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "ID_C", "ADC","TS5"};
		frame.appenedMessage("EncryptedAuthenticator_C:"+"\n");
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
			frame.appenedMessage(key[i]+" "+strArr[i]+"\n");
		}
		frame.appenedMessage("\n\n");
		return map;
	}
}