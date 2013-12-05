
package andoffline.integration.database.dao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.log4j.Logger;

import andoffline.integration.database.DatabaseConfigurationManager;
import andoffline.integration.database.bean.DatabaseConfigurationBean;

/**
 * Super Dao object with the commons method for the other DAO
 *
 */
public class BaseDao {
	
	private static final Logger log = Logger.getLogger(BaseDao.class);	

	 /**
	  * Constructor
	  */
	  public BaseDao() {
		
	  }
	
	  /**
	   * Return a valid JDBC connection to the right database
	   * @return
	   * @throws Exception 
	   */
	   public Connection getConnection(String dbChosen) throws Exception {		
		 
		      log.info("Connecting with Database type: "+dbChosen);
		
		      DatabaseConfigurationManager dbConfManager = new DatabaseConfigurationManager();
		      DatabaseConfigurationBean databaseConfigurationBean = dbConfManager.loadDatabaseConfiguration(dbChosen);
		      
		      Class.forName(databaseConfigurationBean.getDbDriver());
		      String urlString = null;
		      Connection conn = null;		    
		     
		      if(dbChosen.equalsIgnoreCase("SQLITE")){
		    	
		    	 urlString = "jdbc:sqlite"+":"+databaseConfigurationBean.getDbPath()+File.separator+databaseConfigurationBean.getDbName();
				 log.info("Sqlite jdbc connection url: "+urlString);
			    	
				 //eg: jdbc:sqlite:C:\\SQLite\\test.db
				 conn = DriverManager.getConnection(urlString);								
				 log.debug("Connection with database type: "+dbChosen+" Name: "+databaseConfigurationBean.getDbName()+" Host: "+databaseConfigurationBean.getDbHost()+" established successfully !");
					
				 return conn;
		    	
		     }else if(dbChosen.equalsIgnoreCase("MYSQL")){
		    	
		    	urlString = "jdbc:mysql://" + databaseConfigurationBean.getDbHost() + ":" +databaseConfigurationBean.getDbPort()+"/"+databaseConfigurationBean.getDbName(); 			  
			    conn = DriverManager.getConnection(urlString,databaseConfigurationBean.getDbUser(),databaseConfigurationBean.getDbPassword());	
			    
			    log.debug("Connection with Database Type: "+dbChosen+" Name: "+databaseConfigurationBean.getDbName()+" Host: "+databaseConfigurationBean.getDbHost()+" established successfully !");
			  
			    return conn;
			    
		     }else {
		    	log.fatal("The database type: "+dbChosen+" is NOT supported");
		    	return null;
		     }	
        }	

   }
