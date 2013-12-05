
package andoffline.integration.phone;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.io.input.TailerListenerAdapter;
import org.apache.log4j.Logger;

/**
 * Handler called by the Tail listener {@link FileTailListener} when a new line is put in the monitored swap file.
 * That new line is put in a BlockingQueue, whose consumer process it.  
 *
 */
public class FileTailListenerHandler extends TailerListenerAdapter{
	
	private static final Logger log = Logger.getLogger(FileTailListenerHandler.class);
	
	/* The queue filled with the last row read from the file written by LogCat */
	private BlockingQueue<String> incomingQueueToFill;

	/**
	 * Constructor
	 */
	public FileTailListenerHandler(BlockingQueue<String> incomingQueueToFill) {
		this.incomingQueueToFill = incomingQueueToFill;		
	}
	
	/**
	 * Method called when a new line is inserted in the monitored swap file
	 * It put the new line in a BlockingQueue
	 * @param smsLine The last sms read from txt file
	 */
	public void handle(String smsLine) {		
		
        log.info("Handling new sms line: "+smsLine);
       
        try {
        	//put the new sms in the queue
        	incomingQueueToFill.put(smsLine);
		}catch (InterruptedException e) {
			
		  log.info("Error putting the sms: "+smsLine+" in the queue: ",e);			
		}
        
    }

}
