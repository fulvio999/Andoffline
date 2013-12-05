
package andoffline.integration.database.bean;

/**
 * Bean that represents a database configuration stored in the conf/database.properties file
 *
 */
public class DatabaseConfigurationBean {	
	
	private String dbType; //Mysql, sqlite
	private String dbDriver;
	private String dbName;
	
	//used only for sqlite
	private String dbPath;
	
	private String dbUser;
	private String dbPassword;	
	private String dbPort;
	private String dbHost;	

	/**
	 * Constructor
	 */
	public DatabaseConfigurationBean() {
		
	}

	
	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbPath() {
		return dbPath;
	}

	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}


	public String getDbType() {
		return dbType;
	}


	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	

}
