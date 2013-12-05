package andoffline.integration.phone.command.validator;

/**
 * List all the possible values that can be placed as <command> tokens
 * in the text of the sms text to be processed 
 *
 */
public enum CommandEnum {
	
	execute("execute","execute something listed in the PredicateEnum");
	
	/**
	 * Constructor
	 * @param commandName
	 * @param commandDescription
	 */
	private CommandEnum(String commandName,String commandDescription){		
		this.commandName = commandName;
		this.commandDescription = commandDescription;
	}
	
	private String commandName;
	
	private String commandDescription;

	
	
	public String getCommandName() {
		return commandName;
	}

	public String getCommandDescription() {
		return commandDescription;
	}
	

}
