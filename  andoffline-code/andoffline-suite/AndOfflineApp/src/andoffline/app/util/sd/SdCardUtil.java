package andoffline.app.util.sd;

import java.text.DecimalFormat;

import android.os.Environment;
import android.os.StatFs;

/**
 * Utility class with some method to check the state of SD card status
 * 
 * NOTE: Currently this class is NOT used by the application because the idea to write on SD card is not possible because 
 * is NOT possible write on the SD card when is mounted by PC. Maybe this class can be useful in the future 
 */
public class SdCardUtil {
	
	private static final String TAG = "SdCardUtil";

	/**
	 * Constructor
	 */
	public SdCardUtil() {
		
	}
	
	/**
	 * Check if SD is mounted
	 * @return true if the SD card is correctly mounted
	 */
	public static boolean isSdMounted(){
		
		 String state = Environment.getExternalStorageState();
		 if (Environment.MEDIA_MOUNTED.equals(state))
			return true;
		 else
			return false;
	}
	
	/**
	 * Check if is possible write to SD card 
	 * @return true if the SD card is writable
	 */
	public static boolean isSdIWritable(){
		
		 String state = Environment.getExternalStorageState();
		 if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
			 return false;
		 else
			 return true;		 
	}

    /**
     * Get the amount of FREE space (MBytes) (including reserved blocks (that are not available to normal applications) in the SD card using StatFs class, which retrieve overall information about the space on a file-system
     * @return the amount of free space in the SD card
     */
	public static String getSdCardFreeSpace(){
		
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
		
		double bytesFreeTotal = (long) statFs.getBlockSize() * (long) statFs.getFreeBlocks();		
		double megTotal = bytesFreeTotal / 1048576; //divide by 1048576 to get size in MB
		
		DecimalFormat twoDecimalForm = new DecimalFormat("#.#####");		
		
		return twoDecimalForm.format(megTotal);		
	}
	
	/**
     * Get the SD card size (MBytes)
     * @return the amount of free space in the SD card
     */
	public static String getSdCardTotalSpace(){
		
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
		
        double bytesFreeTotal = (long) statFs.getBlockSize() * (long) statFs.getAvailableBlocks();		
		double megTotal = bytesFreeTotal / 1048576; //divide by 1048576 go get size in MB
		
		DecimalFormat twoDecimalForm = new DecimalFormat("#.#####");
		
		return twoDecimalForm.format(megTotal);		
	}
	
}
