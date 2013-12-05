package andoffline.integration.phone.command.validator;

/**
 * List all the possible values that can be placed as <predicate> tokens
 * in the text of the sms text to be processed 
 *
 */
public enum PredicateEnum {
	
     job("job","execute an existing job, which one is provided with argumentEnum"),
     server("server","depending on the argumentEnum, start/stop the embedded server ");
	
	/**
	 * Constructor
	 * @param commandName
	 * @param commandDescription
	 */
	private PredicateEnum(String predicateName,String predicateDescription){		
		this.predicateName = predicateName;
		this.predicateDescription = predicateDescription;
	}
	
	private String predicateName;
	
	private String predicateDescription;
	

	public String getPredicateName() {
		return predicateName;
	}

	public String getPredicateDescription() {
		return predicateDescription;
	}
	

}
