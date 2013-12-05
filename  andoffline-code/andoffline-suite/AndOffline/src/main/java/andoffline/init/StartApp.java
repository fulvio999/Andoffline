package andoffline.init;

/**
 * 
 * Entry point for the application
 *
 */
public class StartApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{ 
			//create the initial Thread
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				
	            public void run() {            	
	            	AndOfflineApplication andOffline = new AndOfflineApplication();	            	
	            }
	        });			
	       
	       	
	}

	

}
