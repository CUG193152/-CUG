package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Client.MyClient;
import Helper.Des;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class ThirdUI extends JFrame {

	private JPanel contentPane;
	String message1 ;
	String message2 ;
	Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
	MyClient myClient;
	JComboBox<String> comboBox;
	
	public ThirdUI(MyClient myClient,String onlineusers) {
		this.myClient = myClient;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 320);
		contentPane = new JPanel();
		this.setLocation(screenSize.width/2-300,screenSize.height/2-150);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setResizable(false);
		
		
		

		comboBox = new JComboBox<String>();
		comboBox.removeAllItems();
		comboBox.addItem("下拉显示");

		String []Users = onlineusers.split(" ");
		for(int i=0;i<Users.length;++i)
		{
			if(!Users[i].split(" ")[0].equals(myClient.getID_C()))
			{
				comboBox.addItem(Users[i].split(" ")[0]);

				System.out.println("Users[i].split()[0]"+Users[i].split(" ")[0]);
			}
			
		}
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(65, 28, 240, 27);
		
		contentPane.add(comboBox);
//		comboBox.setEditable(false);     // 设置选择框为不可编辑
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				message1 = comboBox.getSelectedItem().toString();
			}
		});
		
		
		JComboBox comboBox_1 = new JComboBox(new Object[]{});
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"电影选择","十二生肖","饥饿游戏","听风者","复仇者联盟","泰坦尼克号","师父","九层妖塔","心花怒放"}));
		comboBox_1.setSelectedIndex(0);
		comboBox_1.setBounds(65, 67, 240, 27);
		contentPane.add(comboBox_1);
		comboBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				message2 = comboBox_1.getSelectedItem().toString();
			}
		});
		
		
		
		
		JButton sendButton = new JButton("发送消息");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(message1.equals("下拉显示")||message2.equals("电影选择"))
				{
					JOptionPane.showMessageDialog(null, "请选择用户", "反馈信息", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					String IDandMovie = message1 + " " + message2;
					System.out.println("IDandMovie"+IDandMovie);
				}
			}
		});
		sendButton.setBounds(372, 19, 117, 45);
		contentPane.add(sendButton);
		
		JButton updatebutton = new JButton("更新");
		updatebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				dispose();
//				System.out.println("Get online users");
				System.out.println("Get online users");
				String message = "000011011 "+ new Des().Encrypt(myClient.getID_C() , myClient.getK_C_V());
				System.out.println("message:"+message);
				myClient.getClientConnectionV().getClient().println(message);
				String onlineusers = myClient.getClientConnectionV().getClient().readLine();
				onlineusers = new Des().Decrypt(onlineusers, myClient.getK_C_V()).trim();
				System.out.println(onlineusers);
				ThirdUI thirdUI=new ThirdUI(myClient,onlineusers);
				thirdUI.setVisible(true);
			}
		});
		updatebutton.setBounds(227, 271, 117, 21);
		contentPane.add(updatebutton);
	}
}
