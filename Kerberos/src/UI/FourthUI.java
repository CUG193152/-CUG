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



public class FourthUI {
	JButton clickButton; // 点击的按钮
	ArrayList<JButton> SeatButton = new ArrayList<>(); // 选择座位按钮的数组
	JFrame FrameChoiceSeat = new JFrame(); // 选择座位的窗体
	JButton ConfirmChoiceSeat = new JButton("确认购买");

	public void init()
	{
		JPanel ChoiceSeatPanel = new JPanel(new BorderLayout());

		ChoiceSeatPanel.add(ConfirmChoiceSeat, BorderLayout.SOUTH);
		ConfirmChoiceSeat.addActionListener(new ChoiceSeatListener());
		ChoiceSeatPanel.setFont(new Font("宋体", Font.BOLD, 15));
		JPanel SeatPanel = new JPanel(new GridLayout(5, 5, 2, 2));
		SeatPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		ChoiceSeatPanel.add(SeatPanel, BorderLayout.CENTER);

		for (int j = 0; j < 25; j++) {
			JButton ChoiceSeatButton = new JButton();
			ChoiceSeatButton.addActionListener(new ChoiceSeatListener());
			ChoiceSeatButton.setBackground(Color.GREEN);
//			ChoiceSeatButton.setEnabled(false);
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
						
					} else {
						SeatButton.get(i).setBackground(Color.GREEN);
					}
					break;
				}
			}
			if (clickButton == ConfirmChoiceSeat) {
				String str = "成功购买:\n";
				for (int i = 0; i < 25; i++) {
					if (SeatButton.get(i).getBackground() == Color.RED) {
						str += i / 5 + 1;
						str += " 排";
						str += i % 5 + 1;
						str += " 列\n";
					}
				}
				str += "祝您观影愉快!";
				JOptionPane.showMessageDialog(null, str, "确认信息", JOptionPane.INFORMATION_MESSAGE);
				FrameChoiceSeat.dispose();
			}
		}
	}
}
