
package andoffline.web.action.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import andoffline.web.bean.Job;
import andoffline.web.dao.JobDao;
import andoffline.web.database.util.DatabaseConfigurationReader;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Called when the user want add a new JOB in the target database
 *
 */
public class AddJobAction extends ActionSupport {
	
	 private static final long serialVersionUID = 1L;
	  
	 private static final Log log = LogFactory.getLog(AddJobAction.class);
	 
	 //the database type chosen: Sqlite/MySql
	 private String dbmsType;

	 // the message to show in the error.jsp page
	 private String errorMessage;
	 
	 private DatabaseConfigurationReader dbConfigReader = new DatabaseConfigurationReader();
	 
	 private String name;
	 private String interpreter;
	 private String script;
	 private String argument;
	 private String description;
	 
	/**
	 * Constructor
	 */
	public AddJobAction() {		
		
	}
	
	public String execute(){
		return SUCCESS;
	}
	
	/**
	 * Insert a new job in the Database
	 * @return
	 */
	public String addJob(){
		
		try{
			 log.info("Adding new JOB on DBMS: "+dbmsType);			
			
			 dbConfigReader.readDbConfiguration(dbmsType);
			
			 Job job = new Job();				
			 
			 job.setName(name);
			 job.setInterpreter(interpreter);
			 job.setScript(script);
			 job.setDescription(description);
			  
			JobDao jobDao = new JobDao(dbConfigReader);
			jobDao.insertNewJob(dbmsType, job);
		
		}catch (Exception e) {
			 log.fatal("Error adding the new job in the Database: ",e);
			 errorMessage = "Error adding the new job in the Database";
			 
			 return ERROR;
		}
		
		return SUCCESS;	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getArgument() {
		return argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}

}
