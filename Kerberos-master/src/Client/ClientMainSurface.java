package Client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class ClientMainSurface extends JFrame{
	private JLabel label1;
	private JFrame frame;
	private JTextField text1;
	private JButton button2;
	private JButton button3;
	private String ID_C = "C003";
	private JLabel label2;
	private Container label3;
	
	
	
	public ClientMainSurface() {
		frame = this;
		frame.setTitle("客户机认证");
		frame.setLocation(400, 150);
		frame.setSize(500, 400);	
		Container c=frame.getContentPane();
				
		c.setLayout(null);
		
		label1 = new JLabel("用户");
		label1.setFont(new Font("",0,25));
		label1.setBounds(78, 88, 50, 20);
		
		label2 = new JLabel("：");
		label2.setFont(new Font("",0,25));
		label2.setBounds(190, 88, 50, 20);
		
		label3 = new JLabel("您好！请选择一个专区：");
		label3.setFont(new Font("",0,22));
		label3.setBounds(100, 140, 300, 30);
		
		text1 = new JTextField(ID_C);
		text1.setBounds(130, 80, 50, 35);
		text1.setFont(new Font("",0,20));
		text1.setEditable(false);
		text1.setFocusable(false);
		
		button2 = new JButton("聊天区");
		button2.setBounds(80, 200, 160, 40);
		button2.setFont(new Font("",0,20));
		
		button2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				ClientVerify my = new ClientVerify(frame,ID_C,"V001");
				my.newS(my);
			}
		});
		
		button3 = new JButton("文件共享区");
		button3.setBounds(260, 200, 160, 40);
		button3.setFont(new Font("",0,20));
		
		button3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				ClientVerify my = new ClientVerify(frame,ID_C,"V002");
				try {
					my.newSV2(my);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
//		button4 = new JButton("服务器3");
//		button4.setBounds(320, 150, 100, 40);
//		button4.setFont(new Font("",0,18));
//		
//		button4.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent event){
//				MyClient my = new MyClient(frame,text1.getText(),"V003");
//				my.newS(my);
//			}
//		});
		
		
		c.add(label1);
		c.add(label2);
		c.add(label3);
		c.add(text1);
		c.add(button2);
		c.add(button3);
		//c.add(button4);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	
	public static void main(String[] args) {
		new ClientMainSurface();
	}

}
