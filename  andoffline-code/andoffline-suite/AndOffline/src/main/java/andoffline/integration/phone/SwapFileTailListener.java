package andoffline.integration.phone;

import java.io.File;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.log4j.Logger;

public class SwapFileTailListener {	
	
	private static final Logger log = Logger.getLogger(SwapFileTailListener.class);
	 
	private Tailer tailer;
	
    /**
     * Constructor
     */
	public SwapFileTailListener() {		
		
	}
	
	/**
	 * Start the "Tail" listener on the txt file written by Android 'logcat' command
	 * eg: adb logcat -s AndOfflineSmsProcessor > /home/test/destinatiofile.txt 
	 * That listener put the last line in a blocking-queue which is read by the job executor
	 * 
	 * (the input lines are like: I/AndOfflineSmsProcessor(275): New message: +39349166789 Oct-02-2013-15:10:39 textOfSms ) 
	 * 
	 * @param fileToTail The txt file to be monitored filled by Android 'logcat' utility
	 * @param incomingQueueToFill The BlockingQueue where put the new entry red from the monitored file
	 * @param pollingFreq the frequency of polling the swap file 
	 * @param smsPrefix The sms prefix set on Android application used to filter the incoming sms
	 */
	public void startListener(File fileToTail, BlockingQueue<String> incomingQueueToFill, long pollingFreq){
		
		log.info("Starting Listener on file: "+fileToTail+ " with polling freq (ms): "+pollingFreq);
		
		/* the handler which pass the new input line */
		TailerListener listener = new FileTailListenerHandler(incomingQueueToFill);	
	    this.tailer = new Tailer(fileToTail, listener, pollingFreq);
	        
	    /* this call create a blocking loop */	
	    tailer.run(); 
	}

	public Tailer getTailer() {
		return tailer;
	}

	public void setTailer(Tailer tailer) {
		this.tailer = tailer;
	}

}
