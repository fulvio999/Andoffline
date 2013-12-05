
package andoffline.gui.common.utility;

import java.util.Hashtable;

/**
 * This class offers the data for the label translation/customization. For example to translate a 
 * label or to convert it in a human readable format.
 * It is used in various class inside the application
 *
 */
public class VcfTranslatorUtils {
	
	
	public static Hashtable<String, String> conversionNameTable = new Hashtable<String, String>();
	
	static {
		
		//-- Fill the conversion table with the originals and human readable values
		conversionNameTable.put("N", "NAME");
		conversionNameTable.put("FN", "FULL NAME");
		conversionNameTable.put("item1.URL", "URL"); //item1.URL:http\://www.google.com/profiles/.....
		conversionNameTable.put("item1.X-ABLabel", "PROFILE"); // item1.X-ABLabel:PROFILE
				
		//TODO finish to fill the conversion table
		
	}

	/**
	 * Constructor
	 */
	public VcfTranslatorUtils() {
		
		
	}

}
