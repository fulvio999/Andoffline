package andoffline.constant.backend.sms.command;

/**
 * Enum with all the allowed values for the <argument> token of the command sms 
 * received from the AndofflineApp running on the phone
 *
 */
public enum SmsArgumentEnum {
	
	/* if predicate ="server" the allowed values are 'start' and 'stop'*/
	start("start"),
	stop("stop");
	
	/* if predicate = job the allowed values are any int number, representing the ID of the job*/

	private String tokenValue;
	
	public String getTokenValue() {
		return tokenValue;
	}

	/**
	 * Constructor
	 * @param value
	 */
	private SmsArgumentEnum(String value){
		this.tokenValue = value;
	}
	
	/**
	 * Utility method (case insensitive) to check if the input value is contained in the allowed ones by the enumeration
	 * @param s
	 * @return
	 */
	public static boolean containsIgnoreCase(String s)
	{
	    try {
			SmsArgumentEnum.valueOf(s.toLowerCase());
            return true;
        } catch (Exception e) {
            return false;
        }
	} 

 }
