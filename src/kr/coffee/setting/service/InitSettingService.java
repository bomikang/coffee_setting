package kr.coffee.setting.service;

import kr.coffee.setting.Config;
import kr.coffee.setting.dao.DataBaseDao;
import kr.coffee.setting.dao.TableDao;
import kr.coffee.setting.dao.UserDao;

public class InitSettingService extends ServiceSetting {

	public void initSetting() {
		createDataBase();	// 데이터베이스를 생성
		createTable(); 		// 해당 데이터베이스에서 테이블 생성
		createUser(); 		// 해당 데이터베이스 사용자 추가
	}

	private void createDataBase() {
		DataBaseDao dao = DataBaseDao.getInstance();
		dao.createDatabase();
		dao.selectUseDatabase();
	}

	private void createTable() {
		TableDao dao = TableDao.getInstance();
		for (int i = 0; i < Config.TABLE_NAME.length; i++) {
			dao.createTable(Config.CREATE_SQL[i]);
		}
		dao.createTrigger();
	}

	private void createUser() {
		UserDao.getInstance().initUser();
	}

}
