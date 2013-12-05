package andoffline.app.sms.receiver;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import andoffline.app.sms.processor.AndOfflineSmsProcessor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;



/**
 * A custom SMS Receiver that receives event about new incoming SMS. 
 * Only if they starts with the user chosen prefix, it write a custom message on the on the Log so that PC receiver (that listen on log using LogCat)
 * can be notified about the new received SMS 
 * 
 */
public class AndOfflineSmsBroadcastReceiver extends BroadcastReceiver {
	
	private static final String TAG = "AndOfflineSmsBroadcastReceiver";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy-HH_mm_ss"); //eg: Oct-02-2013-15:10:39
	
	private String smsPrefix; 
	
	/**
	 * Constructor 
	 * @param smsPrefix The prefix of an incoming SMS to be managed by AndOffline
	 */
    public AndOfflineSmsBroadcastReceiver(String smsPrefix) {
    	this.smsPrefix = smsPrefix;
    }

    /**
     * Note: to keep alive the receiver is necessary use in conjunction with a service,
     * because at the end of onReceive() method if no further iteration are present the system kill the process
     */
    @Override
    public void onReceive(Context context, Intent intent) {    	
    	       
        String action = intent.getAction();
        
        String msgToWrite = null;

        /* filter the Intents: get only the ones about SMS received */
        if(action.compareTo("android.provider.Telephony.SMS_RECEIVED") == 0) {
        	
        	Log.i(TAG, "New SMS Received");

        	/* the common prefix for SMS that must be processed by AndOffline */       	
            Bundle bundle = intent.getExtras();
            SmsMessage[] messages = null;
          
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];
            
            for (int i = 0; i < pdus.length; i++)
               messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

            for (SmsMessage message : messages)
            { 
            	 if (message.getMessageBody().toLowerCase(Locale.getDefault()).startsWith(smsPrefix)) { 
            		 
            		Log.i(TAG, "Incoming SMS is for Andoffline, origin msisdn: "+message.getOriginatingAddress());
            		 
            		/* the sms text WITH the prefix */
            		msgToWrite = message.getMessageBody();                 
            	    
            	    try{           	    	
            	    	 Calendar cal = Calendar.getInstance(); 
            	    	 cal.setTimeZone(TimeZone.getDefault());
            	         Date today = cal.getTime();
            	         String todayString = sdf.format(today);            	         
            	    	
            	         /* append the receiving date and the Msisdn */ 
            	         AndOfflineSmsProcessor.processMessage(message.getOriginatingAddress()+" : "+todayString+" : "+msgToWrite); 
            	         
            	         /* Drop the SMS message so it doesn't go to the user's inbox: the incoming sms is not saved in the sqlite
            	            database as happen for the normal sms (to check try to export sms).
                  	        stops the SMS dispatching to other receivers. If something is going wrong, the SMS will not be saved.
                  	      */
                         this.abortBroadcast();   
            	         
            	    }catch (IOException e){
            	       Log.e(TAG, "Error processing message: "+e.getMessage());            	                	    
                    }  
            	 
            	   //otherwise the sms is forwarded as normal sms broadcast: is not managed by Andoffline  
                }            
           } 
      }        
   }       
}