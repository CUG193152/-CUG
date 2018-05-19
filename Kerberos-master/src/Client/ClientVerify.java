package Client;


import java.io.*;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.*;

import Protocol.Protocol;
import Tools.*;

public class ClientVerify {

	public Tool tool=new Tool();
	public Protocol pro=new Protocol();
	public static String Other = null; // 判断，若Other为null，则返回正确，继续执行
	private Socket socket = null;
	private Socket socket1 = null;
	public BufferedWriter bw = null;
	public BufferedReader br = null;

	private String ID_TGS = "";
	private String TS_2 = "";
	private String TS_3 = "";
	private String TS_4 = "";
	private String TS_5 = "";
	private String TS_6 = "";
	private String LifeTime_2 = "";
	private JFrame frame = null;

	private String IP_AS = "192.168.49.233";
	private String IP_TGS = "192.168.49.48";
	private String IP_V = "192.168.49.93";
	private String IP_V_1 = "192.168.49.233";
	private int PORT_AS = 45678;
	private int PORT_TGS = 45679;
	private int PORT_V = 45680;
	private int PORT_V_1 = 33456;

	private String Ticket_TGS = ""; // 从AS获得的票据
	private String Ticket_V = ""; // 从TGS获得的票据

	private String Key_C_TGS = ""; // Client与TGS之间的密钥
	public String KEY_C_V = ""; // Client与V之间的密钥

	private String ID = null;
	private String ID_T = "T001";
	private String ID_V = null;

	private static String Keys_C = "00000003";

	ClientVerify(JFrame frame,String ID, String ID_V){
		this.frame = frame;
		this.ID = ID;
		this.ID_V = ID_V;
	}

	/**
	 * Client向AS和TGS发送的通信信息
	 *
	 */
	public String getMessage(String Type) {
		String message = "";
		if (Type.equals("AS")) {
			message += "0001000000" + ID + ID_T + tool.getTime();
		} else if (Type.equals("TGS")) {
			message += "0010000000" + ID_V + Ticket_TGS + Authenticator(Type);
		} else if (Type.equals("V")) {
			message += "0011000000" + Ticket_V + Authenticator(Type);
		}

		return message;
	}
	
	public String getIP(){
		String ip = null;
		String temp="";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] te = ip.split("\\.");
		for(int i = 0; i < te.length;i++){
			for(int j = 0; j < 3-te[i].length(); j++){
				temp+="0";
			}
			temp+=te[i];
		}
		return temp;
	}


	/**
	 * Authoriticator认证标签生成
	 */
	public String Authenticator(String Type) {
		String ret = "";
		Des d = new Des();
		if (Type.equals("TGS")) {
			TS_3 = tool.getTime();
			ret += d.Encrypt(ID + getIP() + TS_3, Key_C_TGS);
		} else if (Type.equals("V")) {
			TS_5 = tool.getTime();
			ret += d.Encrypt(ID + getIP() + TS_5, KEY_C_V);
		}
		return ret;
	}

	/**
	 * 与AS服务器或TGS服务器连接
	 * 
	 */
	public void ATS(String Type) {
		try {
			socket = null;
			String message = null;
			String Key = null;
			if (Type.equals("AS")) {
				socket = new Socket(IP_AS, PORT_AS);
				Key = Keys_C;
			} else if (Type.equals("TGS")) {
				socket = new Socket(IP_TGS, PORT_TGS);
				Key = Key_C_TGS;
			}
			message = getMessage(Type);
			//message = "1";
			//System.out.println(message);
			if (Type.equals("TGS")) {
				int ts_2 = Integer.valueOf(TS_2);
				int ts_3 = Integer.valueOf(TS_3);
				int lifetime_2 = Integer.valueOf(LifeTime_2);
				if (ts_3 > ts_2 + lifetime_2)
					System.out.println("Ticket TGS过期");
			}
			if (socket != null) {
				bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
				bw.write(message);
				bw.flush();
				System.out.println(ID+"->"+Type + ":" + message);
				socket.shutdownOutput();
				String str = "";
				String str1 = null;
				int j = 0;
				while ((str1 = br.readLine()) != null) {
					if (j != 0) {
						str += "\n";
					}
					str += str1;
					j++;
				}
				socket.close();
				System.out.println(Type+"->"+ID+":"+str);
				LinkedHashMap<String, Integer> lhm = (LinkedHashMap<String, Integer>) pro.Unpack(str);
				int size = lhm.size();
				String[] temp = new String[size];
				int[] temp2 = new int[size];
				int i = 0;
				for (Entry<String, Integer> entry : lhm.entrySet()) {
					temp[i] = entry.getKey();
					temp2[i] = entry.getValue();
					i++;
				}
				String message2 = str.substring(temp2[0]);

				Des d = new Des();
				String dec = d.Decrypt(message2, Key);
				System.out.println("解包后："+dec);
				String id_tgs = null;
				String id_v = null;
				int t = 0;
				for (i = 1; i < size; i++) {
					switch (temp[i]) {
					case "KEY_C_TGS":
						Key_C_TGS = dec.substring(t, t + temp2[i]);
						t += temp2[i];

						break;
					case "ID_TGS":
						id_tgs = dec.substring(t, t + temp2[i]);
						t += temp2[i];

						break;
					case "TS_2":
						TS_2 = dec.substring(t, t + temp2[i]);
						t += temp2[i];
						break;
					case "LifeTime_2":
						LifeTime_2 = dec.substring(t, t + temp2[i]);
						t += temp2[i];
						break;
					case "Ticket_TGS":
						Ticket_TGS = dec.substring(t, t + temp2[i]);
						break;
					case "Ticket_V":
						Ticket_V = dec.substring(t, t + temp2[i]);
						t += temp2[i];
						break;
					case "KEY_C_V":
						KEY_C_V = dec.substring(t, t + temp2[i]);
						t += temp2[i];
						break;
					case "ID_V":
						id_v = dec.substring(t, t + temp2[i]);
						t += temp2[i];
						break;
					case "TS_4":
						TS_4 = dec.substring(t, t + temp2[i]);
						t += temp2[i];
						break;
					case "ERROR":
						Other = dec;
						break;
					}
				}
				br.close();
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printMessage(PrintWriter pw, String message) {
		pw.println(message);
		pw.flush();
	}

	/**
	 * 与服务器V相连
	 */
	public void conV(String ip,int port) {
		try {
			socket1 = null;
			String message = null;
			System.out.println(ip + " " + port);
			socket1 = new Socket(ip, port);
			if (socket1 != null) {
				message = getMessage("V");
				br = new BufferedReader(new InputStreamReader(socket1.getInputStream(), "utf-8"));

				PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket1.getOutputStream(), "utf-8"));
				printMessage(writer, message);
				
				System.out.println(ID+"->V:"+message);
				
				String str = "";
				str = br.readLine();
				LinkedHashMap<String, Integer> lhm = (LinkedHashMap<String, Integer>) pro.Unpack(str);
				int size = lhm.size();
				String[] temp = new String[size];
				int[] temp2 = new int[size];
				int i = 0;
				for (Entry<String, Integer> entry : lhm.entrySet()) {

					temp[i] = entry.getKey();
					temp2[i] = entry.getValue();
					i++;
				}
				System.out.println("V->"+ID+":"+str);
				String message2 = str.substring(temp2[0]);
				Des d = new Des();
				String dec = d.Decrypt(message2, KEY_C_V);
				for (i = 1; i < size; i++) {
					switch (temp[i]) {
					case "TS_6":
						TS_6 = dec;
						break;
					case "ERROR":
						Other = dec;
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void newS(ClientVerify my) {

		my.ATS("AS");
		if (Other != null) {
			System.out.println(Other);
			JOptionPane.showMessageDialog(frame, Other, "错误",  
                    JOptionPane.ERROR_MESSAGE);
			return;
		}
		Other = null;

		my.ATS("TGS");
		if(Other != null){
			System.out.println(Other);
			JOptionPane.showMessageDialog(frame, Other, "错误",  
                    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		my.conV(IP_V,PORT_V);
		if (Other != null) {
			JOptionPane.showMessageDialog(frame, Other, "错误",  
                    JOptionPane.ERROR_MESSAGE);
			System.out.println(Other);

			return;
		}
		//frame.dispose();
		TS_6=tool.MoveSpace(TS_6);
		
		if(Integer.valueOf(TS_6)-Integer.valueOf(TS_5)==1){
			ClientChat c = new ClientChat(socket1,ID,ID_V);
		}

	}
	
	public void newSV2(ClientVerify my) throws IOException {

		my.ATS("AS");
		if (Other != null) {
			System.out.println(Other);
			JOptionPane.showMessageDialog(frame, Other, "错误",  
                    JOptionPane.ERROR_MESSAGE);
			return;
		}
		Other = null;

		my.ATS("TGS");
		if(Other != null){
			System.out.println(Other);
			JOptionPane.showMessageDialog(frame, Other, "错误",  
                    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		my.conV(IP_V_1,PORT_V_1);
		if (Other != null) {
			JOptionPane.showMessageDialog(frame, Other, "错误",  
                    JOptionPane.ERROR_MESSAGE);
			System.out.println(Other);

			return;
		}
		TS_6=tool.MoveSpace(TS_6);
		
		if(Integer.valueOf(TS_6)-Integer.valueOf(TS_5)==1){
			ClientShare c = new ClientShare(socket1,KEY_C_V);
		}
	}
}

