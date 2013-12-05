package andoffline.integration.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import andoffline.integration.database.bean.DatabaseConfigurationBean;


/**
 * Offer methods to manage the database Structure (ie: create the Database schema and tables for the chosen database)
 *
 */
public class DatabaseStructureManager {
	
	 private static final Logger log = Logger.getLogger(DatabaseStructureManager.class);
	
	 private MySqlDatabaseCreator mySqlDatabaseCreator; 	 
	 private SqlLiteDatabaseCreator sqlLiteDatabaseCreator;

	/**
	 * Constructor
	 */
	public DatabaseStructureManager() {
		 this.mySqlDatabaseCreator = new MySqlDatabaseCreator();
		 this.sqlLiteDatabaseCreator = new SqlLiteDatabaseCreator();
	}

	/**
	 * Utility method that check if a database configuration was previously save to conf/database.properties file
	 * and all the mandatory parameters are filled for the DB type chosen.
	 * 
	 * @param dbChosen Mysql or Sqlite
	 * @return true if the DB configuration for the chosen DB is saved to file
	 */
	public boolean isDatabaseConfigurationPresent(String dbChosen){
		
		boolean configPresent = false;
		
		try{
			/* load the currently saved Database configuration */
			DatabaseConfigurationManager databaseConfigurationManager = new DatabaseConfigurationManager();
			DatabaseConfigurationBean databaseConfigurationBean = databaseConfigurationManager.loadDatabaseConfiguration(dbChosen);
		
			if(dbChosen.equalsIgnoreCase("SQLITE"))
			{
				/* check if all the required fields are present */
				if(!StringUtils.isEmpty(databaseConfigurationBean.getDbPath()) && !StringUtils.isEmpty(databaseConfigurationBean.getDbName()))
				   configPresent = true;
				else
				   configPresent = false;
				
			}else if(dbChosen.equalsIgnoreCase("MYSQL")){
				
				/* check if all the mandatory fields are present (password can be empty) */
				if(!StringUtils.isEmpty(databaseConfigurationBean.getDbName()) && 
				   !StringUtils.isEmpty(databaseConfigurationBean.getDbHost()) && 
				   !StringUtils.isEmpty(databaseConfigurationBean.getDbUser()) &&				    
				   !StringUtils.isEmpty(databaseConfigurationBean.getDbPort()))
					
				   configPresent = true;
				else
				   configPresent = false;						
			}
			
		}catch (Exception e) {
			log.fatal("Error loading the database configuration for db type: "+dbChosen);
		}
	    
		log.info("Database config for :"+dbChosen+ " present: "+configPresent);
		return configPresent;
	}
	
	
	/**
	 * @param dbChosen Mysql or Sqlite
	 * @return true if everything finished successfully 
	 */
	public boolean createTable(String dbChosen){
		
		 log.info("Creating the schema and tables for db: "+dbChosen);
				   		    
		 Connection conn = null;	
		 Statement statement = null;
		 ResultSet rs = null;
		 
		 try{	
		    DatabaseConfigurationManager databaseConfigurationManager = new DatabaseConfigurationManager();
		    /* load the currently saved Database configuration */
		    DatabaseConfigurationBean databaseConfigurationBean = databaseConfigurationManager.loadDatabaseConfiguration(dbChosen);
				    
			if(dbChosen.equalsIgnoreCase("SQLITE"))
			{				
				//check if the file system path to database (.db) file already, if not create it
				File f = new File(databaseConfigurationBean.getDbPath());
				
				if(!f.exists()){
				   f.mkdirs();
				   log.debug("provided sqlite db path don't exist: created !");
				}
				
				String 	urlString = "jdbc:sqlite"+":"+databaseConfigurationBean.getDbPath()+File.separator+databaseConfigurationBean.getDbName();
			   	log.info("Sqlite jdbc url: "+urlString);
			   	
			    Class.forName(databaseConfigurationBean.getDbDriver());
				//eg: jdbc:sqlite:C:\\SQLite\\EMPLOYEE.db			
			    		
				log.info("Connection with database: "+databaseConfigurationBean.getDbName()+" Host: "+databaseConfigurationBean.getDbHost()+" established successfully !");
						
				//NOTE: if the db already exist Sqlite re-use it. If don't exist create it			
				this.createSqliteTable(urlString);
				    	
			}else if(dbChosen.equalsIgnoreCase("MYSQL")){
				    	
			    Class.forName(databaseConfigurationBean.getDbDriver());
			    
				if(schemaAlreadyExist(databaseConfigurationBean.getDbHost(),databaseConfigurationBean.getDbPort(),databaseConfigurationBean.getDbName(),databaseConfigurationBean.getDbUser(),databaseConfigurationBean.getDbPassword())){
					
					log.info("Schema "+databaseConfigurationBean.getDbName()+" already exist, creating only tables");					    
					this.createMySqlTables(databaseConfigurationBean.getDbHost(),databaseConfigurationBean.getDbPort(),databaseConfigurationBean.getDbName(),databaseConfigurationBean.getDbUser(),databaseConfigurationBean.getDbPassword());	
					    	
				}else{
					 log.info("Schema "+databaseConfigurationBean.getDbName()+" DON'T exist");		    	
					 
					 this.createMysqlSchema(databaseConfigurationBean.getDbHost(),databaseConfigurationBean.getDbPort(),databaseConfigurationBean.getDbName(),databaseConfigurationBean.getDbUser(),databaseConfigurationBean.getDbPassword());
					 this.createMySqlTables(databaseConfigurationBean.getDbHost(),databaseConfigurationBean.getDbPort(),databaseConfigurationBean.getDbName(),databaseConfigurationBean.getDbUser(),databaseConfigurationBean.getDbPassword());   	
				} 				
							
			 } 
				    
	     }catch (Exception e) {
			log.fatal("Error creating the DB and/or tables, cause: ",e);
			return false;
			
		} finally{
			try{				
				statement.close();			   
				conn.close();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}		 
		return true;
	 }  
	
      /**
       * Create the the MySql schema	
       * @param connection
       * @param dbName
       */
	   private void createMysqlSchema(String dbHost,String dbPort,String dbName,String dbUser,String dbPassword){
	       mySqlDatabaseCreator.createSchema(dbHost,dbPort,dbName,dbUser,dbPassword);
	   }
	
	   
	   /**
	    * Utility methods that checks if the mysql schema already exist or not
	    * @param connectionUrl the jdbc connection url
	    * @return true if already exist
	    * @throws SQLException 
	    */
	   private boolean schemaAlreadyExist(String dbHost,String dbPort,String dbName,String dbUser,String dbPassword){
		
		   Connection connection = null;
		   Statement statement = null;
		   ResultSet rs = null;
		   
		   String urlString = "jdbc:mysql://" + dbHost + ":" +dbPort; //+"/"+databaseConfigurationBean.getDbName();
		   
		   try{			 
			  connection = DriverManager.getConnection(urlString,dbUser,dbPassword);			   
			  log.info("Connection with database name: "+urlString+" established successfully !");

			  statement = connection.createStatement();			
			  rs = statement.executeQuery("SELECT * FROM information_schema.SCHEMATA where schema_name ='"+dbName+"'"); 

			  if(rs.getRow() >0 ){				
				log.info("Schema "+dbName+" already exist, creating only tables");
				return true;
			  }else
				return false;
			
		   }catch (Exception e) {
			  log.fatal("Error connecting with: "+urlString);
			  return false;
			  
		   }finally{
			   if(connection != null){
				 try{
					statement.close();
					connection.close();
				 }catch (SQLException e) {
					
				 }
			   }
		   }
	   }
	   
	   /**
		 * Create the tables in the chosen MySQL database 
		 * @param confReader The object with the necessary data to connect with the previously chosen database
		 */ 	  
	   private void createMySqlTables(String dbHost,String dbPort,String dbName,String dbUser,String dbPassword){
				
		    String urlString = "jdbc:mysql://" + dbHost + ":" +dbPort; //+"/"+databaseConfigurationBean.getDbName();		    
		    Connection connection = null;
		    
		    try {	
		    	 connection = DriverManager.getConnection(urlString,dbUser,dbPassword);		   
								
				 mySqlDatabaseCreator.createSmsTable(connection,dbName);
				 mySqlDatabaseCreator.createCallTable(connection,dbName);
				 mySqlDatabaseCreator.createContactTable(connection,dbName);
				 mySqlDatabaseCreator.createJobTable(connection,dbName);							 
				
				}catch (SQLException e) {
				   log.error("Error creating mysql tables, cause: "+e.getMessage());
				   
				}finally{
					if(connection !=null){
						try{
						  connection.close();
						}catch (SQLException e) {
							
						}
					}	
				}
		}
					 
		/**
		  * Create the tables in the chosen SQLITE database 		  * 
		  * @param confReader The object with the necessary data to connect with the previously chosen database
		  * @throws SQLException 
		 */ 
		private void createSqliteTable(String urlString) throws SQLException{
		
		      sqlLiteDatabaseCreator.createSmsTable(urlString);
		      sqlLiteDatabaseCreator.createCallTable(urlString);
		      sqlLiteDatabaseCreator.createContactTable(urlString);
		      sqlLiteDatabaseCreator.createJobTable(urlString);
		}				    
				    

}
