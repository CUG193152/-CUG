package FileServer;

import java.awt.*;
import java.io.File;
import javax.swing.*;

public class ServerShareSurface {

	private JLabel label_1;
	private JLabel label_2;
	private JTextArea textarea_1;
	private JTextArea textarea_2;
	
	public void textarea_1_show(String str){
		
		textarea_1.append(str+"\r\n");
	}
	
	public void textatea_1_flus(){
		textarea_1.setText(null);
	}
	public void textarea_2_show(String str){
		
		textarea_2.append(str+"\r\n");
	}
	
	
//	public void getFileName(FileServer_UI ui) {
//		   String path = "C:/Users/cpl/Desktop/FileServer"; // 路径
//		   ui.textarea_1_show("路径为："+"\t\n"+"C:/Users/cpl/Desktop/FileServer");
//		   File f = new File(path);		 
//		    File fa[] = f.listFiles();
//		      for (int i = 0; i < fa.length; i++) {
//		       File fs = fa[i];
//		      if (!fs.isDirectory()) {
//		    	  ui.textarea_1_show(fs.getName());
//		       } 	     
//		      }
//	}
	
	
	public void getFileName() {
		   String path = "C:/Users/cpl/Desktop/FileServer"; // 路径
		   textarea_1_show("路径为："+"\t\n"+"C:/Users/cpl/Desktop/FileServer");
		   File f = new File(path);		 
		    File fa[] = f.listFiles();
		      for (int i = 0; i < fa.length; i++) {
		       File fs = fa[i];
		      if (!fs.isDirectory()) {
		    	  textarea_1_show(fs.getName());
		       } 	     
		      }
	}
	
	
	
	public ServerShareSurface(){
		JFrame f = new JFrame("文件服务器");
		f.setLocation(400, 150);
		f.setSize(700, 500);		
		Container c=f.getContentPane();
		c.setLayout(null);
				
		label_1 = new JLabel("文件列表");	
		label_1.setFont(new Font("Serif",1,17));
		label_1.setBounds(20, 15, 100, 20);
		c.add(label_1);
		
		textarea_1 = new JTextArea();
		textarea_1.setFont(new Font("Serif",0,15));
		textarea_1.setBounds(20, 40, 200, 400);
		textarea_1.setEditable(false);
		c.add(textarea_1);
		
		label_2 = new JLabel("访问信息");	
		label_2.setFont(new Font("Serif",1,17));
		label_2.setBounds(250, 15, 100, 20);
		c.add(label_2);
		
		textarea_2 = new JTextArea();
		textarea_2.setFont(new Font("Serif",0,15));
		textarea_2.setBounds(250, 40, 400, 400);
		textarea_2.setEditable(false);
		c.add(textarea_2);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	public static void main(String[] args) {
		new ServerShareSurface();
	}
}
