package UI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;



public class ThirdUI extends JFrame{
	
	JButton clickButton; // 点击的按钮
	ArrayList<JButton> TimeButton = new ArrayList<>(); // 选择时间按钮的数组
	JFrame FrameChoiceTime = new JFrame(); // 选择时间的窗体

	public void init()
	{
		JPanel ChoiceTimePanel = new JPanel(new BorderLayout());

		JPanel ChoiceTime = new JPanel(new GridLayout(20, 1, 0, 2));
		ChoiceTime.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		ChoiceTimePanel.add(new JScrollPane(ChoiceTime), BorderLayout.CENTER);
		JPanel HeadPanel = new JPanel(new GridLayout(1, 3, 10, 0));
		HeadPanel.add(new JLabel("时间", JLabel.CENTER));
		HeadPanel.add(new JLabel("演出厅", JLabel.CENTER));
		HeadPanel.add(new JLabel("票价", JLabel.CENTER));
		ChoiceTimePanel.add(HeadPanel, BorderLayout.NORTH);
		for (int i = 0; i < 16; i += 2) {
			JButton ChoiceTimeButton = new JButton(i + 8 + ":00" + " ~ " + (i + 10) + ":00"
					+ "                          " + i + "号厅" + "                           " + (i + 30));
			TimeButton.add(ChoiceTimeButton);
			ChoiceTimeButton.addActionListener(new ChoiceTimeListener());
			ChoiceTime.add(ChoiceTimeButton);
		}
		FrameChoiceTime.setTitle("时间选择");
		FrameChoiceTime.add(ChoiceTimePanel);
		FrameChoiceTime.setSize(430, 230);
		FrameChoiceTime.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		FrameChoiceTime.setLocationRelativeTo(null);
		FrameChoiceTime.setVisible(true);
		FrameChoiceTime.setResizable(false);
	}
	
	class ChoiceTimeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			clickButton = (JButton) e.getSource();
			for (int i = 0; i < 8; i++) {
				if (clickButton == TimeButton.get(i)) {
					FrameChoiceTime.dispose();
					new FourthUI().init();
					
					
				}
			}
		}
	}
}
