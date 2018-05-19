package Tools;

import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Tool {

	//获取客户端的IP
	public String getIP(Socket s){
		String ip = null;
		String temp="";
		ip = s.getInetAddress().getHostAddress();
		String[] te = ip.split("\\.");
		for(int i = 0; i < te.length;i++){
			for(int j = 0; j < 3-te[i].length(); j++){
				temp+="0";
			}
			temp+=te[i];
		}
		return temp;
	}
	
	public String getTime(){
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
	public String CreateKey(){//创建DES密钥
		int count = 0;
		int max = 62;
		int i;	
		char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',  
		        'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',  
		        'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',  
		        'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',  
		        'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer Key = new StringBuffer("");
		Random r = new Random();
		while(count < 8){
			i = Math.abs(r.nextInt(max));
			if (i>=0&&i<str.length){
				Key.append(str[i]);
				count ++;
			}
		}
		String key = Key.toString();
		return key;
	}

	public boolean comT(String t1,String t2,String t3){
    	boolean b = false;
    	Date date1 ,date2;
        DateFormat formart = new SimpleDateFormat("MMddHHmmss");
        long l3 = 86400000;
        try {
			date1 = formart.parse(t1);
			date2 = formart.parse(t2);
			long l2 = date1.getTime()-date2.getTime()+l3;
			if(l2 > 0){
				b = true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return b;
    }
	
 	public String MoveSpace(String name) {
		int num=name.length();
		if(num>0)
		while(name.substring(num-1, num).equals(" ")){
			name=name.substring(0, num-1);
			num--;
		}
		return name;
	}
}
