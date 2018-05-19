package AS;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import javax.swing.*;

@SuppressWarnings("serial")
public class ASSurface extends JFrame{
		Container co;
		JButton Clear_button = new JButton("清空");      
		JTextArea Content_text = new JTextArea(); 
		JScrollPane jsp = new JScrollPane(Content_text);
		JLabel label = new JLabel("AS消息列表");
        
	    public void demo(String str){
	        Content_text.append(str+"\r\n"); 
	        	    	
	    }
	    public ASSurface(){
	    	co=this.getContentPane();
	    	co.setLayout(null); 
	    	
	    	this.setTitle("AS");
	        this.setLocation(300, 100);  //窗口位置
	        this.setSize(600, 500);  //窗口大小
	        
	        Content_text.setFont(new Font("Serif", Font.PLAIN, 16));

	        
	        Clear_button.setBounds(220, 400, 130, 40);
	        Clear_button.setFont(new Font("Serif", Font.PLAIN, 25));
	        
	        jsp.setBounds(40, 60, 500, 320);
	        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
	        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);   
	        label.setBounds(220, 10, 200, 40);
	        label.setFont(new Font("Serif", Font.PLAIN, 25));
	        co.add(jsp);
	        co.add(label);
	        co.add(Clear_button);
	        
	         
	        //按钮响应
	        Clear_button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Content_text.setText(null);   //清空文本区
				}
			});  
	        Content_text.setEditable(false);//不可编辑
	        this.setVisible(true); 
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }
	    
	    public static String getTime(){
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改 
			int month = c.get(Calendar.MONTH)+1; 
			int date = c.get(Calendar.DATE); 
			int hour = c.get(Calendar.HOUR_OF_DAY); 
			int minute = c.get(Calendar.MINUTE); 
			int second = c.get(Calendar.SECOND); 
			String TS = "";
			if(month < 10)
				TS+= 0;
			TS += Integer.toString(month);
			if(date < 10)
				TS+= 0;
			TS+=Integer.toString(date);
			if(hour < 10)
				TS+= 0;
			TS+=Integer.toString(hour);
			if(minute < 10)
				TS+= 0;
			TS+=Integer.toString(minute);
			if(second < 10)
				TS+= 0;
			TS+=Integer.toString(second);
			return TS;
		}
	    
	    public static void main(String args[]){	
	    	ASSurface d = new ASSurface();
	    	//d.demo("21342rf3");

	    }  
}
