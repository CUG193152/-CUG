package UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.cc.test.CertManager;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

public class RegisterUI extends JFrame {

	private JPanel contentPane;
	private final JLabel label = new JLabel("用户名");
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;

//	/**
//	 * Launch the application.
//	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterUI frame = new RegisterUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 */
	public RegisterUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 300, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel lblNewLabel = new JLabel("密码");
		lblNewLabel.setBounds(104, 94, 61, 16);
		contentPane.add(lblNewLabel);
		label.setBounds(104, 66, 39, 16);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("确认密码");
		label_1.setBounds(104, 126, 61, 16);
		contentPane.add(label_1);
		
		
		passwordField = new JPasswordField();
		passwordField.setBounds(177, 89, 130, 26);
		contentPane.add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(177, 121, 130, 26);
		contentPane.add(passwordField_1);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(177, 66, 122, 16);
		contentPane.add(textArea);
		
		JButton button = new JButton("注册");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button.setEnabled(false);
				String userAccount=textArea.getText();
				boolean result=userAccount.matches("[0-9]+");
				if(result==true) {
					JOptionPane.showMessageDialog(null, "账户名不能为纯数字"); 
				}else {
					String password=new String(passwordField.getPassword());
					String passwordConfirm=new String(passwordField_1.getPassword());
					if(!password.equals(passwordConfirm)) {
						JOptionPane.showMessageDialog(null, "两次输入的密码不同"); 
					}else {
						if(password.matches("[0-9]+")==true) {
							JOptionPane.showMessageDialog(null, "密码不能为纯数字"); 
						}else {
							new CertManager(userAccount+" "+password);
							JOptionPane.showMessageDialog(null, "验证通过"); 
							dispose();
						}
					}
					
				}
				button.setEnabled(true);
				
			}
		});
		button.setBounds(153, 159, 117, 29);
		contentPane.add(button);
	}
}
