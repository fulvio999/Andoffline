
package andoffline.constant.backend;

/**
 * Constant used in various point of the back-end source code
 *
 */
public enum BackendConstantEnum {
	
	/* Name of the log tag inserted in the class of the Android application necessary to filter	the log messages */
	ANDOFFLINE_APP_FILTER_TAG("AndOfflineSmsProcessor");
	
	private String paramValue;
	
	/*
	 * Constructor
	 */
	private BackendConstantEnum(String v){
		this.paramValue = v;
	}

	
	
	public String getParamValue() {
		return paramValue;
	}

}
