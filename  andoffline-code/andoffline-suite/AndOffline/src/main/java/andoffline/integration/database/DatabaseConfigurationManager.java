package andoffline.integration.database;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import andoffline.integration.database.bean.DatabaseConfigurationBean;

/**
 * Object that offer method to Read/Write informations necessary to connect and store data in a DBMS (MySql or Sqlite)
 * The target properties file (ie: database.properties) must be placed in the folder "conf" and named "database.properties"
 *
 * The methods are called from the Menu: Configuration --> Database --> *
 */
public class DatabaseConfigurationManager {	
	
   private static final Logger log = Logger.getLogger(DatabaseConfigurationManager.class);
	
   private static String CONFIG_FILE_NAME = "conf/database.properties";

   // The header to insert at the beginning on the conf/database.properties file
   private static String HEADER = "# Configuration file with the necessary parameters to persist on database the calls, sms ..." + "\n"+
								  "# ----------------------- NOTE -----------------------------" + "\n"+
								  "# ******** DON'T EDIT. USE THE DEDICATED MENU FUNTION !!!! *********"+ "\n"+
								  "# - The JDBC driver for the chosen database must be placed in the lib folder !!" + "\n"+								 
								  "# - The dbPort is not necessary in case of sqlite database" + "\n"+
								  "#"+ "\n"+
								  "#  NOTE: some fields are not necessary for sqlite database (eg host, port...)" + "\n"+
								  "#"+ "\n"+
								  "# - DON'T USE SPACE INSIDE THE NAMES !!!" + "\n"+
								  "# - CURRENTLY THE ONLY DBMS TYPE SUPPORTED ARE SQLITE and MYSQL" + "\n"+
								  "#"+ "\n"+
								  "# ----------------------------------------------------------" + "\n";	

	/**
	 * Constructor
	 */
	public DatabaseConfigurationManager() {
		
	}
	
	
	/**
	 * Utility method that return the configured database name (ie: mysql or sqlite) reading a dedicated property value
	 * in conf/database.properties that is written each time the user set a database configuration. 
	 * @return the currently set target database
	 */
	public static String getConfiguredDatabaseType() throws IOException{
		
		File databaseConfigFile = new File(CONFIG_FILE_NAME);
		FileReader fr = new FileReader(databaseConfigFile.getAbsolutePath());
		Properties databaseConfig = new Properties();
		databaseConfig.load(fr);
				
		return databaseConfig.getProperty("target_db");
	}
	
	/**
	 * Load the Databases configurations stored in conf/database.properties file
	 * 
	 * Called with Menu: Configuration --> Database --> Edit Configuration
	 * 
	 * @param dbType the db type (eg mysql, sqlite) necessary to load the right configuration 
	 * @throws IOException 
	 */
	public DatabaseConfigurationBean loadDatabaseConfiguration(String dbType) throws IOException {
		
		log.info("Reading database configuration for DBMS type: "+dbType);
		
		String db = dbType.toLowerCase();
		
		// Load from a configuration file the database data where store sms, calls... 
	    File databaseConfigFile = new File(CONFIG_FILE_NAME);
		FileReader fr = new FileReader(databaseConfigFile.getAbsolutePath());
		Properties databaseConfig = new Properties();
		databaseConfig.load(fr);
		
		DatabaseConfigurationBean databaseConfigurationBean = new DatabaseConfigurationBean();	
		databaseConfigurationBean.setDbType(db);
		databaseConfigurationBean.setDbDriver(databaseConfig.getProperty(db+"_dbDriver"));
		databaseConfigurationBean.setDbName(databaseConfig.getProperty(db+"_dbName"));
		
		if(db.equalsIgnoreCase("sqlite"))
		   databaseConfigurationBean.setDbPath(databaseConfig.getProperty(db+"_dbPath"));

		databaseConfigurationBean.setDbUser(databaseConfig.getProperty(db+"_dbUser"));
		databaseConfigurationBean.setDbPassword(databaseConfig.getProperty(db+"_dbPassword"));
		databaseConfigurationBean.setDbPort(databaseConfig.getProperty(db+"_dbPort"));
		databaseConfigurationBean.setDbHost(databaseConfig.getProperty(db+"_dbHost"));	
		
		return databaseConfigurationBean;			
	}	

	
	/**
	 * Save a Database configuration in 'conf/database.properties' file
	 * 
	 * Called with Menu: Configuration --> Database --> Create Configuration
	 * or Menu: Configuration --> Database --> Edit Configuration
	 * 
	 * @param dbType the db type (eg mysql, sqlite) chosen in the combo box
	 * @param dbType
	 * @param dbDriver
	 * @param dbName
	 * @param dbUser
	 * @param dbPassword
	 * @param dbPort
	 * @param dbHost
	 * @return true if the operation was executed successfully
	 */
	public boolean saveOrUpdateDatabaseConfiguration(DatabaseConfigurationBean databaseConfigurationBean){
		
		log.debug(" Saving (or updating) DB configuration on 'conf/database.properties' file with values: \n"+
		          " dbType: "+databaseConfigurationBean.getDbType() +"\n"+
		          " dbDriver:"+databaseConfigurationBean.getDbDriver() +"\n"+
		          " dbName:"+databaseConfigurationBean.getDbName() +"\n"+
		          " dbPath:"+databaseConfigurationBean.getDbPath() +"\n"+
		          " dbUser:"+databaseConfigurationBean.getDbUser() +"\n"+
		          " dbPassword:"+databaseConfigurationBean.getDbPassword() +"\n"+
		          " dbPort:"+databaseConfigurationBean.getDbPort() +"\n"+
		          " dbHost:"+databaseConfigurationBean.getDbHost());
		
		try{				
			
			File databaseConfigFile = new File(CONFIG_FILE_NAME);
			//FileReader fr = new FileReader(databaseConfigFile.getAbsolutePath());
			Properties databaseConfig = new Properties();
			
			if(databaseConfigFile.exists()){
			   databaseConfigFile.delete();
			   databaseConfigFile.createNewFile();
			   log.info("Created a new database.properties file");
			}	
				
			
			FileWriter fw = new FileWriter(databaseConfigFile.getAbsolutePath());				
			//fw.write(HEADER);			
			
			if(databaseConfigurationBean.getDbType().equalsIgnoreCase("mysql")){
				
				// overwrite the old properties with the new edited one 
				databaseConfig.setProperty("target_db","mysql");
				databaseConfig.setProperty(databaseConfigurationBean.getDbType()+"_dbName", databaseConfigurationBean.getDbName());
				databaseConfig.setProperty(databaseConfigurationBean.getDbType()+"_dbDriver", databaseConfigurationBean.getDbDriver());
				databaseConfig.setProperty(databaseConfigurationBean.getDbType()+"_dbPort", databaseConfigurationBean.getDbPort());
				databaseConfig.setProperty(databaseConfigurationBean.getDbType()+"_dbPassword", databaseConfigurationBean.getDbPassword());
				databaseConfig.setProperty(databaseConfigurationBean.getDbType()+"_dbUser", databaseConfigurationBean.getDbUser());
				databaseConfig.setProperty(databaseConfigurationBean.getDbType()+"_dbHost", databaseConfigurationBean.getDbHost());
			}			
			
			if(databaseConfigurationBean.getDbType().equalsIgnoreCase("sqlite")) {
				
			   databaseConfig.setProperty("target_db","sqlite");	
			   databaseConfig.setProperty(databaseConfigurationBean.getDbType()+"_dbPath", databaseConfigurationBean.getDbPath());
			   databaseConfig.setProperty(databaseConfigurationBean.getDbType()+"_dbDriver", databaseConfigurationBean.getDbDriver());
			   databaseConfig.setProperty(databaseConfigurationBean.getDbType()+"_dbName", databaseConfigurationBean.getDbName());
			}  
			
			databaseConfig.store(fw, HEADER);		
			fw.close();
			
			return true;
			
		} catch (Exception e) {
			log.fatal("Error saving database configuration file: "+e.getMessage());
			return false;			
		}		
	}
	
}
