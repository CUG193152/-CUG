package Helper;

import java.util.Calendar;
import java.util.Random;

public class Tool {
	public String getTime() {
		Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		String TS = "";
		if (month < 10)
			TS += 0;
		TS += Integer.toString(month);
		if (date < 10)
			TS += 0;
		TS += Integer.toString(date);
		if (hour < 10)
			TS += 0;
		TS += Integer.toString(hour);
		if (minute < 10)
			TS += 0;
		TS += Integer.toString(minute);
		if (second < 10)
			TS += 0;
		TS += Integer.toString(second);
		return TS;
	}

	/**
	 * 生成指定位数的随机秘钥
	 * 
	 * @param KeyLength
	 * @return KeyRandom
	 */
	public String KeyCreate(int KeyLength) {

		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer Keysb = new StringBuffer();
		for (int i = 0; i < KeyLength; i++) // 生成指定位数的随机秘钥字符串
		{
			int number = random.nextInt(base.length());
			Keysb.append(base.charAt(number));
		}
		return Keysb.toString();
	}
}
