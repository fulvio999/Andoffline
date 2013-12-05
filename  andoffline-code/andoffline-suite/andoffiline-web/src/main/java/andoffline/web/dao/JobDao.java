
package andoffline.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.beanutils.RowSetDynaClass;
import org.apache.log4j.Logger;

import andoffline.web.bean.Job;
import andoffline.web.database.util.DatabaseConfigurationReader;


/**
 * DAO object that offer the methods to manage the "job" database object
 *
 */
public class JobDao extends BaseDao {
	
	 private static final Logger log = Logger.getLogger(JobDao.class);	
	 
	 private DatabaseConfigurationReader dbConfigReader;
	
	 private static String GET_ALL_JOB_QUERY = "select * from job";	 
	 private static String DELETE_JOB_QUERY = "delete from job where id = ?";	 
	 private static String INSERT_JOB_QUERY = "insert into job (name,interpreter,interpreterArgument,script,scriptArgument,description) values (?,?,?,?,?,?)";	 	 
	 private static String UPDATE_JOB_QUERY = "UPDATE job SET name=?,interpreter=?,interpreterArgument=?,script=?,scriptArgument=?,description=? WHERE id= ?";

	 
	/*
 	 * Constructor
 	 */
 	public JobDao(DatabaseConfigurationReader dbConfigReader){   		  
 		this.dbConfigReader = dbConfigReader;
 	}
	
 	 /**
	   * Load form the database the SMS with the Id in argument
	   *  
	   * @param jobId The id of the sms to load
	   * @return
	   */
	  public Job getJobById(String jobId){
		  
		  log.debug("Loading JOB with id: "+jobId);
		     
		  Connection connection = null;	
		  PreparedStatement stmt = null;
		  ResultSet rs = null;
		   
		  Job job = null;
			 
		  try{		
			   String query = "SELECT * FROM job where id =?";			   
			 
			   connection = this.getConnection(this.dbConfigReader);

			   stmt = connection.prepareStatement(query);
			   stmt.setString(1, jobId);
			   
			   log.debug("Query for search job by id: "+stmt);
				 
			   rs = stmt.executeQuery();				 
			   job = new Job();
				 
			   if (rs.next())
			   {
				 job.setName(rs.getString("name"));
				 job.setInterpreter(rs.getString("interpreter"));
				 job.setInterpreterArgument(rs.getString("interpreterArgument"));
				 job.setScript(rs.getString("script"));
				 job.setScriptArgument(rs.getString("scriptArgument"));
				 job.setDescription(rs.getString("description"));
				 job.setLastExecutionDate("lastExecutionDate");
				 job.setExecutorMsisdn("executorMsisdn");
					 
			   }else{
				  log.info("No JOB found with id: "+jobId);
				  return null;
			   }				 
			
			}catch (Exception e){				
				log.fatal("Error searching JOB cause: ",e);					
				return null;
				
			}finally{
				
				try {
					  if(stmt != null)
						 stmt.close();
					  
					  if(connection != null)
						 connection.close();
					  
					}catch (SQLException e) {
						log.fatal("Error closing DB connection: "+e.toString());
					}
			}		     
		return job;
		  
	  }	 
 	
 	
	/**
	 * Load ALL the currently stored jobs in the database
	 * @return A list of JobBean
	 * @throws Exception 
	 */
	public List getAllStoredJob() throws Exception {		
		
		Statement statement = null;
		Connection connection = null;
		
		try{			
			 RowSetDynaClass resultSet = null;			 
			 connection = this.getConnection(this.dbConfigReader);			

			 statement = connection.createStatement();
			 ResultSet rs = statement.executeQuery(GET_ALL_JOB_QUERY);
			 
			 //use BasicDynaBean so that is not necessary use a DTO
			 resultSet = new RowSetDynaClass(rs, false);
			 rs.close();
			 
			 log.debug("Found Total JOB: "+resultSet.getRows().size());
			 
			 return resultSet.getRows();
		
		}catch (Exception e) {
		   log.fatal("Error retrieving job from the db, cause: ",e);
		   throw e;
			
		}finally {			
			try {
				statement.close();
				connection.close();
			}catch (SQLException e) {
			   log.fatal("Error closing DB connection: "+e.getMessage());
			}
		}		
	   
	}
	
	
	/**
	 * Insert a new job in the database
	 * @param dbChosen The DB type where insert the job (ie: mysql, sqlite)
	 * @param job The new job to insert
	 * @throws Exception 
	 */
	public void insertNewJob(String dbChosen,Job jobBean) throws Exception{
		
		 log.info("Inserting a new job in the database type: "+dbChosen);
		 
		 PreparedStatement ps = null;
		 Connection connection = null;
			
		 try{		 
			  connection = this.getConnection(this.dbConfigReader);				
	 
			  ps = connection.prepareStatement(INSERT_JOB_QUERY);
			  ps.setString(1, jobBean.getName());
			  ps.setString(2, jobBean.getInterpreter());
			  ps.setString(3, jobBean.getInterpreterArgument());			  
			  ps.setString(4, jobBean.getScript());
			  ps.setString(5, jobBean.getScriptArgument());
			  ps.setString(6, jobBean.getDescription());
			  //the fields 'executorMsisdn' and 'lastExecutionDate'are updated at each job execution with a SMS command
			
		      int updatedRow = ps.executeUpdate();
				
			  log.debug("Inserted "+updatedRow+ " row");
			
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
	 * Update an existing job. Method called from the WEB console
	 * ('executorMsisdn' and 'lastExecutiondate' fields are updated by java desktop application when the job is executed)
	 * 
	 * @param jobId The Primary key vale of the job to update 
	 * @param newJobBean The bean representing the updated data of the Job
	 * @throws Exception 
	 */
	public void updateJob(String dbChosen, String jobId, Job updatedJobBean) throws Exception{
		
		 log.info("Updating job with ID: "+jobId+" in the database type: "+dbChosen);
		 
		 PreparedStatement ps = null;
		 Connection connection = null;
			
		 try{			    	
			  connection = this.getConnection(this.dbConfigReader);
		
			  ps = connection.prepareStatement(UPDATE_JOB_QUERY);	
			  ps.setString(1, updatedJobBean.getName());
			  ps.setString(2, updatedJobBean.getInterpreter());
			  ps.setString(3, updatedJobBean.getInterpreterArgument());
			  
			  ps.setString(4, updatedJobBean.getScript());
			  ps.setString(5, updatedJobBean.getScriptArgument());
			  ps.setString(6, updatedJobBean.getDescription());
			  ps.setString(7, jobId);	
			  
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
			connection = this.getConnection(this.dbConfigReader);
			
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
