package kr.coffee.setting.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import kr.coffee.setting.Config;
import kr.coffee.setting.jdbc.DBCon;
import kr.coffee.setting.jdbc.JdbcUtil;

public class DataBaseDao {
	private PreparedStatement pstmt;
	
	private static DataBaseDao instance = new DataBaseDao();

	private DataBaseDao() {}

	public static DataBaseDao getInstance() {
		return instance;
	}
	
	public void createDatabase() {
		try {
			Connection con = DBCon.getConnection();
			pstmt = con.prepareStatement("CREATE DATABASE " + Config.DB_NAME);
			pstmt.execute();
			System.out.printf("CREATE DATABASE(%s) Success! %n", Config.DB_NAME);
		} catch (SQLException e) {
			if (e.getErrorCode()==1007){
				System.out.printf("DATABASE(%s) Exist! %n", Config.DB_NAME);
				dropDatabase();
				createDatabase();
			}
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
	
	private void dropDatabase() {
		try {
			Connection con = DBCon.getConnection();
			pstmt = con.prepareStatement("DROP DATABASE IF EXISTS " + Config.DB_NAME);
			pstmt.execute();
			System.out.printf("DROP DATABASE(%s) Success! %n", Config.DB_NAME);
		} catch (SQLException e) {
			System.out.printf("DROP DATABASE(%s) Fail! %n", Config.DB_NAME);
		} finally {
			JdbcUtil.close(pstmt);
		}
		
	}

	public void selectUseDatabase() {
		try {
			Connection con = DBCon.getConnection();
			pstmt = con.prepareStatement("USE " + Config.DB_NAME);
			pstmt.execute();
			System.out.printf("USE DATABASE(%s) Selected Success! %n", Config.DB_NAME);
		} catch (SQLException e) {
			System.out.printf("USE DATABASE(%s) Selected Fail! %n", Config.DB_NAME);
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
		}		
	}

	public void setForeignKeyCheck(int isCheck){
		try{
			Connection con = DBCon.getConnection();
			pstmt = con.prepareStatement("SET FOREIGN_KEY_CHECKS = ?");
			pstmt.setInt(1, isCheck);
			pstmt.execute();
			System.out.printf("%s SET FOREIGN_KEY_CHECKS(%s) Success!%n", Config.DB_NAME, isCheck==0?"False":"True");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			JdbcUtil.close(pstmt);
		}
	}

}
