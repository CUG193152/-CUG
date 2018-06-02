package As;

import java.util.HashMap;
import java.util.Map;

import Helper.Des;
import Helper.Tool;
import Socket.Connection;
import Socket.MessageHandler;

class EchoHandler implements MessageHandler {
	AsDisplay frame = new AsDisplay();
	@Override
	public void onReceive(Connection connection, String message) {
		System.out.println("Got a message from a client:");
		System.out.println(message);
		frame.appenedMessage(message+"\n");
		Map<String, String> map = unpack(message);
		String Head = map.get("HEAD");
		String TS1 = map.get("TIME");
		String TS2 = new Tool().getTime();
		if (Head.equals("000001000")&&(Integer.parseInt(TS2)-Integer.parseInt(TS1)<=200)) {
			String ID_C = map.get("USERACCOUNT");
			if (new ConnectionMysql().userAccountExsits(ID_C).equals(true)) {// 检查用户名在数据库中是否存在
				String K_C_TGS = new Tool().KeyCreate(64);
				String ID_TGS = map.get("TGS_NAME");
				String AD_C = connection.getSocket().getInetAddress().getHostAddress();;
				String Lifetime = "300";
				String Ticket_TGS = new Des().Encrypt(
						K_C_TGS + " " + ID_C + " " + AD_C + " " + ID_TGS + " " + TS2 + " " + Lifetime, "cccccccc");
				String K_C = new ConnectionMysql().getPassword(ID_C);// 获取用户名在数据库中对应的密码
				String returnMessage = new Des().Encrypt(
						"000100000 " + K_C_TGS + " " + ID_TGS + " " + TS2 + " " + Lifetime + " " + Ticket_TGS, K_C);
				System.out.println("Send back the message back to the client.");
				connection.println(returnMessage);
			}else {
				connection.println("");
			}
		}

	}

	public Map<String, String> unpack(String message) {// 将client传得消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "HEAD", "USERACCOUNT", "TGS_NAME", "TIME" };
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
		}
		return map;
	}
}