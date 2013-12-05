
package andoffline.constant.gui;

/**
 * List of some values displayed in the gui (eg: default values for jdbc connection for MySql or Sqlite) 
 *
 */
public enum GuiParametersEnum {
	
	MYSQL_JDBC_DRIVER("com.mysql.jdbc.Driver"),
	MYSQL_DEFAULT_DB_NAME("andoffline"),
	MYSQL_DEFAULT_DB_USER("root"),
	MYSQL_DEFAULT_DB_PASSWORD(""),
	MYSQL_DEFAULT_DB_PORT("3306"),
	MYSQL_DEFAULT_DB_HOST("localhost"),
	
	
	SQLITE_JDBC_DRIVER("org.sqlite.JDBC"),
	SQLITE_DEFAULT_DB_NAME("andoffline.db"),
	SQLITE_DEFAULT_DB_PATH("/home/fulvio/AndOffline");
	
	
	private String paramValue;
	
	/**
	 * Constructor
	 * @param value
	 */
	private GuiParametersEnum(String value){
		this.paramValue = value;
	}
	
	public String getParamValue() {
		return paramValue;
	}

}
