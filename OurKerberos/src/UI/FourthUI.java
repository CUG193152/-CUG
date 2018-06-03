package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Client.MyClient;
import Helper.Des;



public class FourthUI {
	JButton clickButton; // 点击的按钮
	ArrayList<JButton> SeatButton = new ArrayList<>(); // 选择座位按钮的数组
	JFrame FrameChoiceSeat = new JFrame(); // 选择座位的窗体
	JButton ConfirmChoiceSeat = new JButton("确认购买");
	String seat;
	String num;
	int had[];
	int buy[];
	MyClient myClient;
	FourthUI(MyClient myClient,String seat)
	{
		this.myClient = myClient;
		this.seat = seat;
		buy = new int[25];
	}
	public void init(String num)
	{
		this.num = num;
		JPanel ChoiceSeatPanel = new JPanel(new BorderLayout());
		ChoiceSeatPanel.add(ConfirmChoiceSeat, BorderLayout.SOUTH);
		ConfirmChoiceSeat.addActionListener(new ChoiceSeatListener());
		ChoiceSeatPanel.setFont(new Font("宋体", Font.BOLD, 15));
		JPanel SeatPanel = new JPanel(new GridLayout(5, 5, 2, 2));
		SeatPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		ChoiceSeatPanel.add(SeatPanel, BorderLayout.CENTER);
		
		had = unpackseat(seat);

		for (int j = 0; j < 25; j++) {
			JButton ChoiceSeatButton = new JButton();
			ChoiceSeatButton.addActionListener(new ChoiceSeatListener());
			if(had[j]==1)
			{
				ChoiceSeatButton.setBackground(Color.PINK);
				ChoiceSeatButton.setEnabled(false);
			}
			else
			{
				ChoiceSeatButton.setBackground(Color.GREEN);
			}
			ChoiceSeatButton.setOpaque(true);
			ChoiceSeatButton.setBorderPainted(false);
			SeatPanel.add(ChoiceSeatButton);
			SeatButton.add(ChoiceSeatButton);
		}

		FrameChoiceSeat.setTitle("座位选择");
		FrameChoiceSeat.add(ChoiceSeatPanel);
		FrameChoiceSeat.setSize(430, 230);
		FrameChoiceSeat.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		FrameChoiceSeat.setLocationRelativeTo(null);
		FrameChoiceSeat.setVisible(true);
		FrameChoiceSeat.setResizable(false);
	}
	
	class ChoiceSeatListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			clickButton = (JButton) e.getSource();
			for (int i = 0; i < 25; i++) {
				if (clickButton == SeatButton.get(i)) {
					if (SeatButton.get(i).getBackground() == Color.GREEN) {
						SeatButton.get(i).setBackground(Color.RED);
						SeatButton.get(i).setOpaque(true);
						SeatButton.get(i).setBorderPainted(false);
						
					} else {
						SeatButton.get(i).setBackground(Color.GREEN);
						SeatButton.get(i).setOpaque(true);
						SeatButton.get(i).setBorderPainted(false);
					}
					break;
				}
			}
			if (clickButton == ConfirmChoiceSeat) {
				for (int i = 0; i < 25; i++) {
					if (SeatButton.get(i).getBackground() == Color.RED) 
					{
						buy[i]=1;
					}
				}
				seat = packseat(buy);
				
				String str;
				if(!seat.equals(""))
				{
					String message = "000011010 "+ new Des().Encrypt(num+" "+seat.trim(), myClient.getK_C_V());
					myClient.getClientConnectionV().getClient().println(message);
					String sign = myClient.getClientConnectionV().getClient().readLine();
					sign = new Des().Decrypt(sign, myClient.getK_C_V()).trim();
					System.out.println(sign);
					
					if(sign.equals("yes"))
					{
						str = "成功购买:\n";
						for (int i = 0; i < 25; i++) {
							if (SeatButton.get(i).getBackground() == Color.RED) {
								str += i / 5 + 1;
								str += " 排";
								str += i % 5 + 1;
								str += " 列\n";
							}
						}
						str += "祝您观影愉快!";
					}
					else
					{
						str = "购买失败！:\n";
					}
				}
				else
				{
					str = "请选择座位！:\n";
				}
				JOptionPane.showMessageDialog(null, str, "确认信息", JOptionPane.INFORMATION_MESSAGE);
				FrameChoiceSeat.dispose();
			}
		}
	}
	
	
	public int[] unpackseat(String seat)
	{
		System.out.println("seat:"+seat);
		int had[] = new int[25];
		String[] strArr = seat.split(" ");
		for(int i=0;i<strArr.length;++i)
		{
			had[Integer.parseInt(strArr[i])-1] = 1;
		}
		return had;
	}
	
	public String packseat(int []buy)
	{
		String seat = "";
		for(int i=0;i<25;++i)
		{
			if(buy[i]==1)
			{
				seat = seat +Integer.toString(i+1)+" ";
			}
		}
		
		return seat;
	}
}
