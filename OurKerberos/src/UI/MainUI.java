package UI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Client.MyClient;


public class MainUI extends JFrame {
	CardLayout card = new CardLayout();
	JPanel cardPanel = new JPanel(card); // 采用卡片布局的方式
	JButton clickButton; // 点击的按钮
	int MyleftMoney = 150;
	
	JLabel FirstPanelLabelImage = new JLabel(
			new ImageIcon("images/1.png"));
	JLabel FirstPanelLabelName = new JLabel("帐号  ");
	JLabel FirstPanelLabelPasswd = new JLabel("密码  ");
	JTextField FirstPanelFieldName = new JTextField(15);
	JPasswordField FirstPanelFieldPasswd = new JPasswordField(15);

	JButton FirstPanelButton1 = new JButton("登录");
	JButton FirstPanelButton2 = new JButton("注册");
	
	public void init()
	{
		add(cardPanel);

		// panel 1 登录界面
		/*********************************************************/
		JPanel FirstPanel = new JPanel(new BorderLayout());
		cardPanel.add(FirstPanel);

		FirstPanelButton1.addActionListener(new MyButtonActionListener());
		FirstPanelButton2.addActionListener(new MyButtonActionListener());
		
		FirstPanel.add(FirstPanelLabelImage, BorderLayout.NORTH);
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel FirstPanelDown = new JPanel(gb);

		FirstPanel.add(FirstPanelDown, BorderLayout.CENTER);
		FirstPanelDown.setBackground(Color.GRAY);
		FirstPanelLabelName.setFont(new Font("宋体", Font.BOLD, 20));
		FirstPanelLabelPasswd.setFont(new Font("宋体", Font.BOLD, 20));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		FirstPanelDown.add(FirstPanelLabelName, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		FirstPanelDown.add(FirstPanelFieldName, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		FirstPanelDown.add(new JLabel(" "), gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		FirstPanelDown.add(FirstPanelLabelPasswd, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		FirstPanelDown.add(FirstPanelFieldPasswd, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		FirstPanelDown.add(new JLabel(" "), gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		FirstPanelDown.add(FirstPanelButton1, gbc);
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		FirstPanelDown.add(FirstPanelButton2, gbc);
		
		this.setSize(800, 650);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
	}
	
	class MyButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			clickButton = (JButton) e.getSource();
			if (clickButton == FirstPanelButton1 )
			{
				System.out.println(new String(FirstPanelFieldPasswd.getPassword()));
				MyClient myClient=new MyClient(FirstPanelFieldName.getText(),new String(FirstPanelFieldPasswd.getPassword()));
				System.out.println(myClient.getClientConnectionV().toString());
//				while(true) {
//					System.out.println(myClient.getClientConnectionV().getClient().readLine());
//				}
				if(myClient.getFlag()) {
					
					
					new SecondUI(myClient).init(FirstPanelFieldName.getText());
					dispose();
					
				}else {
					JOptionPane.showMessageDialog(null, "您的账户密码输入有误"); 
				}
				
				
			} else {
				RegisterUI RUI=new RegisterUI();
				RUI.setVisible(true);
			}
		
	}
	}
	public static void main(String[] args) {
		new MainUI().init();
	}
	
}

