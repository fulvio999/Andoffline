
package andoffline.integration.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * Class that contains the creation methods to create tables where store sms, call,contact in a SQLITE Database
 *
 */
public class SqlLiteDatabaseCreator {
	
	 private static final Logger log = Logger.getLogger(SqlLiteDatabaseCreator.class);
	
	 /* SQL script to create the tables 
	  * TODO: move script to external sql files
	  * */	 
	 
	 private static String QUERY_CREATE_SMS_TABLE =    " CREATE TABLE main.sms ( " +
													   " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
													   " protocol INTEGER, " +
													   " phone TEXT, "+
													   " date TEXT," +
													   " type INTEGER, " +
													   " subject TEXT, " +
													   " body TEXT, " +
													   " toa TEXT, " +
													   " sc_toa TEXT, " +
													   " service_center TEXT, " +
													   " smsRead INTEGER, " +
													   " status INTEGER, " +
													   " locked INTEGER );";


      private static String QUERY_CREATE_CALL_TABLE =   " CREATE TABLE main.calls ( " +
													    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
													    " phone TEXT, " +
													    " duration TEXT, " +
													    " call_date TEXT, " +
													    " call_type INTEGER ); ";																						 

		/* 
		  To prevent DB store problem caused by various vcf format the contact are stored in the format key-value
		  the column contactNum is used to group key-value pair that compose the same record 
		*/	
		private static String QUERY_CREATE_CONTACT_TABLE = " CREATE TABLE main.contact (" +
														   " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
														   " contactRecord INTEGER, " +
														   " contactKey TEXT, " +
														   " contactValue TEXT ); ";
		
		/*
		   Create the table with the executable jobs (invoked by SMS) 
		 */
		private static String QUERY_CREATE_JOB_TABLE = " CREATE TABLE main.job (" +
													   " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
													   " name TEXT, " +
													   " interpreter TEXT, " +
													   " interpreterArgument TEXT, "+
													   " script TEXT," +
													   " scriptArgument TEXT," +
													   " description TEXT," +
													   " lastExecutionDate TEXT," +
													   " executorMsisdn TEXT ); ";		

		/**
		* Constructor
		*/
		public SqlLiteDatabaseCreator() {
			
		}
		

		/**
		 * Create the table to store the SMS using a pre-configured sql script
		 * @throws SQLException 
		 */
		public void createSmsTable(String urlString) {
			
			Connection conn = null;
			Statement stmt = null;
			
			try {
				//Driver already loaded
				conn = DriverManager.getConnection(urlString);
				stmt = conn.createStatement();
				stmt.executeUpdate(QUERY_CREATE_SMS_TABLE);
				
				log.info("Sms table created successfully");
			
			}catch (Exception e) {
				log.error("Error creating SMS table, cause:",e);
				
			}finally {
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					
				}				
			}			
		}
		
		
		/**
		 * Create the table to store the CALL using a pre-configured sql script
		 */
		public void createCallTable(String urlString) throws SQLException{
			
			Connection conn = null;
			Statement stmt = null;
			
			try {
				//Driver already loaded
				conn = DriverManager.getConnection(urlString);
				stmt = conn.createStatement();
				stmt.executeUpdate(QUERY_CREATE_CALL_TABLE);
				
				log.info("Call table created successfully");
				
				stmt.close();
				conn.close();
			
			}catch (Exception e) {
				log.error("Error creating SMS table, cause:",e);
				
			}finally {
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					
				}				
			}
		}
		
		
		/**
		 * Create the table to store the CONTACT using a pre-configured sql script
		 */
		public void createContactTable(String urlString) throws SQLException{
			 
			Connection conn = null;
			Statement stmt = null;
			
			try {
				//Driver already loaded
				conn = DriverManager.getConnection(urlString);
				stmt = conn.createStatement();
				stmt.executeUpdate(QUERY_CREATE_CONTACT_TABLE);
				
				log.info("Contact table created successfully");
			
			}catch (Exception e) {
				log.error("Error creating CONTACT table, cause:",e);
				
			}finally {
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					
				}				
			}
		}
		
		/**
		 * Create the table to store the JOB using a pre-configured sql script
		 */
		public void createJobTable(String urlString) throws SQLException{
			
			Connection conn = null;
			Statement stmt = null;
			
			try {
				//Driver already loaded
				conn = DriverManager.getConnection(urlString);
				stmt = conn.createStatement();
				stmt.executeUpdate(QUERY_CREATE_JOB_TABLE);
			
				log.info("Job table created successfully");
			
			}catch (Exception e) {
				log.error("Error creating JOB table, cause:",e);
				
			}finally {
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					
				}				
			}		
	   }

}
