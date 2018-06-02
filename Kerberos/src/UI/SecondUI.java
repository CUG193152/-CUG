package UI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.MatteBorder;

import Socket.ConnectionThread;


public class SecondUI extends JFrame{
	CardLayout card = new CardLayout();
	JPanel cardPanel = new JPanel(card); // 采用卡片布局的方式
	JButton clickButton; // 点击的按钮
	int MyleftMoney = 150;
	
	JLabel SecondPanelUserHeadImage = new JLabel(
			new ImageIcon("images/head1.png"));
	JLabel SecondPanelUserName;
	JButton SecondPanelViewFilmButton = new JButton("正在上映");
	JButton SecondPanelViewChargedButton = new JButton("我的影票");
	
	public void init(String userAccount)
	{
		SecondPanelUserName = new JLabel(userAccount);
		add(cardPanel);

		JPanel SecondPanel = new JPanel(new BorderLayout());
		cardPanel.add(SecondPanel);

		JPanel SecondPanelUP = new JPanel(new GridLayout(1, 7, 5, 5));
		SecondPanel.add(SecondPanelUP, BorderLayout.NORTH);

		SecondPanelUP.add(SecondPanelUserHeadImage);
		SecondPanelUP.add(SecondPanelUserName);
		SecondPanelUserName.setFont(new Font("宋体", Font.BOLD, 20));
		SecondPanelUP.add(SecondPanelViewFilmButton);
		SecondPanelViewFilmButton.setBackground(Color.YELLOW);
		SecondPanelViewFilmButton.setFont(new Font("宋体", Font.BOLD, 15));
		SecondPanelViewFilmButton.addActionListener(new MyButtonActionListener());
		SecondPanelUP.add(SecondPanelViewChargedButton);
		SecondPanelViewChargedButton.setBackground(Color.GREEN);
		SecondPanelViewChargedButton.setFont(new Font("宋体", Font.BOLD, 15));
		SecondPanelViewChargedButton.addActionListener(new MyButtonActionListener());
		SecondPanelViewChargedButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		JPanel SecondPanelDown = new JPanel(new GridLayout(3, 4, 10, 10));
		SecondPanelDown.setBorder(new MatteBorder(20, 5, 10, 30, Color.LIGHT_GRAY));
		SecondPanel.add(new JScrollPane(SecondPanelDown));

		clientMoviePanel film1 = new clientMoviePanel("十二生肖",
				"images/film1.png");
		clientMoviePanel film2 = new clientMoviePanel("饥饿游戏",
				"images/film2.png");
		clientMoviePanel film3 = new clientMoviePanel("听风者",
				"images/film3.png");
		clientMoviePanel film4 = new clientMoviePanel("复仇者联盟",
				"images/film4.png");
		clientMoviePanel film5 = new clientMoviePanel("泰坦尼克号",
				"images/film5.png");
		clientMoviePanel film6 = new clientMoviePanel("师父",
				"images/film6.png");
		clientMoviePanel film7 = new clientMoviePanel("九层妖塔",
				"images/film7.png");
		clientMoviePanel film8 = new clientMoviePanel("心花怒放",
				"images/film8.png");
		SecondPanelDown.add(film1.panel);
		SecondPanelDown.add(film2.panel);
		SecondPanelDown.add(film3.panel);
		SecondPanelDown.add(film4.panel);
		SecondPanelDown.add(film5.panel);
		SecondPanelDown.add(film6.panel);
		SecondPanelDown.add(film7.panel);
		SecondPanelDown.add(film8.panel);

		
		this.setSize(800, 650);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
	}
	
	class MyButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			clickButton = (JButton) e.getSource();
			if (clickButton == SecondPanelViewFilmButton) {
				card.first(cardPanel);
				for (int i = 0; i < 1; i++) {
					card.next(cardPanel);
				}
				repaint();
			}
		}
	}
	class clientMoviePanel extends JFrame 
	{
		public clientMoviePanel() 
		{
		}

		public clientMoviePanel(String FilmNamePath, String FilmImagePath)
		{
			this.FilmName = new JLabel(FilmNamePath, JLabel.CENTER);
			this.FilmImage = new JLabel(new ImageIcon(FilmImagePath));
			BuyTicketButton = new JButton("买票");
			BuyTicketButton.addActionListener(new MovieButtonListener());
			ViewIntroduce = new JButton("介绍");
			ViewIntroduce.addActionListener(new MovieButtonListener());
			Box horizontal = Box.createHorizontalBox();
			horizontal.add(BuyTicketButton);
			horizontal.add(Box.createHorizontalStrut(40));
			horizontal.add(ViewIntroduce);

			panel.add(FilmName, BorderLayout.NORTH);
			panel.add(FilmImage, BorderLayout.CENTER);
			panel.add(horizontal, BorderLayout.SOUTH);

		}

		JPanel panel = new JPanel(new BorderLayout());
		JLabel FilmName;
		JLabel FilmImage;
		JButton BuyTicketButton;
		JButton ViewIntroduce;

		class MovieButtonListener implements ActionListener
		{

			public void actionPerformed(ActionEvent e) 
			{
				clickButton = (JButton) e.getSource();
				if (clickButton == BuyTicketButton)
				{

					new ThirdUI().init();
				}
				else if (clickButton == ViewIntroduce) {
					JTextArea textArea = new JTextArea("这是一部非常有趣的电影!");
					textArea.setColumns(10);
					textArea.setRows(5);
					textArea.setLineWrap(true);
					textArea.setEditable(false);
					JOptionPane.showMessageDialog(textArea, new JScrollPane(textArea), "电影介绍",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

			
		}
	}
}
