package kr.coffee.setting.service;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import kr.coffee.setting.Config;
import kr.coffee.setting.dao.DataBaseDao;
import kr.coffee.setting.jdbc.DBCon;
import kr.coffee.setting.jdbc.JdbcUtil;

public class ExportSettingService extends ServiceSetting{
	
	@Override
	public void initSetting() {
		DataBaseDao dao = DataBaseDao.getInstance();
		dao.selectUseDatabase();
		
		checkBackupDir();
		
		for(String tableName : Config.TABLE_NAME){
			executeExportData(getFilePath(tableName), tableName);
		}		
	}
	
	private void checkBackupDir() {
		File backupDir = new File(Config.EXPORT_DIR);
		if (backupDir.exists()){
			for(File file : backupDir.listFiles()){
				file.delete();
				System.out.printf("%s Delete Success! %n",file.getName());
			}
		}else{
			backupDir.mkdir();
			System.out.printf("%s make dir Success! %n",Config.EXPORT_DIR);
		}
				
	}

	public void executeExportData(String tablePath, String tableName) {
		String sql = String.format("select * into outfile '%s' "
				+ "character set 'UTF8' " + "fields TERMINATED by ',' "
				+ "LINES TERMINATED by '\n' from %s", 
				tablePath,	tableName);
		
		Statement stmt = null;
		try {
			Connection con = DBCon.getConnection();
			stmt = con.createStatement();
			stmt.executeQuery(sql);
			System.out.printf("Export Table(%s) %d Rows Success! %n",tableName, stmt.getUpdateCount());
		} catch (SQLException e) {
			System.out.printf("error %d : %s %n", e.getErrorCode(), e.getMessage());
		} finally {
			JdbcUtil.close(stmt);
		}
	}
}