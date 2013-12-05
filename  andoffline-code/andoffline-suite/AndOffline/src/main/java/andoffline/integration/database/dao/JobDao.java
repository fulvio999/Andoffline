
package andoffline.integration.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import andoffline.integration.database.DatabaseConfigurationManager;
import andoffline.integration.database.dto.JobBean;

/**
 * DAO object that offer the methods to manage the "job" database object
 *
 */
public class JobDao extends BaseDao{
	
	 private static final Logger log = Logger.getLogger(JobDao.class);	 
	
	 private static String GET_ALL_JOB_QUERY = "select * from job";	 
	 private static String GET_JOB_BY_ID_QUERY = "select * from job where id =?";	 
	 private static String DELETE_JOB_QUERY = "delete from job where id = ?";	
	 /* 'lastExecutionDate' field is left blank, will be update at each job execution */
	 private static String INSERT_JOB_QUERY = "insert into job (name,interpreter,interpreterArgument,script,scriptArgument,description) values (?,?,?,?,?,?)";	 
	 private static String UPDATE_JOB_QUERY = "UPDATE job SET name=?,interpreter=?,interpreterArgument=?,script=?,scriptArgument=?,description=?,executorMsisdn=? WHERE id= ?";
	 private static String UPDATE_JOB_EXECUTION = "UPDATE job SET executorMsisdn=?,lastExecutionDate=? WHERE id= ?";

	 
	/**
	 * Constructor
	 */
	public JobDao() {
		
	}
	
	/**
	 * Load from the database the job whose id is in argument
	 * @param jobId The id of the job to load 
	 * @return A {@link JobBean} representing the job or an empty JobBean if no job found with the provided id
	 * @throws Exception 
	 */
	public JobBean loadJobById(String jobId) throws Exception{
		
		log.info("Loading from DataBase job with id: "+jobId);
		
		PreparedStatement ps = null;
		Connection connection = null;
		
		// bean that represents a table row
		JobBean jobBean = new JobBean();
		
		try{
			String targetdatabase = DatabaseConfigurationManager.getConfiguredDatabaseType();
			 
			connection = this.getConnection(targetdatabase);
			ps = connection.prepareStatement(GET_JOB_BY_ID_QUERY);
			ps.setString(1, jobId);
			ResultSet queryResultset = ps.executeQuery();
			
			if(queryResultset.isBeforeFirst() == false){
				log.fatal("NO job found in the database with id= "+jobId);
				return jobBean;
			}
				
			while (queryResultset.next()) 
			{			 
				 String jobIdColumn = queryResultset.getString("id");
				 String jobName= queryResultset.getString("name");
				 
				 //eg: bash, python,... (or full path to it if the command is not in the PATH)
				 String interpreter = queryResultset.getString("interpreter"); 
				 String interpreterArgument = queryResultset.getString("interpreterArgument"); 
				 
				 //the full path at the script path to pass as argument at the interpreter (eg: /home/john/mysript.py)
				 String script = queryResultset.getString("script"); 
				 String scriptArgument = queryResultset.getString("scriptArgument");
				 String description = queryResultset.getString("description");	
				 String lastExecutionDate = queryResultset.getString("lastExecutionDate"); 
				 String executorMsisdn = queryResultset.getString("executorMsisdn");
				 
				 jobBean.setId(jobIdColumn);
				 jobBean.setName(jobName);
				 jobBean.setInterpreter(interpreter);
				 jobBean.setInterpreterArgument(interpreterArgument);
				 jobBean.setScript(script);
				 jobBean.setScriptArgument(scriptArgument);
				 jobBean.setDescription(description);		
				 jobBean.setLastExecutionDate(lastExecutionDate);		
				 jobBean.setExecutorMsisdn(executorMsisdn);
			}
		
		}catch (Exception e) {
		   log.fatal("Error retrieving data form the db, cause: ",e);
		   throw e;
			
		}finally {			
			try {
				ps.close();
				connection.close();
			}catch (SQLException e) {
			   log.fatal("Error closing DB connection: "+e.getMessage());
			}
		}		
	    return jobBean;		
	}
	
	
	/**
	 * Load ALL the currently stored jobs in the database type in argument
	 * @return A list of JobBean
	 * @throws Exception 
	 */
	public ArrayList<JobBean> loadAllJobs(String dbChosen) throws Exception {
		
		log.info("Loading Jobs from DataBase: "+dbChosen);
		ArrayList<JobBean> jobBeanList = new ArrayList<JobBean>();
		
		Statement statement = null;
		Connection connection = null;
		
		try{			
			connection = this.getConnection(dbChosen);
			statement = connection.createStatement();
			ResultSet queryResultset = statement.executeQuery(GET_ALL_JOB_QUERY);
			
			//fill the bean list using the result-set
			while (queryResultset.next()) 
			{			 
				 String jobId = queryResultset.getString("id");
				 String jobName = queryResultset.getString("name"); 
				 String interpreter = queryResultset.getString("interpreter"); //eg: bash, python
				 String interpreterArgument = queryResultset.getString("interpreterArgument"); // -jar
				 String script = queryResultset.getString("script");
				 String scriptArgument = queryResultset.getString("scriptArgument");
				 String descriptionColumn = queryResultset.getString("description");	
				 String lastExecutionDateColumn = queryResultset.getString("lastExecutionDate"); 
				 String executorMsisdn = queryResultset.getString("executorMsisdn");
							 
				 // bean that represents a table row
				 JobBean jobBean = new JobBean();
				 
				 jobBean.setId(jobId);
				 jobBean.setName(jobName);
				 jobBean.setInterpreter(interpreter);
				 jobBean.setInterpreterArgument(interpreterArgument);
				 jobBean.setScript(script);
				 jobBean.setScriptArgument(scriptArgument);
				 jobBean.setDescription(descriptionColumn);		
				 jobBean.setLastExecutionDate(lastExecutionDateColumn);		
				 jobBean.setExecutorMsisdn(executorMsisdn);
				 
				 log.trace("** -id:"+jobId+" -Name:"+jobName+" -Interpreter: "+interpreter+" -InterpreterArgument:"+interpreterArgument +" -Script: "+script+" -ScriptArgument: "+scriptArgument+" -Description: "+descriptionColumn+" -executorMsisdn:"+executorMsisdn);
				 jobBeanList.add(jobBean);
			}
		
		}catch (Exception e) {
		   log.fatal("Error retrieving data form the db, cause: ",e);
		   throw e;
			
		}finally {			
			try {
				statement.close();
				connection.close();
			}catch (SQLException e) {
			   log.fatal("Error closing DB connection: "+e.getMessage());
			}
		}		
	    return jobBeanList;
	}
	
	
	/**
	 * Insert a new job in the database
	 * @param dbChosen The DB type where insert the job (ie: mysql, sqlite)
	 * @param jobBean The new job to insert
	 * @throws Exception 
	 */
	public void insertNewJob(String dbChosen,JobBean jobBean) throws Exception {
		
		 log.info("Inserting a new job in the database type: "+dbChosen);
		 
		 PreparedStatement ps = null;
		 Connection connection = null;
		 
		 try{			    	
			  connection = this.getConnection(dbChosen);
			  ps = connection.prepareStatement(INSERT_JOB_QUERY);
			  ps.setString(1, jobBean.getName());
			  ps.setString(2, jobBean.getInterpreter());
			  ps.setString(3, jobBean.getInterpreterArgument());			  
			  ps.setString(4, jobBean.getScript());
			  ps.setString(5, jobBean.getScriptArgument()); 
			  ps.setString(6, jobBean.getDescription());
			
		      int updatedRow = ps.executeUpdate();
				
			  log.debug("Inserted "+updatedRow+ " (job) row");
			
			}catch (Exception e) {
				log.fatal("Error inserting Job in the "+dbChosen+" cause: "+e.getMessage());
				throw e;
				
			}finally {
				try {
					ps.close();
					
					if(connection != null)
					   connection.close();
					
				}catch (SQLException e) {
				   log.fatal("Error closing connection: ",e);
				}
			}	
	 }
	
	/**
	 * Update a job msisdn field inserting the last one that has executed it; also update his execution date.
	 * @param jobId
	 * @param executorMsisdn
	 * @param executionTime  The date and time when the sms was RECEIVED by the phone running 'AndOfflineApp' 
	 * @throws Exception 
	 */
	public void updateJobExecutionInfo(String jobId, String executorMsisdn, String executionTime) throws Exception {
	
         log.info("Updating job with ID: "+jobId+" with executor msisdn: "+executorMsisdn+ " execution time: "+executionTime);
		 
		 PreparedStatement ps = null;
		 Connection connection = null;
			
		 try{	
			  String targetdatabase = DatabaseConfigurationManager.getConfiguredDatabaseType();
			 
			  connection = this.getConnection(targetdatabase);
			  ps = connection.prepareStatement(UPDATE_JOB_EXECUTION);
			  ps.setString(1,executorMsisdn);
			  ps.setString(2,executionTime);			 
			  ps.setString(3,jobId);	
			
		      int updatedRow = ps.executeUpdate();
				
			  log.debug("Updated "+updatedRow+ " row");
			
			}catch (Exception e) {
				log.fatal("Error updating Job execution info with id: "+jobId+" cause: "+e.toString());
				throw e;
				
			}finally {
				try {
					ps.close();
					
					if(connection != null)
					   connection.close();
					
				}catch (SQLException e) {
				   log.fatal("Error closing connection: ",e);
				}
			}
		
	}
	
	/**
	 * Update an existing job
	 * 
	 * @param jobId The Primary key vale of the job to update 
	 * @param newJobBean The bean representing the updated data of the Job
	 * @throws Exception 
	 */
	public void updateJob(String dbChosen, String jobId, JobBean updatedJobBean) throws Exception{
		
		 log.info("Updating job with ID: "+jobId+" in the database type: "+dbChosen);
		 
		 PreparedStatement ps = null;
		 Connection connection = null;
			
		 try{				    	
			  connection = this.getConnection(dbChosen);
			  
			  ps = connection.prepareStatement(UPDATE_JOB_QUERY); 
			  ps.setString(1, updatedJobBean.getName());
			  ps.setString(2, updatedJobBean.getInterpreter());
			  ps.setString(3, updatedJobBean.getInterpreterArgument());
			  
			  ps.setString(4, updatedJobBean.getScript());
			  ps.setString(5, updatedJobBean.getScriptArgument());
			  ps.setString(6, updatedJobBean.getDescription());
			  ps.setString(7, updatedJobBean.getExecutorMsisdn());
			  ps.setString(8, jobId);	
			  
			  /* Note: the field 'Last Execution' is set to blank, because the new updated job is never executed yet */
			
		      int updatedRow = ps.executeUpdate();
				
			  log.debug("Updated "+updatedRow+ " row");
			
			}catch (Exception e) {
				log.fatal("Error updating Job with id: "+jobId+" cause: "+e.getMessage());
				throw e;
				
			}finally {
				try {
					ps.close();
					
					if(connection != null)
					   connection.close();
					
				}catch (SQLException e) {
				   log.fatal("Error closing connection: ",e);
				}
			}		
	}
	
	
   /**
    * Delete a Job from the database	
    * @param jobId The id of the job to delete
    * @param dbChosen The chosen database type (ie: mysql or sqlite)
    * @throws Exception 
    */
   public void deleteJob(String jobId,String dbChosen) throws Exception {
	   
	    log.info("Deleting job with id: "+jobId+" from database type: "+dbChosen);		
		
		PreparedStatement ps = null;
		Connection connection = null;
		
		try{				
			connection = this.getConnection(dbChosen);
			ps = connection.prepareStatement(DELETE_JOB_QUERY);
			ps.setString(1,jobId);
			int updatedRow = ps.executeUpdate();
			log.info("Deleted "+updatedRow+ " row");
			
		}catch (Exception e) {
			log.fatal("Error deleting Job in the "+dbChosen+" cause: "+e.getMessage());
			throw e;
			
		}finally {
			try {
				ps.close();
				
				if(connection != null)
				   connection.close();
				
			}catch (SQLException e) {
				log.fatal("Error closing connection: ",e);
			}
		}
   }		 
 
}
