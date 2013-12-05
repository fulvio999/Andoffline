
package andoffline.web.action.job;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import andoffline.web.bean.Job;
import andoffline.web.dao.JobDao;
import andoffline.web.database.util.DatabaseConfigurationReader;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Edit/Delete the job with the provided ID
 *
 */
public class EditJobAction extends ActionSupport {

	 private static final long serialVersionUID = 1L;
	  
	 private static final Log log = LogFactory.getLog(EditJobAction.class);
	 
	 /* The id of the JOB to edit */
	 private String jobId;
	 
	 private Job jobToEdit;
	 
	 //the database type chosen: Sqlite/MySql
	 private String dbmsType;
	
	 private String name;
	 private String interpreter;
	 private String interpreterArgument;
	 private String script;
	 private String scriptArgument;
	 private String description;
	 
	 //Note: the lastExecutionTime field is not editable: is updated at each execution
	 
	 // the message to show in the error.jsp page
	 private String errorMessage;
	 
	 private DatabaseConfigurationReader dbConfigReader = new DatabaseConfigurationReader();

	/**
	 * Constructor
	 */
	public EditJobAction() {
		
	}
	
	public String execute(){
		
		if(StringUtils.isEmpty(jobId)){				
		   errorMessage = "Please, select an item";			 
		   return ERROR;
		} 
		
		try{
			log.info("Getting details for JOB with id: "+jobId+" On DBMS: "+dbmsType);			
			
			dbConfigReader.readDbConfiguration(dbmsType);
			  
			JobDao jobDao = new JobDao(dbConfigReader);
			jobToEdit = jobDao.getJobById(jobId);
		
		}catch (Exception e) {
			 log.fatal("Error loading the stored SMS: ",e);
			 errorMessage = "Error loading the stored JOB from Database";
			 
			 return ERROR;
		}
		
		return SUCCESS;
	}
	
	/**
	 * Method called when the user has confirmed the sms editing
	 * @return
	 */
	public String updateJob(){
		
		try{
			 log.info("Updating the job with id: "+jobId +" On DBMS: "+dbmsType);
			 
			 if(StringUtils.isEmpty(jobId)){
			    errorMessage = "Error, No item selected !";				 
			    return ERROR;
			 }	
			
			 dbConfigReader.readDbConfiguration(dbmsType);
			
			 Job job = new Job();
			 job.setId(jobId);		
			 
			 job.setName(name);
			 job.setInterpreter(interpreter);
			 job.setInterpreterArgument(interpreterArgument);
			 job.setScript(script);
			 job.setScriptArgument(scriptArgument);
			 job.setDescription(description);
			
			 JobDao jobDao = new JobDao(dbConfigReader);
			 jobDao.updateJob(dbmsType,jobId,job);
		
		}catch (Exception e) {
			 log.fatal("Error Updating the JOB with id: "+jobId+ " cause:",e);
             errorMessage = "Error Updating the JOB";
			 
			 return ERROR;
		}
		
		return SUCCESS;		
	}
		
   /**
	* Delete a Job previously stored in the database
	* @return
	*/
   public String deleteJob(){
		
	  try{
		  log.fatal("Deleting the job with id: "+jobId+" On DBMS: "+dbmsType);
		  
		  if(StringUtils.isEmpty(jobId)){
			 errorMessage = "Error, No item selected !";
			 
			 return ERROR;
		  }	 
			
		  dbConfigReader.readDbConfiguration(dbmsType);
			
		  JobDao jobDao = new JobDao(dbConfigReader);
		  jobDao.deleteJob(jobId,dbmsType);
		
		}catch (Exception e) {
		  log.fatal("Error deleting the JOB: ",e);
		  errorMessage = "Error deleting the JOB";
			 
		  return ERROR;
		}
	  
	   return SUCCESS;		
	}

   
   
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getDbmsType() {
		return dbmsType;
	}

	public void setDbmsType(String dbmsType) {
		this.dbmsType = dbmsType;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Job getJobToEdit() {
		return jobToEdit;
	}

	public void setJobToEdit(Job jobToEdit) {
		this.jobToEdit = jobToEdit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(String interpreter) {
		this.interpreter = interpreter;
	}

	public String getInterpreterArgument() {
		return interpreterArgument;
	}

	public void setInterpreterArgument(String interpreterArgument) {
		this.interpreterArgument = interpreterArgument;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getScriptArgument() {
		return scriptArgument;
	}

	public void setScriptArgument(String scriptArgument) {
		this.scriptArgument = scriptArgument;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
