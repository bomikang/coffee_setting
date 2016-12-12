package kr.coffee.setting.service;

import java.util.HashMap;
import java.util.Map;

import kr.coffee.setting.Config;
import kr.coffee.setting.dao.DataBaseDao;
import kr.coffee.setting.dao.TableDao;
import kr.coffee.setting.dao.UserDao;

public class InitSettingService extends ServiceSetting {
	private Map<String, String> TABLE_SQL = new HashMap<String, String>();

	public void initSetting() {
		createSql();		// create table문 생성
		createDataBase();	// 데이터베이스를 생성
		createTable(); 		// 해당 데이터베이스에서 테이블 생성
		createUser(); 		// 해당 데이터베이스 사용자 추가
	}

	private void createSql() {
		TABLE_SQL.put("product", "CREATE TABLE product (code VARCHAR(4)  NOT NULL, name VARCHAR(20) NULL,PRIMARY KEY (code))");
		TABLE_SQL.put("sale", "CREATE TABLE sale (code VARCHAR(4) NOT NULL, price INTEGER NULL, saleCnt INTEGER NULL, marginRate INTEGER NULL, PRIMARY KEY (code), FOREIGN KEY (code) REFERENCES product (code))");		
	}

	private void createDataBase() {
		DataBaseDao dao = DataBaseDao.getInstance();
		dao.createDatabase();
		dao.selectUseDatabase();
	}

	private void createTable() {
		TableDao dao = TableDao.getInstance();
		for (int i = 0; i < Config.TABLE_NAME.length; i++) {
			dao.createTable(TABLE_SQL.get(Config.TABLE_NAME[i]));
		}
		dao.createViewTable();
	}

	private void createUser() {
		UserDao.getInstance().initUser();
	}

}
