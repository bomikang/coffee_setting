package kr.coffee.setting;

public class Config {
	public static final String USER   = "root";
	public static final String PWD    = "rootroot";
	public static final String URL    = "jdbc:mysql://localhost:3306/";
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	
	public static final String DB_NAME = "coffee_project";
	public static final String PJT_USER = "user_coffee";
	public static final String PJT_PASSWD = "rootroot";

	public static final String[] TABLE_NAME = {"product","sale", "sale_detail"};
	
	public static final String EXPORT_DIR = System.getProperty("user.dir")+ "\\BackupFiles\\";
	public static final String IMPORT_DIR = System.getProperty("user.dir")+ "\\DataFiles\\";
	public static final String MYSQL_EXPORT_PATH = "C:\\ProgramData\\MySQL\\MySQL Server 5.6\\Uploads\\";
	
	public static final String[] CREATE_SQL ={
			
			"CREATE TABLE product (	"
			+ "code VARCHAR(4)  NOT NULL,	"
			+ "name VARCHAR(20) null,	"
			+ "primary key (code))",
			
			"CREATE TABLE sale (	"
			+ "code VARCHAR(4) NOT NULL,"
			+ "price INT(11) NULL, "
			+ "saleCnt INT(11) NULL,"
			+ "marginRate INT(11) null, "
			+ "primary key (code), "
			+ "FOREIGN KEY (code) REFERENCES product (code))",
			
			"CREATE TABLE sale_detail (	"
			+ "code VARCHAR(4)  NULL, "
			+ "sale_price INT(11) NULL,	"
			+ "addTax INT(11) NULL, "
			+ "supply_price INT(11) NULL, "
			+ "marginPrice  INT(11) null, "
			+ "FOREIGN KEY (code) REFERENCES product (code) on delete cascade)"
	};
	
	public static final String CREATE_TRIGGER = 
			"CREATE TRIGGER tri_sale_after_insert_detail "
			+ "AFTER INSERT ON sale "
			+ "FOR EACH ROW "
			+ "begin "
			+   "set @saleprice=NEW.price*new.salecnt, "
			+   "@addtax=ceil(NEW.price*new.salecnt/11), "
			+   "@supPrice=@saleprice - @addtax, "
			+   "@marPrice=@supPrice*(new.marginRate/100); "
			+   "INSERT INTO sale_detail(code, sale_price, addTax, supply_price, marginPrice) "
			+   "values (new.code, @saleprice, @addtax, @supPrice, @marPrice);"
			+ "END;";
}
