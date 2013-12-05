/**
 * 
 */
package andoffline.web.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.log4j.Logger;

import andoffline.web.database.util.DatabaseConfigurationReader;

/**
 *  Super Dao object with the commons method for the other DAO
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
	   public Connection getConnection(DatabaseConfigurationReader dbConfigReader) throws Exception {		
		 
		      log.info("Connecting with Database type: "+dbConfigReader.getDbmsType());
		      
		      Class.forName(dbConfigReader.getDbDriver());
		      String urlString = null;
		      Connection conn = null;		    
		     
		      if(dbConfigReader.getDbmsType().equalsIgnoreCase("SQLITE")){
		    	
		    	 urlString = "jdbc:sqlite"+":"+dbConfigReader.getDbPath()+File.separator+dbConfigReader.getDbName();
				 log.info("Sqlite jdbc connection url: "+urlString);
			    	
				 //eg: jdbc:sqlite:C:\\SQLite\\test.db
				 conn = DriverManager.getConnection(urlString);								
				 log.debug("Connection with database type: "+dbConfigReader.getDbmsType()+" Name: "+dbConfigReader.getDbName()+" DB Path: "+dbConfigReader.getDbPath()+" established successfully !");
					
				 return conn;
		    	
		     }else if(dbConfigReader.getDbmsType().equalsIgnoreCase("MYSQL")){
		    	
		    	urlString = "jdbc:mysql://" + dbConfigReader.getDbHost() + ":" +dbConfigReader.getDbPort()+"/"+dbConfigReader.getDbName(); 			  
			    conn = DriverManager.getConnection(urlString,dbConfigReader.getDbUser(),dbConfigReader.getDbPassword());	
			    
			    log.debug("Connection with Database Type: "+dbConfigReader.getDbmsType()+" Name: "+dbConfigReader.getDbName()+" Host: "+dbConfigReader.getDbHost()+" Port: "+dbConfigReader.getDbPort()+" established successfully !");
			  
			    return conn;
			    
		     }else {
		    	log.fatal("The database type: "+dbConfigReader.getDbmsType()+" is NOT supported");
		    	return null;
		     }	
        }

}
