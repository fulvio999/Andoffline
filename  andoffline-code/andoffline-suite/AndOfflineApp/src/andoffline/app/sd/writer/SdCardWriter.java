package andoffline.app.sd.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import andoffline.app.util.AndOfflneConstants;
import android.os.Environment;
import android.util.Log;

/**
 * Write to SD card file
 * 
 * NOTE: Currently this class is NOT used by the application because the idea to write on SD card is not possible because 
 * is NOT possible write on the SD card when is mounted by PC. Maybe this class can be useful in the future  
 *
 */
public class SdCardWriter {
	
	private static final String TAG = "SdCardWriter";
	
	/**
	 * Write to file, placed on SD card (under a folder named 'andoffline'), the sms message in argument.
	 * The file will be created if missing, otherwise is appended at the end.
	 *  
	 * @param message The sms text to write (without prefix)
	 * @throws Exception 
	 */
	public static void writeSms(String message) throws IOException {
		
		/* PREMISE: all the checks on SD card are already done before starting-up the server */
		
		try {				
			 //find the root folder of the SD card (value known at runtime because depends on the Phone)
			 File root = Environment.getExternalStorageDirectory();
			 Log.i(TAG, "SD card root folder: " + root.getName());				    	
			
			 /* If not present, create the folder "andoffline" where place the swap file */
			 File customFolder = new File(root+File.separator+"andoffline");
			 
			 if(!customFolder.exists()){
				Log.i("TAG", "andoffline folder not present, create it"); 
				customFolder.mkdir();
			 }else
				Log.d("TAG", "andoffline folder already exist"); 
			 
			 File file = new File(customFolder.getAbsolutePath(),AndOfflneConstants.INCOMING_SMS_FILE_NAME.getValue()); 			 
			 	 
			 Log.d("TAG", "Start SD card writing process....");
				 
			 if(file.exists())
			   	Log.d("TAG", "File "+AndOfflneConstants.INCOMING_SMS_FILE_NAME.getValue() +" already exist");
			 else{
			   	Log.i("TAG", "File "+AndOfflneConstants.INCOMING_SMS_FILE_NAME.getValue() +" doesn't exist, creating it");			    		 
			   	file.createNewFile();			    		
			 }
			 
			 if(file.canWrite())
			 {				 
				byte[] data = message.getBytes();
				String msgToWrite = new String(data); 						
					    	
				FileWriter fw = new FileWriter(file,true);
				fw.append(msgToWrite+"\n");
				fw.close();
					    	
				Log.i("TAG", "End SD card writing process.");
				 
			 }else{
				 //Note: this case shouldn't happen because before service starting some SD check was performed (eg SD card read-only)
				 Log.e("TAG", "File "+AndOfflneConstants.INCOMING_SMS_FILE_NAME.getValue()+" is not writeable !");	
				 throw new IOException("File not writeable !");
			 }
			
		} catch (IOException e) {
			Log.e("TAG", "Problem writing to SD card: "+e.getMessage());				
		}
		
	}

}
