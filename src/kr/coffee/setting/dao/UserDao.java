package kr.coffee.setting.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import kr.coffee.setting.Config;
import kr.coffee.setting.jdbc.DBCon;
import kr.coffee.setting.jdbc.JdbcUtil;

public class UserDao {
	private static UserDao instance = new UserDao();
	
	private UserDao() {}

	public static UserDao getInstance() {
		return instance;
	}

	public void initUser(){
		createUser();
		grantUser();
//		createUserAndGrant();
	}
	
	private void createUser(){
		String sql = "create user ? identified by ?";
		PreparedStatement pstmt = null;
		try {
			Connection con = DBCon.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, Config.PJT_USER);
			pstmt.setString(2, Config.PJT_PASSWD);
			pstmt.execute();
			System.out.printf("Create User(%s) Success! %n", Config.PJT_USER);
		} catch (SQLException e) {
			if (e.getErrorCode()==1396){
				System.err.printf("User(%s) exists!%n", Config.PJT_USER);
				dropUser();
				createUser();
			}
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
	
	/* 계정 삭제 */
	private void dropUser() {
		String sql = "drop user ?";
		PreparedStatement pstmt = null;
		try {
			Connection con = DBCon.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, Config.PJT_USER);
			pstmt.executeUpdate();
			System.out.printf("Drop User(%s) Success! %n", Config.PJT_USER);
		} catch (SQLException e) {
			System.err.printf("Drop User(%s) Fail! %n", Config.PJT_USER);
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	/* 계정에 대한 권한 Config.DB_NAME에 해당하는 데이터베이스만 select, insert
	 * update, delete권한 만 부여 
	 * */
	private void grantUser() {
		String sql = "grant select, insert, update, delete on " + Config.DB_NAME + ".* to ?";
		PreparedStatement pstmt = null;
		try {
			Connection con = DBCon.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, Config.PJT_USER);
			pstmt.executeUpdate();
			System.out.printf("Grant User(%s) Success! %n", Config.PJT_USER);
		} catch (SQLException e) {
			System.err.printf("Grant User(%s) Fail! %n", Config.PJT_USER);
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	/*
	 * 계정추가및 해당 권한을 원샷~~!!
	 * 
	 */
	private void createUserAndGrant() {
		String sql = "grant select, insert, update, delete on " + Config.DB_NAME+ ".* to ? identified by ?";
		PreparedStatement pstmt = null;
		try {
			Connection con = DBCon.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, Config.PJT_USER);
			pstmt.setString(2, Config.PJT_PASSWD);
			pstmt.execute();
			System.out.printf("Create User(%s) Success! %n", Config.PJT_USER);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
}