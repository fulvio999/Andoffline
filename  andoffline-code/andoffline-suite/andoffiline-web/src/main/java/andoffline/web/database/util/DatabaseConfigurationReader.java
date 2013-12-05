package andoffline.web.database.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Utility Class that read the necessary values to connect with the database
 * 
 * It look for a file named "database-location.properties" placed on folder:
 * <installation-folder>/AndOffline/web-app/apache-tomcat/conf/andoffline 
 * 
 * where <installation-folder> is chose during the installation process and is set by Izpack installer
 * 
 * This file contains a key named "database.config.file.path" that indicates the real properties file (ie database.properties) with configured account
 * for DB.
 * This value <installation-folder> is set by IZPACK during installation process because depends on the choice of the user
 * 
 * The file "database.properties" is written by the Swing application
 */
public class DatabaseConfigurationReader {
	
	private static final Logger log = Logger.getLogger(DatabaseConfigurationReader.class);	
	
	private static final String FILE_PATH = File.separator+"conf"+File.separator+"andoffline"+File.separator+"database-location.properties";

	//environment variable set by Tomcat during the startup
	private static final String BASE_DIR = "catalina.base";
	
	private String dbmsType;  //Mysql or Sqlite
	private String dbHost;
	private String dbName;
	private String dbPort;
	private String dbUser;
	private String dbPassword;
	private String dbDriver;
	
	private String dbPath; // used only for sqlite
	
	/**
	 * Constructor
	 */
	public DatabaseConfigurationReader() {
		
	}

	
	/**
	 * Read The necessary values from a properties file to connect with the right database. 
	 * 	 
	 * @param  dbmsType The Database type (ie: Mysql or Sqlite)
	 */
	public void readDbConfiguration(String dbmsType) throws IOException{		

		log.info("Looking for DB connection parameters for DBMS: "+dbmsType);
		this.dbmsType = dbmsType;
		
		String path = System.getProperty(BASE_DIR);
		path += FILE_PATH;
		
		Properties p = new Properties();
		p.load(new FileInputStream(path));
		String dbConfigFileLocation = p.getProperty("database.config.file.path");
		
		log.info("Location of database.properties file: "+dbConfigFileLocation);		

		Properties dbConfig = new Properties();
		//to read from a file outside the war
		dbConfig.load(new FileInputStream(dbConfigFileLocation));
		
		/* read connection parameters according with the database type */
		if(dbmsType.equalsIgnoreCase("Mysql"))
		{		
			dbHost = dbConfig.getProperty(dbmsType.toLowerCase()+"_dbHost");
			dbName = dbConfig.getProperty(dbmsType.toLowerCase()+"_dbName");
			dbPort = dbConfig.getProperty(dbmsType.toLowerCase()+"_dbPort");
			dbUser = dbConfig.getProperty(dbmsType.toLowerCase()+"_dbUser");
			dbPassword = dbConfig.getProperty(dbmsType.toLowerCase()+"_dbPassword");
			dbDriver = dbConfig.getProperty(dbmsType.toLowerCase()+"_dbDriver");
			
		}else if(dbmsType.equalsIgnoreCase("sqlite")) {
			
		   dbName = dbConfig.getProperty(dbmsType.toLowerCase()+"_dbName");	
		   dbPath = dbConfig.getProperty(dbmsType.toLowerCase()+"_dbPath");
		   dbDriver = dbConfig.getProperty(dbmsType.toLowerCase()+"_dbDriver");
		}		
		
		log.trace("Database Host: "+dbHost);
		log.trace("Database Name: "+dbName);
		log.trace("Database Port: "+dbPort);
		log.trace("Database Url: "+dbUser);
		log.trace("Database Password: "+dbPassword);
		log.trace("Driver"+dbDriver);			
	}
	
	
	public String getDbHost() {
		return dbHost;
	}


	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}


	public String getDbName() {
		return dbName;
	}


	public void setDbName(String dbName) {
		this.dbName = dbName;
	}


	public String getDbPort() {
		return dbPort;
	}


	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
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

	public String getDbDriver() {
		return dbDriver;
	}


	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}


	public String getDbPath() {
		return dbPath;
	}


	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}


	public String getDbmsType() {
		return dbmsType;
	}


	public void setDbmsType(String dbmsType) {
		this.dbmsType = dbmsType;
	}


}