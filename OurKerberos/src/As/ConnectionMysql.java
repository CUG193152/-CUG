package As;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import DBUtil.DBUtil;

public class ConnectionMysql {
	public Boolean userAccountExsits(String userAccount) {
		try {
			Connection connect = DBUtil.getConnect();
			Statement statement = (Statement) connect.createStatement(); // 对数据库的所有操作都通过statement来实现
			ResultSet result;

			String sqlQuery = "select * from " + DBUtil.TABLE_PASSWORD + " where userAccount='" + userAccount + "'";

			// 查询类操作返回一个ResultSet集合，没有查到结果时ResultSet的长度为0
			result = ((java.sql.Statement) statement).executeQuery(sqlQuery); // 查询同样的账号是否存在
			if (result.next()) { // 已存在
				System.out.println("该账号已存在");
				return true;
			} else {
				System.out.println("该账号不存在");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getPassword(String userAccount) {
		try {
			Connection connect = DBUtil.getConnect();
			Statement statement = (Statement) connect.createStatement(); // 对数据库的所有操作都通过statement来实现
			ResultSet result;

			String sqlQuery = "select * from " + DBUtil.TABLE_PASSWORD + " where userAccount='" + userAccount + "'";

			// 查询类操作返回一个ResultSet集合，没有查到结果时ResultSet的长度为0
			result = ((java.sql.Statement) statement).executeQuery(sqlQuery); // 查询同样的账号是否存在
			if (result.next()) { // 已存在
				return result.getString("userPassword");
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void saveData(String userAccount, String password) {
		try {
			Connection connect = DBUtil.getConnect();
			Statement statement = (Statement) connect.createStatement(); // 对数据库的所有操作都通过statement来实现
			ResultSet result;

			String sqlQuery = "select * from " + DBUtil.TABLE_PASSWORD + " where userAccount='" + userAccount + "'";

			// 查询类操作返回一个ResultSet集合，没有查到结果时ResultSet的长度为0
			result = ((java.sql.Statement) statement).executeQuery(sqlQuery); // 查询同样的账号是否存在
			if (result.next()) { // 已存在
				System.out.println("账号已存在，插入失败！");
			} else {
				String sqlInsertPass = "insert into " + DBUtil.TABLE_PASSWORD + "(userAccount,userPassword) values('"+userAccount+"','"+password+"')";
				statement.executeUpdate(sqlInsertPass); // 插入帐号密码
				System.out.println("插入成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
