package andoffline.integration.database.dto;

/**
 * Bean that represents a JOB database object (ie a job runnable with an incoming SMS) 
 *
 */
public class JobBean {

	private String id;
	private String name;
	private String interpreter;
	private String interpreterArgument;  //eg: -jar  for "java" interpreter
	private String script;
	private String scriptArgument;  //the argument to pass at the script eg: /usr/bin/python /home/john/mysrcipt.py start
	private String description;
	private String executorMsisdn; //the msisdn of the sms sender 
	private String lastExecutionDate;
	
	/**
	 * Constructor
	 */
	public JobBean() {
		
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
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


	public String getExecutorMsisdn() {
		return executorMsisdn;
	}


	public void setExecutorMsisdn(String executorMsisdn) {
		this.executorMsisdn = executorMsisdn;
	}


	public String getLastExecutionDate() {
		return lastExecutionDate;
	}


	public void setLastExecutionDate(String lastExecutionDate) {
		this.lastExecutionDate = lastExecutionDate;
	}


	

}
