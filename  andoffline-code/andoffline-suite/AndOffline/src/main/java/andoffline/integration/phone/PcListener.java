
package andoffline.integration.phone;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;


/**
 * Entry point class that start the reading process:
 * 
 * Phone --> Swap-File --> IncomingQueue --> PC
 *  
 * Where "Swap File" is created by Android command 'LogCat'.
 * 
 * NOTE: before start this listener is necessary execute in a shell a command like this:
 * adb logcat -s filterTagName > /home/test/destinatiofile.txt
 * (See file 'doc/pc-listener-service.script') 
 *
 */
public class PcListener extends SwingWorker<Void, Void> {	
	
	private static final Logger log = Logger.getLogger(PcListener.class);

	private SwapFileTailListener swapFileTailListener;
	
	private IncomingQueueConsumer incomingQueueConsumer;
	
	private String fileMonitored;	
	private long pollingFreq;
	private MessageQueueMonitorPanel messageQueueMonitorPanel;
		
	/**
	 * Constructor
	 * 
	 * @param fileMonitored The swap file written by Android 'logcat' utility
	 * @param pollingFreq The frequency of polling the swap file
	 * @param messageQueueMonitorPanel The monitoring panel with the real time queue situation
	 * @param smsPrefix The sms prefix set on the Android application used to filter the incoming messages
	 */
	public PcListener(String fileMonitored, long pollingFreq, MessageQueueMonitorPanel messageQueueMonitorPanel) {
		
		this.fileMonitored = fileMonitored;
		this.pollingFreq = pollingFreq;
		this.messageQueueMonitorPanel = messageQueueMonitorPanel;		
	}	
	
	/**
	 * @param messageQueueMonitorPanel the panel with the value to update to be shown in the GUi
	 * @throws InterruptedException 
	 * @throws IOException 
	 */	
	@Override
	protected Void doInBackground(){
		
		try{		
			// the file created and written by 'logcat' and monitored in "tail" mode
			File fileToMonitor = new File(this.fileMonitored);
			
			// initialize the Queue where Tailer object put the incoming msg and start his queue consumer
			incomingQueueConsumer = new IncomingQueueConsumer();
			incomingQueueConsumer.startConsumer(messageQueueMonitorPanel);
			
			// get the previously initialized queue that will be filled with "tail" (ie andoffline.txt)
			BlockingQueue<String> incomingQueue = incomingQueueConsumer.getIncomingQueue();
			
			// start Tail listener on the swap file. The tail listener put the last incoming sms in the "incomingQueue"			
			swapFileTailListener = new SwapFileTailListener();				
			swapFileTailListener.startListener(fileToMonitor,incomingQueue,pollingFreq);
		
		}catch (Exception e) {
		   log.fatal("Error starting the PcListener, cause: ",e);
		}
		return null;
	}

   /**
    * Start the tail listener on txt written by Android logCat command
    */
	public void startListener(){
		
		log.info("Start listener to monitor file: "+fileMonitored+ " with pollingFreq:"+pollingFreq);
		//run doInBackground() method in a new Thread
		this.execute();
	}
	
	/**
	 * Stop the listener and the Tail object
	 */
	public void stopListener(){
		
		//stop the tail listener
		this.swapFileTailListener.getTailer().stop();
		this.cancel(true);
		
		//stop the queue consumer
		this.incomingQueueConsumer.stopExecuting();		
	}

	public SwapFileTailListener getSwapFileTailListener() {
		return swapFileTailListener;
	}

	public void setSwapFileTailListener(SwapFileTailListener swapFileTailListener) {
		this.swapFileTailListener = swapFileTailListener;
	}

	public IncomingQueueConsumer getIncomingQueueConsumer() {
		return incomingQueueConsumer;
	}

	public void setIncomingQueueConsumer(IncomingQueueConsumer incomingQueueConsumer) {
		this.incomingQueueConsumer = incomingQueueConsumer;
	}

}
