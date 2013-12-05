
package andoffline.app.sms.receiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

/**
 * Android service that register a new BroadCastReceiver to receive notifications 
 * about new incoming SMS
 *
 */
public class AndOfflineService  extends Service {
	
	private static final String TAG = "AndOfflineService";
	
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    
    /* The "key" name used to store the Preferences */
    public static final String ANDOFFLINE_PREFS_NAME = "andOfflinePrefsFile";
    
    /* The custom sms receiver to register */
    private AndOfflineSmsBroadcastReceiver andOfflineSmsBroadcastReceiver;

	/**
	 * Constructor
	 */
	public AndOfflineService() {
		
	}
	
	/**
	 * Called by the system when the service is FIRST created.
	 * It register a new Broadcast receiver to be notified when a new SMS arrives 
	 */
	public void onCreate(){		
		
		super.onCreate();
		
		SharedPreferences settings = getSharedPreferences(ANDOFFLINE_PREFS_NAME, 0);
	    String smsPrefix = settings.getString("smsPrefix","");
	    Log.i(TAG, "Retrieving Preferences: "+smsPrefix);
		
		//filter the intents so that AndOfflineSmsReceiver receives only the Intent about incoming SMS
    	IntentFilter filter = new IntentFilter(SMS_RECEIVED);    
    	
    	andOfflineSmsBroadcastReceiver = new AndOfflineSmsBroadcastReceiver(smsPrefix);
        registerReceiver(andOfflineSmsBroadcastReceiver, filter); 
              
        Log.i(TAG, "Service Created");
	}

	
	/**
	 * Called by the system to notify a Service that it is no longer used and is being removed. 
	 * Clean up any resources it holds (eg new registered receiver)
	 */
	public void onDestroy() {
		 
		unregisterReceiver(andOfflineSmsBroadcastReceiver);
		
		Log.i(TAG, "Service Stopped/Destroyed");
	}

	/**
	 * Second method called after the onCreate(). Is called with the argument supplied by the client
	 * The result returned by this method determines how The Android system treat the Service when his process is killed.
	 * 
	 * START_STICKY is used for services that are explicitly started and stopped as needed: it keep the Service
	 * in the started state when the process is killed, after create a new service instance and call onStartCommand
	 * with input Intent=null if there are not commands to be delivered to the service
	 *   
	 * Called on first time with:
	 *  Received start id 1: Intent { cmp=andoffline.app/.sms.receiver.AndOfflineSmsReceiverService }
   	 *    
	 */
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		
		//TODO check if intent is null ??
		
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly  stopped, so return sticky
        
        return START_STICKY;
    }

	
	
	/**
	 * Return an IBinder through which clients can call on to the service. 
	 * null if clients can not bind to the service
	 */
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

}
