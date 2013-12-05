
package andoffline.app.sms.processor;

import java.io.IOException;

import android.util.Log;

/**
 * Process a new received SMS for AndOffline application. 
 * The process task consist of writing it to SD card  
 *
 */
public class AndOfflineSmsProcessor {
	
	private static final String TAG = "AndOfflineSmsProcessor";

	 /**
	  * Process the incoming SMS sent to AndOfflineApp, writing a "special" log message that is read by the receiver on the PC 
	  * 	
	  * @param message  The SMS message text content WITH the the 'prefix'
	  * @return
	  * @throws Exception If error during the writing process (eg. file not writable)
	  */
	 public static boolean processMessage(String message) throws IOException {		
		 
		 /* 
		  * Write a "special" message to LOG to be read by application running on the PC
		    For example:
		    I/AndOfflineSmsProcessor(275): New message: 123456 : Oct-02-2013-15_10_39 : textOfSms
		 */
		 Log.i(TAG, "New message: "+message);		 
		
		 /*
		   NOT used, the idea to write sms to SD card is not applicable: the SD card can't be written when in mounted on the PC  
		   SdCardWriter.writeSms(message);
		 */
		 
		 return false;		 
	 }

}
