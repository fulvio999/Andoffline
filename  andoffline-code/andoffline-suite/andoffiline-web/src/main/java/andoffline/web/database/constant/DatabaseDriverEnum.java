
package andoffline.web.database.constant;

/**
 * Contains the list of JDBC driver url for the supported databases
 *
 */
public enum DatabaseDriverEnum {
	
	MYSQL("com.mysql.jdbc.Driver"), SQLITE("org.sqlite.JDBC");
	
	private String driver;
	
    
	private DatabaseDriverEnum(String d) {
	  driver = d;
	}
		 
	public String getDriver() {
	   return driver;
	}  

}
