package andoffline.app.util;

/**
 * Class with the parameters used in the java code (other than the constant used by Android (eg string.xml) )
 *
 */
public enum AndOfflneConstants {	

  /* The SD card file where write the received SMS to be processed by the PC */	
  INCOMING_SMS_FILE_NAME("andoffline-in.txt"),
  
  /* The SD card file where the PC write the SMS to be sent out as receipt for an execute command (ie like: "OK command executed successfully") */
  // currently not used OUTCOMING_SMS_FILE_NAME("andoffline-out.txt"),
  
  /* The name of the Android preferences key where store the Andoffline application preferences (eg smsPrefix chosen) */
  ANDOFFLINE_PREFS_NAME("andOfflinePrefsFile");
  
  private String value;	
  

  private AndOfflneConstants(String value) {
	   this.value = value;
  }


  public String getValue() {
	  return value;
  }
  

}
