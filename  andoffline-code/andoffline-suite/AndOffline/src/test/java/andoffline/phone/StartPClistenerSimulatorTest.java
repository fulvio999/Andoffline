
package andoffline.phone;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import andoffline.integration.phone.AndroidFileWriterSimulator;
import andoffline.integration.phone.FileTailListenerHandler;
import andoffline.integration.phone.IncomingQueueConsumer;
import andoffline.integration.phone.MessageQueueMonitorPanel;


/**
 * TEST CLASS that simulate the FULL process: writing-queuing-consuming process (ie the full process)
 * Phone --> SD card <--> PC
 *  
 */
public class StartPClistenerSimulatorTest {	
	
	private static final Logger log = Logger.getLogger(StartPClistenerSimulatorTest.class);

	/*
	 * Constructor
	 */
	public StartPClistenerSimulatorTest(){
		
	}
	
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	 public static void main(String[] args) throws IOException, InterruptedException {	
		 
		try{
			log.info("Starting Listener....");
		
			//the file written by AndroidApp, to be monitored
			File monitoredFile = new File("/home/fulvio/andoffline.txt");
			
			// 1 start the SD card file writer process that simulate the AndofflineApp
			AndroidFileWriterSimulator androidFileWriterSimulator = new AndroidFileWriterSimulator(monitoredFile);
			androidFileWriterSimulator.startWriterSimulator();
			
			// 2 initialize the Queue and start his consumer
			IncomingQueueConsumer incomingQueueConsumer = new IncomingQueueConsumer();
			incomingQueueConsumer.startConsumer(new MessageQueueMonitorPanel()); //dummy panel to update
			
			//the initialized queue to be filled
			BlockingQueue<String> queue = incomingQueueConsumer.getIncomingQueue();
			
			// 3 start Tail listener on the file (the producer that fill the queue)
			FileTailListenerHandler fileTailListenerHandler = new FileTailListenerHandler(queue);				
			// FIXME fileTailListenerHandler.handle(monitoredFile,queue,1000);
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
