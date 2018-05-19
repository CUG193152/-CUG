package AS;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import Tools.*;

public class ASServerThread implements Runnable{
	public Tool tool=new Tool();
		String ID_C = "";
		String ID_TGS = "";
		String KEY_C_TGS = "";
		String AD_C = "";
		String KEY_C = "00000000";
		String KEY_TGS = "";
		String Prelude = "";
		String TS_2 = "";
		String TS_1 = "";
		String Message_2 = "";
		//定义当前线程所处理的Socket
		Socket s = null;
		//该线程处理的Socket对应的输入流
		BufferedReader br = null;
		ASSurface d = null;
		Connection c = null;
		//监听端口，将接收的线程保存在链表中
		//负责处理每个线程通信的线程类
		public ASServerThread(Socket s,ASSurface d,Connection c) throws IOException{
				this.s = s;
				this.d = d;
				this.c = c;
				//初始化该socket对应的输入流
				br = new BufferedReader(new InputStreamReader(s.getInputStream(),"utf-8"));
			}	
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String content = null;			
			while((content = readFromClient()) != null){
				//System.out.println(content);
				
				d.demo(content);

				try {
					PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream(),"utf-8")),true);
					if(content.length()!=28){							
						d.demo("客户端发送消息的长度有误");
						System.out.println("客户端发送消息的长度有误");
						out.println("0100010000"+new Des().Encrypt("Length Error", KEY_C));
						out.flush();
						s.close();
					}
					else{
						uncontent(content);						
						if(judge_ID_C(c)!=1){	
							d.demo("没有找到该客户端");
							System.out.println("没有找到该客户端");
							out.println("0100010000"+new Des().Encrypt("ID_C Error", KEY_C));
							out.flush();
							s.close();
						}
						if(judge_ID_TGS(c)!=1){
							d.demo("没有找到客户端想要访问的TGS");
							System.out.println("没有找到客户端想要访问的TGS");
							out.println("0100010000"+new Des().Encrypt("ID_TGS Error", KEY_C));
							out.flush();
							s.close();
						}
						if((judge_ID_TGS(c)==1)&&judge_ID_C(c)==1){	
							System.out.println(ID_C+"->AS:"+content);
							out.println(creat_package());
							
							d.demo("收到"+ID_C+"向"+ID_TGS+"发送的请求消息，时间戳为"+TS_1);
							d.demo("返回的票据时间为"+TS_2);
							d.demo("发送成功！");
							out.flush();
							s.close();
						}
						
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
			
		}
		
		private String readFromClient() {
			// TODO Auto-generated method stub
			try{
				return br.readLine();
			}
			catch(IOException e){
				ASServer.socketlist.remove(s);
			}
			return null;
		}
	
		//收到的包每部分规定
		public LinkedHashMap Unpack(String message){
			LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
			if(message.length()>=10){
				if(message.substring(4, 6).equals("01")){
					//System.out.println("该包为错误包！");
					String str = "该包为错误包！";
					d.demo(str);
				}
				else{
					String symbol=message.substring(0, 4);
					switch(symbol){
					case "0001":
						map.put("Prelude", 10);
						map.put("ID_C",4);
						map.put("ID_TGS",4);
						map.put("TS_1",10);
						break;
					case "0100":
						map.put("Prelude", 10);
						map.put("KEY_C_TGS",8);
						map.put("ID_TGS",4);
						map.put("TS_2",10);
						map.put("LifeTime_2",4);
						map.put("Ticket_TGS",384);
						break;
					}
				}
			}
			else{
				//System.out.println("解析包过程中出错，收到的字符串长度小于10");
				String str = "解析包过程中出错，收到的字符串长度小于10";
				d.demo(str);
			}		
			return map;
		}

		//连接数据库
					
		//判断数据库中是否有ID_C
			public int judge_ID_C(Connection connection) throws SQLException{
				int a = 0;
				Statement statement = connection.createStatement();
				String sql_c = "select * from client";
				ResultSet rs_c = statement.executeQuery(sql_c);			
				String c_id = "";
				while(rs_c.next()){
					c_id = rs_c.getString("c_id");
					if(c_id.equals(ID_C)){
						KEY_C = rs_c.getString("key_c");
						a =1;
					}
				}
					rs_c.close();
					return a;
			}				
			
			public int judge_ID_TGS(Connection connection) throws SQLException{
				int b = 0;
				Statement statement = connection.createStatement();
				String sql_tgs = "select * from tgs";
				ResultSet rs_tgs = statement.executeQuery(sql_tgs);	
				String tgs_id = "";
				while(rs_tgs.next()){
					tgs_id = rs_tgs.getString("tgs_id");
					if(tgs_id.equals(ID_TGS)){
					KEY_TGS = rs_tgs.getString("key_tgs");
						b =1;
					}
				}
				rs_tgs.close();
				return b;					
			}
						
		
		//拆包，并C发给AS的包的每部分分别保存
		public void uncontent(String content){
			TS_2 = tool.getTime();			
			LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
			map = Unpack(content);
			for(Entry<String, Integer> entry : map.entrySet()){
				if(entry.getKey() == "Prelude"){
					Prelude = content.substring(0, entry.getValue());
					content = content.substring(entry.getValue());
				}
				if(entry.getKey() == "ID_C"){
					ID_C = content.substring(0, entry.getValue());
					content = content.substring(entry.getValue());
			}
				if(entry.getKey() == "ID_TGS"){
					ID_TGS = content.substring(0, entry.getValue());
					content = content.substring(entry.getValue());
			}
				if(entry.getKey() == "TS_1"){
					TS_1 = content.substring(0, entry.getValue());
					content = content.substring(entry.getValue());
			}
		}
	}
	
		//构建AS发给Client的包
		public String creat_package(){
			int a = Integer.parseInt(TS_2);
			int b = Integer.parseInt(TS_1);
			String Message_2_temp = "";
			String Ticket_TGS = "";
			String Ticket_temp = "";
			String LifeTime_2 = "2359";
			KEY_C_TGS = tool.CreateKey();
			AD_C=tool.getIP(s);
			//System.out.println(AD_C);
			Ticket_temp = Ticket_temp + KEY_C_TGS + ID_C + AD_C + ID_TGS + TS_2 + LifeTime_2;
			Des d = new Des();
			Ticket_TGS = d.Encrypt(Ticket_temp, KEY_TGS);	
			Message_2_temp = Message_2_temp + KEY_C_TGS + ID_TGS + TS_2 + LifeTime_2 + Ticket_TGS;
			Message_2 = d.Encrypt(Message_2_temp, KEY_C);
			Message_2 = "0100000000"+Message_2;
			System.out.println("AS->"+ID_C+":"+Message_2);
			
			if((a-b)>10000){
				Message_2 = "0100010000"+"Error";
				System.out.println("传输超时");
			}
			return Message_2;
		}
		
		}

