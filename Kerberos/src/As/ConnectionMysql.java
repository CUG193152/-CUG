package As;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import DBUtil.DBUtil;

public class ConnectionMysql {
	public static void main(String[] args) throws IOException {
		try {
		Connection connect = DBUtil.getConnect();  
	    Statement statement = (Statement) connect.createStatement(); // 对数据库的所有操作都通过statement来实现  
	    ResultSet result;  
	      
	    String sqlQuery = "select * from " + DBUtil.TABLE_PASSWORD + " where userAccount='" + "chencong" + "'";  
	      
	    // 查询类操作返回一个ResultSet集合，没有查到结果时ResultSet的长度为0  
	    result = ((java.sql.Statement) statement).executeQuery(sqlQuery); // 查询同样的账号是否存在  
	    if(result.next()){ // 已存在  
	        System.out.println("该账号已存在");
	    }  
	    else {
	    	System.out.println("该账号不存在");
	    }
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
