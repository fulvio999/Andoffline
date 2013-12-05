package andoffline.web.action;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import andoffline.web.dao.JobDao;
import andoffline.web.database.util.DatabaseConfigurationReader;

import com.opensymphony.xwork2.ActionSupport;

/*
 * Action that load the jobs stored in the configured database
 */
public class GetStoredContentAction extends ActionSupport {
 
  private static final long serialVersionUID = 1L;
  
  private static final Log log = LogFactory.getLog(GetStoredContentAction.class);
  
  private List jobList;
  
  //the chosen database type chosen: Sqlite/MySql
  private String dbmsType;
  
  // the content to show: sms or job
  private String contentType;
  
  //the message to show in the error.jsp page
  private String errorMessage;
  
  
  public String execute()  
  {
	  try{
		  log.info("Loading the stored "+contentType+" from DBMS type: "+dbmsType);
		  
		  DatabaseConfigurationReader dbConfigReader = new DatabaseConfigurationReader();
		  dbConfigReader.readDbConfiguration(dbmsType.toLowerCase());
		  
		  if(contentType.equalsIgnoreCase("job")){
			  
			  JobDao jobDao = new JobDao(dbConfigReader);
			  jobList = jobDao.getAllStoredJob();
			  
			  if(jobList == null){
				 errorMessage = "No job found (Chosen database type is correct ?)";				 
				 return ERROR;	 
			  }				  
			  return "showStoredJob";
			  
		   }else {			   
			   errorMessage = "wrong content type (must be sms of job)";				 
			   return ERROR;
		   }			   
		  
	  }catch (Exception e) {
		 log.fatal("Error loading the stored JOB: ",e);
		 errorMessage = "Error loading the stored JOB, see log file for details";
		 return ERROR;
	  }	
	  
   }
  

	public String getDbmsType() {
		return dbmsType;
	}


	public void setDbmsType(String dbmsType) {
		this.dbmsType = dbmsType;
	}


	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public List getJobList() {
		return jobList;
	}


	public void setJobList(List jobList) {
		this.jobList = jobList;
	}
	
 
}
