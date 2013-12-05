package andoffline.app;


import andoffline.app.sms.receiver.AndOfflineService;
import andoffline.app.util.AndOfflneConstants;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main Activity of the application with the start/stop buttons and sms prefix settings 
 *
 */
public class AndOfflineMainActivity extends Activity {
	
	private static final String TAG = "AndOfflineMainActivity";	
	
	/* The chosen prefix for the incoming messages that must be handled AndOffline */
	private String smsPrefix = null;

	/**
	 * Called the first time or when the use re-open the application selecting it form the sliding notification bar in the upper part
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);			
		setContentView(R.layout.andoffline_layout);			
		
		final Button startReceiverButton = (Button) findViewById(R.id.start_receiver_button);
		startReceiverButton.setEnabled(true);	
		
		final Button stopReceiverButton = (Button) findViewById(R.id.stop_receiver_button);	
		stopReceiverButton.setEnabled(false);		
		
		final EditText smsPrefixInput = (EditText) findViewById(R.id.prefixInputText);
		smsPrefixInput.setEnabled(true);		
		
		final TextView serviceStatusLabel = (TextView) findViewById(R.id.serviceStatusLabel);			
		final Intent andOfflineService = new Intent(this,AndOfflineService.class);		
		
		
		/* listener on the start button to START the receiver */
		startReceiverButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				// TODO restore smsPrefix value from storedPreferences
				
                smsPrefix = smsPrefixInput.getText().toString();
				
				/* user input validation */
				if (smsPrefix.length() == 0)
				{					
				  Toast toast = Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_LONG);
				  toast.show();
					
				}else{					
					  Log.d("TAG", "Starting the service...");
							
					  /* manage the visibility of some UI component */
					  smsPrefixInput.setEnabled(false);						
					  startReceiverButton.setEnabled(false);	
					  stopReceiverButton.setEnabled(true);
							
					  /* Android core method that start a service */
					  startService(andOfflineService);
							
					  /* store the chosen smsPrefix using Shared Preferences method */
					  storePreferences(smsPrefix);
							
					  //show an icon in the status bar: when the user click on it re-open the application
					  showNotification(); 
							
					  serviceStatusLabel.setText("Service Status: Started !");		
				 }				
			 }
		});		
		
		
		
		/* listener for STOP receiver button */		
		stopReceiverButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				/* Android core method that stop a service */
				stopService(andOfflineService);		

				/* manage the visibility of some UI component */
				stopReceiverButton.setEnabled(false);
				startReceiverButton.setEnabled(true);
				smsPrefixInput.setEnabled(true);				
				
				hideNotification();				
				
				serviceStatusLabel.setText("Service Status: Stopped !");
			}
		});				
		
		Log.i(TAG, "Activity Created");
		
		//  I/AndOfflineMainActivity( 2694): Activity Created
		
	}	

	
	/**
	 * Show a notification in the notification area (the one in the upper side).
	 * More info at: See: http://developer.android.com/guide/topics/ui/notifiers/notifications.html
	 * 
	 * Note: The use of Notifications is not available in Android 2.2 by default. They can be used adding a support-library 
	 * in the build but (download from http://developer.android.com/tools/extras/support-library.html#Samples) 
	 * The feature of add an action on the Notification is only available from Android 4.1
	 */
    private void showNotification() {
    	 
		Log.i(TAG, "Showing notification...");
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.service_icon)
		        .setContentTitle("AndOfflineApp")
		        .setContentText("AndOfflineApp is running !");
		
		/* Creates an explicit intent for an Activity in your app */
		Intent resultIntent = new Intent(this, AndOfflineMainActivity.class);  
		
		//TODO errore mi fà partire altra istanza dell'activity và su Activity created!!! nno recupera la stessa 

		// The stack builder object will contain an artificial back stack for the started Activity.
		// This ensures that navigating backward from the Activity leads out of your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(AndOfflineMainActivity.class);
		
		// Adds the Intent that starts the Activity to the top of the stack
	    stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 'id' allows you to update/delete the notification later on.
		mNotificationManager.notify(0, mBuilder.build());  	
	}
    
    /**
     * Hide the notification about the AndofflineApp is running
     */
    private void hideNotification() {
    	
    	Log.i(TAG, "Hide notification...");
    	
    	NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		/* hide the notification with id=0 */
		mNotificationManager.cancel(0);    	
    }
	
	
	/**
	 * Utility method that show an AlertDialog
	 * @param title The title of the AlertDialog
	 * @param message The message to show
	 */
	private void showAlertError(String title,String message){
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(AndOfflineMainActivity.this);
	    alertDialog.setTitle(title);
	    alertDialog.setMessage(message);
	        
	    alertDialog.setNegativeButton("Close",new DialogInterface.OnClickListener() {
	        	
			public void onClick(DialogInterface dialog,int id) {
				/* close the dialog box and do nothing */
				dialog.cancel();
			}
		});
	    
	   alertDialog.show();		
	}
	
	
	/**
	 * Utility method that saves the elementary input value as key-value 
	 * @param valueToStore
	 */
	private void storePreferences(String valueToStore){
		
		Log.i(TAG, "Saving Preferences, smsPrefix to save: "+valueToStore);
				
	    SharedPreferences settings = getSharedPreferences(AndOfflneConstants.ANDOFFLINE_PREFS_NAME.getValue(), 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString("smsPrefix", valueToStore);
	
	    editor.commit();
	}
	
	
	@Override
	/* Called after the onPause() */
	protected void onDestroy() {
		
		super.onDestroy();
		
		/* clean ALL the SharedPreferences of the application */		
		SharedPreferences settings = getSharedPreferences(AndOfflneConstants.ANDOFFLINE_PREFS_NAME.getValue(), 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.clear().commit();
		
		Log.i(TAG, "All SharedPreferences removed");
	}
	
	
	/**
	 * Called when the user RE-open the application from the available application menu list
	 * or click on the sliding notification status bar.
	 */
	@Override
	protected void onStart(){
		
		 super.onStart(); 		 
		 /* The activity is either being restarted or started for the first time */
		 Log.i(TAG, "Activity Start");		
	}
	
	/**
	 * Called when an activity is going into the background, but has not (yet) been killed.
	 * eg: another Activity come in background or when i press the Home button
	 * or when the screen become locked in auto
	 * Called before the onStop() method
	 */
	@Override
	protected void onPause () {
		
		super.onPause();		
		Log.i(TAG, "Activity Paused");		
	}
	
	
	/**
	 * Called to RETRIEVE per-instance state from an activity before being killed so that the state can be restored in onCreate(Bundle) method
	 * Called when the user press the "Home" button
	 * 
	 * Called before onPause()
	 */
	@Override
	protected void onSaveInstanceState (Bundle outState){
		
		super.onSaveInstanceState(outState);  
		
		//TODO save also the button status ???
		
		Log.i(TAG, "Saving Activity state");
	}

    
}
