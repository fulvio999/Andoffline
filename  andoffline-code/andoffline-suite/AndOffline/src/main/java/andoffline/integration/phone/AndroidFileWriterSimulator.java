
package andoffline.integration.phone;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Simulate the Android application that write data to a file placed on the SD card  
 *
 */
public class AndroidFileWriterSimulator{
	
	private File pcounter_log;
	
	/**
	 * @param pcounter_log The file to be monitored
	 */
	public AndroidFileWriterSimulator(File pcounter_log){
		this.pcounter_log = pcounter_log;

	}

	
	/**
	 * Start the SD card file writer process
	 * 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * 
	 */
	public void startWriterSimulator() throws IOException, InterruptedException {
        
        // producer thread: Write to a file (ie simulate the behavior of the AndofflineApp running on the phone)
      	new Thread(new Runnable() {

      		public void run() {
      			System.out.println("Starting Android File Writer simulator on file: "+pcounter_log);     				
      			
      			FileWriter fw;
      				
      			try {
      				  fw = new FileWriter(pcounter_log);		
      				
      				  for(int i=0; i<10000;i++){
      					fw.append("sms: "+i +"\n");
      					fw.flush();
      					Thread.sleep(5000);
      				  }
      				
      			} catch (Exception e) {  
      				System.out.println("Error writing SD card file");
      				e.printStackTrace();
      			}
      		}
      	}).start();

	}
	
	 /* For standalone use */
	 public static void main(String[] args) throws IOException, InterruptedException {	
		
		 System.out.println("Starting AndroidApp write simulator as standalone...");
		 
		 File monitoredFile = new File("/home/fulvio/andoffline.txt");
			
		// 1 start the SD card file writer process that simulate the AndofflineApp
		AndroidFileWriterSimulator androidFileWriterSimulator = new AndroidFileWriterSimulator(monitoredFile);
		androidFileWriterSimulator.startWriterSimulator();
	 }

}
