package kr.coffee.setting.service;

import kr.coffee.setting.Config;

public abstract class ServiceSetting {
	
	protected static String getFilePath(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append(Config.EXPORT_DIR).append(tableName).append(".txt");
		return sb.toString().replace("\\", "/");
	}
	
	public abstract void initSetting();
}
