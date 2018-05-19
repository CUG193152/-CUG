package TGS;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import Protocol.Protocol;
import Tools.*;

public class TGSServerThread implements Runnable{
	
	//全局变量
	public Tool tool=new Tool();
	public Protocol pro=new Protocol();
	public String ID_TGS_1="";
	public String AD_C_1="";
	public String ID_C_1="";
	public String Prelude = "";
	public String ID_V = "";
	public String Ticket_TGS = "";
	public String Authenticator_C = "";
	public String Ticket_V = "";
	public String KEY_C_TGS = "";
	public String KEY_C_V = "";
	public String KEY_TGS = "";
	public String KEY_V = "";
	public String ID_C = "";
	public String AD_C = "";
	public String ID_TGS = "";
	public String TS_2 = "";
	public String TS_3 = "";
	public String TS_4 = "";
	public String LifeTime_2 = "";
	public String LifeTime_4 = "2400";
	public String ERROR = null;
	//定义当前线程所处理的Socket
	Socket s = null;
	//某线程处理的Socket对应的输入流
	BufferedReader br = null;
	BufferedWriter bw = null;
	TGSSurface d = null;
	Connection conn = null;
	
	//监听端口，将接收的线程保存在链表中
	//负责处理每个线程通信的线程类
	public TGSServerThread(Socket s,TGSSurface d,Connection conn) throws IOException{
			this.s = s;
			this.d = d;
			this.conn = conn;
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		}

	@Override
	public void run() {
		String str = "";
		String str1 = null;
		try {
			if (s != null) {
				bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "utf-8"));
				br = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
				int j = 0;
				while((str1=br.readLine())!=null){
					if(j != 0){
						str+="\n";
					}
					str+=str1;
					j++;
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(str);
		//System.out.println("ID"+ID_V);//显示ID_V
		PullPack(str);
		
		try {
			int n = getKEY_V(conn);
			//System.out.println(n);
			String Dec_Ticket_TGS = Analyse_Ticket_TGS();//解密Ticket_TGS
			String temp = CreateTicket_v(Dec_Ticket_TGS);//创建Ticket_v
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream(),"utf-8")),true);
			if(n!=1){
				out.println("1000010000"+new Des().Encrypt("can't find key_v", KEY_C_TGS));
				out.flush();
				s.close();
			}
			else{
				out.println(temp);
				out.flush();			
				s.close();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	//分解C->TGS的包 获取map.key map.value 按位取字符串 
	public void PullPack(String message){
		LinkedHashMap<String, Integer> map = pro.Unpack(message);
		for(Entry<String, Integer> entry : map.entrySet()){
			if(entry.getKey() == "Prelude"){
				Prelude = message.substring(0, entry.getValue());
				message = message.substring(entry.getValue());
			}
			if(entry.getKey() == "ID_V"){
				ID_V = message.substring(0, entry.getValue());
				message = message.substring(entry.getValue());
			}
			if(entry.getKey() == "Ticket_TGS"){
				Ticket_TGS = message.substring(0, entry.getValue());
				System.out.println("Ticket_TGS: "+Ticket_TGS);
				message = message.substring(entry.getValue());
			}
			if(entry.getKey() == "Authenticator_C"){
				Authenticator_C = message;
				System.out.println("Authenticator_C: "+Authenticator_C);
			}
		}
	}
	
	//创建Ticket_V
	public String CreateTicket_v(String message){
		LinkedHashMap<String, Integer> map = pro.TICKET_TGS();
		String Message="";
		for(Entry<String, Integer> entry : map.entrySet()){
			if(entry.getKey() == "KEY_C_TGS"){
				KEY_C_TGS = message.substring(0, entry.getValue());
				//System.out.println("KEY_C_TGS"+KEY_C_TGS);
				message = message.substring(entry.getValue());
			}
			if(entry.getKey() == "ID_C"){
				ID_C_1 = message.substring(0, entry.getValue());
				//System.out.println("ID_C_1"+ID_C_1);
				message = message.substring(entry.getValue());
			}
			if(entry.getKey() == "AD_C"){
				AD_C_1 = message.substring(0, entry.getValue());
				//System.out.println("AD_C_1"+AD_C_1);
				message = message.substring(entry.getValue());
			}
			if(entry.getKey() == "ID_TGS"){
				ID_TGS_1 = message.substring(0, entry.getValue());
				//System.out.println("ID_TGS_1"+ID_TGS_1);
				message = message.substring(entry.getValue());
			}
			if(entry.getKey() == "TS_2"){
				TS_2 = message.substring(0, entry.getValue());
				//System.out.println("TS_2"+TS_2);
				message = message.substring(entry.getValue());
			}
			if(entry.getKey() == "LifeTime_2"){
				LifeTime_2 = message.substring(0, entry.getValue());
				//System.out.println("LifeTime_2"+LifeTime_2);
			}
		}
		
		TS_4 = tool.getTime();
		Analyse_Authenticator_C();//分析Authenticator_C
		if(Verify()){
			KEY_C_V = tool.CreateKey();
			//System.out.println("KEY_C_V:   "+KEY_C_V);
			//System.out.println("ID_V:   "+ID_V);
			//System.out.println("票据Ticket_V生成时间戳为："+TS_4);
			String str = "票据Ticket_V生成时间戳为："+TS_4;
			d.demo(str);
			System.out.println(ID_C+"->TGS:" + ID_V + Ticket_TGS + Authenticator_C);
			Des des=new Des();
			Ticket_V=KEY_C_V+ID_C+AD_C+ID_V+TS_4+LifeTime_4;
			//System.out.println("加密前Ticket_V :"+Ticket_V);
			Ticket_V=des.Encrypt(Ticket_V, KEY_V);
			//System.out.println("加密后Ticket_V :"+Ticket_V);
			Message=KEY_C_V+ID_V+TS_4+Ticket_V;
			System.out.println("TGS->"+ID_C+"加密前:"+Message);
			Message=des.Encrypt(Message, KEY_C_TGS);
			System.out.println("TGS->"+ID_C+"加密后:"+Message);
			Message="1000000000"+Message;
			//System.out.println("发送成功");
			String str2 = "发送成功";
			d.demo(str2);
		}
		else{
			Des des=new Des();
			Message=des.Encrypt(ERROR, KEY_C_TGS);
			Message="1000010000"+Message;
			//System.out.println("反馈失败消息成功");
			String str3 = "反馈失败消息成功";
			d.demo(str3);
		}
		return Message;
	}
	
	//分解Authenticator_C
	public void Analyse_Authenticator_C(){
		Des des = new Des();
		String message = des.Decrypt(Authenticator_C, KEY_C_TGS);
		LinkedHashMap<String, Integer> map = pro.AUTHENTICATOR_C_TGS();
		for(Entry<String, Integer> entry : map.entrySet()){
			if(entry.getKey() == "ID_C"){
				ID_C = message.substring(0, entry.getValue());
				//System.out.println("ID_C"+ID_C);
				message = message.substring(entry.getValue());
			}
			if(entry.getKey() == "AD_C"){
				AD_C = message.substring(0, entry.getValue());
				//System.out.println("AD_C"+AD_C);
				message = message.substring(entry.getValue());
			}
			if(entry.getKey() == "TS_3"){
				TS_3 = message.substring(0, entry.getValue());
				//System.out.println("TS_3"+TS_3);
				message = message.substring(entry.getValue());
			}
		}
		
	}

	
	public boolean Verify(){
		if(ID_C.equals(ID_C_1)){
			if(AD_C.equals(AD_C_1)){
				//System.out.println(TS_2+"  "+TS_4+"  "+LifeTime_2);
				if(tool.comT(TS_2, TS_4, LifeTime_2)){
					//这里打印收到C001
					//System.out.println("接收到"+ID_C+"向"+ID_V+"发出的请求消息，时间戳为："+TS_3);
					
					String str = "接收到" + ID_C + "向" + ID_V + "发出的请求消息";
					d.demo(str);
					return true;
				}
				else{
					ERROR="Ticket_TGS Time Out!";
					//System.out.println("Ticket_TGS过期！");
					String str = "Ticket_TGS过期！";
					d.demo(str);
					return false;
				}
			}
			else{
				ERROR="Verify ADc failed!";
				//System.out.println("ADc不同验证失败！");
				String str = "ADc不同验证失败！";
				d.demo(str);
				return false;
			}
		}
		else{
			ERROR="Verify Client ID failed!";
			//System.out.println("Client ID 不同验证失败！");
			String str = "Client ID 不同验证失败！";
			d.demo(str);
			return false;
		}
	}

	//解密Ticket_TGS
	public String Analyse_Ticket_TGS(){
		
		Des des = new Des();
		String temp = null;
		//System.out.println(Ticket_TGS.length());//Ticket_TGS的长度
		try {
			KEY_TGS = getKEY_TGS(conn);//获取KEY_TGS
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(KEY_TGS);//
		temp = des.Decrypt(Ticket_TGS, KEY_TGS);//解码
		return temp;
	}
	
	public String getKEY_TGS(Connection c) throws SQLException{				  
	    ResultSet ret = null; 
	    String id_as = null;
	    int a = 0;
		Statement statement = c.createStatement();
	    String sql = "select *from astgs";//SQL语句 
		ret = statement.executeQuery(sql);//执行语句，得到结果集 	  
        try { 
			while (ret.next()) {   
				id_as = ret.getString("v_id");
				if(id_as.equals("A001")){
					KEY_TGS = ret.getString("key_v");
					a = 1;
				}    
			}//显示数据  			
			ret.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return KEY_TGS;  
	}
	
	
	public int getKEY_V(Connection c) throws SQLException{  
	    ResultSet ret = null; 
	    String v_id = null;
	    Statement statement = c.createStatement();
	    int b = 0;
	    String sql = "select *from v";//SQL语句  
		ret = statement.executeQuery(sql);//执行语句，得到结果集  
        try {
			while (ret.next()) {
				v_id = ret.getString("v_id");
				if(v_id.equals(ID_V)){
					KEY_V = ret.getString("key_v");
					System.out.println("key——"+KEY_V);
					b = 1;
				}	
			}//显示数据  
			ret.close();		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;  
	}
}

