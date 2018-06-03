package VServer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import DBUtil.DBUtillV;

public class ConnectionMysqlMovies {
	public String getSeat(String num) {
		try {
		Connection connect = DBUtillV.getConnect();  
	    Statement statement = (Statement) connect.createStatement(); // 对数据库的所有操作都通过statement来实现  
	    ResultSet result;  
	      
	    String sqlQuery = "select * from " + DBUtillV.TABLE_PASSWORD + " where num='" + num + "'";  
	      
	    // 查询类操作返回一个ResultSet集合，没有查到结果时ResultSet的长度为0  
	    result = ((java.sql.Statement) statement).executeQuery(sqlQuery); // 查询同样的账号是否存在  
	    if(result.next()){ // 已存在  
	    	return result.getString("seat");
	    }  
	    else {
	    	System.out.println("无电影");
	    	return null;
	    }
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Boolean updateSeat(String num,String seat) {
		try {
			Connection connect = DBUtillV.getConnect();  
			Statement statement = (Statement) connect.createStatement(); // 对数据库的所有操作都通过statement来实现  
			ResultSet result;  
			
			String sqlQuery = "select * from " + DBUtillV.TABLE_PASSWORD + " where num='" + num + "'";  
		      
		    // 查询类操作返回一个ResultSet集合，没有查到结果时ResultSet的长度为0  
		    result = ((java.sql.Statement) statement).executeQuery(sqlQuery); // 查询同样的账号是否存在  
		    if(result.next()){ // 已存在  
		    	String temp = result.getString("seat").trim(); 
		    	if(Check(seat,temp))
		    	{
		    		temp = temp + " " +seat;
		    		String sqlInsertInfo = "update " + DBUtillV.TABLE_PASSWORD +" set seat='"+temp+"' where num='" + num + "'";
			    	int row2 = statement.executeUpdate(sqlInsertInfo);
			    	return true;
		    	}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	public static Boolean Check(String buy,String temp)
	{
		String[] strArr1 = buy.split(" ");
		String[] strArr2 = temp.split(" ");
		for(int i=0;i<strArr1.length;++i)
		{
			for(int j=0;j<strArr2.length;++j)
			{
				if(strArr1[i].equals(strArr2[j]))
				{
					return false;
				}
			}
		}
		return true;
	}
		
}
