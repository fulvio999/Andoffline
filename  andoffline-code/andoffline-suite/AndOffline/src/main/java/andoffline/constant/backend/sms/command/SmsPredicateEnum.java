
package andoffline.constant.backend.sms.command;

/**
 * Enum with all the allowed values for the <predicate> token of the command sms 
 * received from the AndofflineApp running on the phone
 *
 */
public enum SmsPredicateEnum {
	
	job("job"),
	server("server");

	private String tokenValue;
	
	public String getTokenValue() {
		return tokenValue;
	}

	/**
	 * Constructor
	 * @param value
	 */
	private SmsPredicateEnum(String value){
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
			SmsPredicateEnum.valueOf(s.toLowerCase());
            return true;
        } catch (Exception e) {
            return false;
        }
	}
}
