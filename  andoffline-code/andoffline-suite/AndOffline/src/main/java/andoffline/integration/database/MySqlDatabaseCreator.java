package andoffline.integration.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;


/**
 * Class that contains the creation methods to create tables where store sms, call,contact in a MySQL Database
 *
 */
public class MySqlDatabaseCreator { 
	
	 private static final Logger log = Logger.getLogger(MySqlDatabaseCreator.class);
	
	 /* SQL script to create the tables 
	  * Note: the <schema> place holder inside the String will be replaced with the database name
	  * TODO: move script to external sql files
	  * */	
	
	private static String QUERY_CREATE_SMS_TABLE = "CREATE TABLE <schema>.sms ( " +
													 " id INT NOT NULL AUTO_INCREMENT, " +
													 " phone VARCHAR(50), " +
													 " protocol VARCHAR(50), " +
													 " date VARCHAR(50), " +
													 " type VARCHAR(20), " +
													 " subject VARCHAR(250), " +
													 " body TEXT, " +
													 " toa VARCHAR(25), " +
													 " sc_toa VARCHAR(25),  " +
													 " service_center VARCHAR(25), " +
													 " smsRead VARCHAR(10),  " +
													 " status VARCHAR(10), " +
													 " locked VARCHAR(10), " +
													 " PRIMARY KEY (id)  " +
													 ") " +
													 " ENGINE = InnoDB";	
	
	
	 private static String QUERY_CREATE_CALL_TABLE = "CREATE TABLE <schema>.calls ( " +
													  " id INT NOT NULL AUTO_INCREMENT, " +
													  " phone VARCHAR(25), " +
													  " duration VARCHAR(25), " +
													  " call_date VARCHAR(50), " +
													  " call_type VARCHAR(10), " +
													  " PRIMARY KEY (id) " +
													  ") " +
													  " ENGINE = InnoDB";	
	
	/* 
	   To prevent DB store problem caused by various VCF format the contact are stored in the format key-value
	   the column contactNum is used to group key-value pair that compose the same record (ie the same contact)
	*/	
	private static String QUERY_CREATE_CONTACT_TABLE = "CREATE TABLE <schema>.contact (" +
													   " id INT NOT NULL AUTO_INCREMENT, " +
													   " contactRecord INTEGER," +
													   " contactKey VARCHAR(100)," +
													   " contactValue VARCHAR(100), " +
													   " PRIMARY KEY (id) " +
													   " ) " +
													   " ENGINE = InnoDB";
	
	/*
	   Create the table with the JOB executable with an SMS
	 */
	private static String QUERY_CREATE_JOB_TABLE =   "CREATE TABLE <schema>.job ( " +
													 " id INT NOT NULL AUTO_INCREMENT, " +
													 " name VARCHAR(70), " +
													 " interpreter TEXT, " +
													 " interpreterArgument TEXT, "+
													 " script TEXT, " +
													 " scriptArgument TEXT, " + 
													 " description TEXT, " +
													 " lastExecutionDate VARCHAR(45)," +
													 " executorMsisdn VARCHAR(20), "+
													 " PRIMARY KEY (id)  " +
													 ") " +
													 " ENGINE = InnoDB";
	
	
	
	/**
	 * The creation schema query
	 */
	private static String QUERY_CREATE_SCHEMA = "CREATE DATABASE ";

	
   /**
    * Constructor
    * 
    */
   public MySqlDatabaseCreator(){	 
	   
   }
   
    
   /**
    * Create the schema in the MySql database
    */
   public void createSchema(String dbHost,String dbPort,String dbName,String dbUser,String dbPassword){	
	   
	  Connection connection = null;
	  Statement statement = null;
	   
	  try{
		  String urlString = "jdbc:mysql://" + dbHost + ":" +dbPort; //+"/"+databaseConfigurationBean.getDbName();		    
		
		  connection = DriverManager.getConnection(urlString,dbUser,dbPassword);   
		  
		  String createSchemaQuery = QUERY_CREATE_SCHEMA+dbName;
		  log.info("Creation MySql Schema query: "+createSchemaQuery);	
		  
		  //note: if a schema with the same name already exist is not overwritten
		  statement = connection.createStatement();
		  statement.executeUpdate(createSchemaQuery);
		  log.info("Database created successfully...");
	   	  
	  }catch (Exception e) {
		 log.fatal("Error creating the schema on MySql db, cause: "+e.getMessage());
		 
	  }finally{
		  if(connection !=null){
			 try {
				connection.close();
				statement.close();
			} catch (SQLException e) {
				
			}			 
		  }
	  }
	  
   }
   
   /**
    * Create the table where store the SMS
    */
   public void createSmsTable(Connection connection, String dbName) {
	   
	  Statement statement = null;
	   
	  try{
		  log.info("Creating SMS table in MySql database...");		 
	      statement = connection.createStatement();
	
	      String query = QUERY_CREATE_SMS_TABLE.replaceFirst("<schema>", dbName);	      
	      log.debug("SMS table created, with query: \n"+query);
	      
	      statement.executeUpdate(query);
	   	  //The connection will be closed by the caller
	      
	   }catch (Exception e) {
		   log.fatal("Error creating the SMS table, cause: "+e.getMessage());
		   
	   } finally{
		   try {
			statement.close();
		} catch (SQLException e) {
			
		}
	   }
   }
   
   /**
    * Create the table where store the CALL
    */
   public void createCallTable(Connection connection, String dbName) {
	   
	   Statement statement = null;
	   
	   try{
		   log.info("Creating CALL table in MySql database...");		   
		   statement = connection.createStatement();
		
		   String query = QUERY_CREATE_CALL_TABLE.replaceFirst("<schema>", dbName);
		   log.debug("CALL table created, with query: \n"+query);
		   
		   statement.executeUpdate(query);	   
		     	  
		   //The connection will be closed by the caller
		      
		 }catch (Exception e) {
			 log.fatal("Error creating the CALL table, cause: "+e.getMessage());
			 
		 } finally{
			 try {
				statement.close();
			} catch (SQLException e) {
				
			}	 
		 }
   }
   
   /**
    * Create the table where store the CONTACT
    */
   public void createContactTable(Connection connection, String dbName) {
	   
	   Statement statement = null;
	   
	   try{
		   log.info("Creating CONTACT table in MySql database...");		   
		   statement = connection.createStatement();
		
		   String query = QUERY_CREATE_CONTACT_TABLE.replaceFirst("<schema>", dbName);
		   log.debug("CONTACT table created, with query: \n"+query);
		   
		   statement.executeUpdate(query);		   
		   //The connection will be closed by the caller
		      
		 }catch (Exception e) {
			log.fatal("Error creating the CONTACT table, cause: "+e.getMessage());
			
		 }  finally{
			 try {
				statement.close();
			} catch (SQLException e) {
				
			}	 
		 }
   }
   
   /**
    * Create the table where store the JOB executable with an SMS
    */
   public void createJobTable(Connection connection, String dbName) {
	   
	   Statement statement = null;
	   
	   try{
		   log.info("Creating JOB table in MySql database...");		   
		   statement = connection.createStatement();
		
		   String query = QUERY_CREATE_JOB_TABLE.replaceFirst("<schema>", dbName);
		   log.debug("JOB table created, with query: \n"+query);
		   
		   statement.executeUpdate(query);			   	  
		   //The connection will be closed by the caller
		      
		 }catch (Exception e) {
			log.fatal("Error creating the JOB table, cause: "+e.getMessage());
			
		 } finally{
			 try {
				statement.close();
			} catch (SQLException e) {
				
			}	 
		 }
   }
   
}