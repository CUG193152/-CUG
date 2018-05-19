package FileServer;


import java.io.*;
import java.net.Socket;

import Tools.*;

public class ServerShareThread implements Runnable { 
	 private Socket socket;
	 public Des des = new Des();
	 public Tool tool = new Tool();
	 ServerShareVerify v = new ServerShareVerify();
	 String KEY_C_V = "";
	 String ID_C = "";
	 
	 int flag=0;
	 ServerShareSurface ui;
	 public ServerShareThread(Socket socket,ServerShareSurface ui) { 
	     this.socket = socket; 
	     this.ui=ui;
	 } 
	 
	 
	 public void run() { 
		
	 while(true){	
		 
		 while(flag==0){
			 // 
			 
			 System.out.println("flag"+flag);
             try {
				BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
				 //BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
				 PrintWriter out=null;
				 String str = r.readLine();				 
				 v.PullPack(str);
				String Dec_Ticket_TGS=v.Decrypt_Ticket_V();
				System.out.println(Dec_Ticket_TGS);
				try {
					out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8")),true);
					out.println(v.Analyse_Ticket_v(Dec_Ticket_TGS));
					out.flush();
					KEY_C_V=v.KEY_C_V;
					//System.out.println("output close");
					//socket.close();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}     
             flag=1;
             System.out.println("flag    "+flag);
             if(flag==1)
            	 break;
			
		 }
		 if(flag!=0)
				myserver();
			
			}
	 }


	private void myserver(){
		try {
			FileOutputStream fos=null; 			
					// 文件名 
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));

			String temp = br.readLine();
			System.out.println(temp+temp.length());
			String head = temp.substring(0, 10);
			System.out.println(head);
			temp = temp.substring(10);

			temp = des.Decrypt(temp, KEY_C_V);
			temp = tool.MoveSpace(temp);
			System.out.println(temp+"qqq");
				if(head.equals("0011100010")&&temp.equals("req")){
					request();		
					System.out.println(temp);
				}				
				else if(head.equals("0011100011")){
					String fileName=temp;
					try {
							sendFile(temp);
							ID_C = v.ID_C;
						ui.textarea_2_show(ID_C+"下载文件"+fileName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				  		 
				}
				else if(head.equals("0011101111")){	
					ID_C = v.ID_C;
					ui.textarea_2_show(ID_C+"下线");
					Thread.currentThread().destroy();
				}
 
		else{											
			String fileName=temp;
			File directory = new File("C:/Users/cpl/Desktop/FileServer"); 
			if(!directory.exists()) { 
				directory.mkdir(); 
			} 	
			ID_C= v.ID_C;
			ui.textatea_1_flus();			
			ui.textarea_2_show(ID_C+"上传文件"+fileName);
//			ui.textarea_2_show(directory.getAbsolutePath()+ File.separatorChar + fileName);
			File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName); 			  
			fos = new FileOutputStream(file);			  
			// 开始接收文件 
			byte[] bytes = new byte[1024]; 
			String mess=""; 
			while((mess = br.readLine()) != null) {
				  if(mess.equals("end")){
					  break;
			  }	
			
			if(mess.substring(0, 10).equals("0011100001")){
				 mess = mess.substring(10);
				  mess = des.Decrypt(mess, KEY_C_V);
				  bytes = mess.getBytes();							  				  
				  fos.write(bytes, 0, bytes.length);													  
				  fos.flush(); 								  
				}	
			
			else{
				System.out.println("上传文件失败");
			}											
		}
			fos.close();
			ui.getFileName();
	}	  
										 
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		 
	 
	 	public void request(){
	 		try {
				PrintWriter writer;
				writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
				String path = "C:/Users/cpl/Desktop/FileServer";
				File f = new File(path);		 
				File fa[] = f.listFiles();
				  for (int i = 0; i < fa.length; i++) {
				   File fs = fa[i];
				  if (!fs.isDirectory()) {
					  String temp = fs.getName();
					  System.out.println(temp);
					  temp = des.Encrypt(temp, KEY_C_V);
					  System.out.println(temp);
					  printMessage(writer,temp);
				   } 	     
				 }
				  printMessage(writer,"end");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 			 				
	 	}
	 	   

	 	public void printMessage(PrintWriter pw, String message) {
			pw.println(message);
			pw.flush();
		}
	    	 	
	 	public void sendFile(String FileName) throws Exception { 
	 			FileInputStream	fis = null;
	 			String path = "C:/Users/cpl/Desktop/FileServer/"+FileName;
				File file = new File(path); 
				if(file.exists()) { 
					fis = new FileInputStream(file); 							
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));			
				String filename=file.getName();
//				printMessage(writer,filename);				
//			   System.out.println(filename);	
	    	   System.out.println("======== 开始传输文件 ========"); 
	    	   byte[] bytes = new byte[1024]; 
	           int length = 0; 
	    	   String temp="";	    	   
	    	   while((length = fis.read(bytes, 0, bytes.length)) != -1) { 
	    		  temp=new String(bytes);
	    		  temp = des.Encrypt(temp, KEY_C_V);
	    		  printMessage(writer,temp);  
	    		  System.out.println("aaa");
	    	  }  
	    	  printMessage(writer,"end"); 
	    	  System.out.println("======== 文件传输成功 ========"); 
	      	} 				 			
		}
	 	

}
