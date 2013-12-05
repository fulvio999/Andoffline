
package andoffline.integration.phone.command.validator;

/**
 * List all the possible values that can be placed as <argument> tokens
 * in the text of the sms text to be processed 
 *
 */
public enum ArgumentEnum {
		
    start("start","start the embedded server"),
    stop("stop","stop the embedded server");
    //jobId a numeric value that depends on the PK in the job table
	
	/**
	 * Constructor
	 * @param commandName
	 * @param commandDescription
	 */
	private ArgumentEnum(String argumentName,String argumentDescription){		
		this.argumentName = argumentName;
		this.argumentDescription = argumentDescription;
	}
	
	private String argumentName;
	
	private String argumentDescription;
	
	
	public String getArgumentName() {
		return argumentName;
	}

	public String getArgumentDescription() {
		return argumentDescription;
	}	


}
