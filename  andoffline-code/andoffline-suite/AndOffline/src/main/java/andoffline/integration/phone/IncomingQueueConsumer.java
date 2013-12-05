
package andoffline.integration.phone;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JLabel;

import org.apache.log4j.Logger;

import andoffline.integration.phone.command.PhoneCommandInterpreter;


/**
 * Manager for the Queue filled by the "Tailer" object with the content of the monitored file
 * by AndofflineApp.
 * 
 * This class contains more component:
 * 1 listen for data in a file written by Tail that monitor file written by LogCat
 * 2 put the red data in a BlockingQueue 
 * 3 consume the data in the BlockingQueue
 *
 */
public class IncomingQueueConsumer {
	
	private static final Logger log = Logger.getLogger(IncomingQueueConsumer.class);
	
	/*
     * The queue filled with data coming from log file written by AndroidApp
     */
	private BlockingQueue<String> incomingQueue; 
	
	/* the interpreter for new incoming messages */
	private PhoneCommandInterpreter phoneCommandInterpreter;
	
	/* The amount of messages currently in the queue to be processed */
	private int messageInQueue; 
	
	/* The amount of messages picked from the queue and processed */	
	private int amountOfMessageProcessed;
	
	private boolean executeFlag;
	
	/*
	 * Constructor
	 */
	public IncomingQueueConsumer()
	{		
		this.incomingQueue = new LinkedBlockingQueue<String>();
		this.phoneCommandInterpreter = new PhoneCommandInterpreter();
		//reset the counter
		this.messageInQueue = 0;
		this.amountOfMessageProcessed = 0;		
	}

	/**
	 * Start a Consumer that process the messages in the BlockingQueue by the "Tail" File Listener process
	 */
	public void startConsumer(final MessageQueueMonitorPanel messageQueueMonitorPanel){
		
		executeFlag = true;
		
		//consumer thread: read message from IncomingQueue and pass it to the message processor
		new Thread(new Runnable() {

			public void run() {
				
				log.info("IncomingQueueConsumer starting Thread...");
				
				String incomingMsg = null;
				
				JLabel tm = messageQueueMonitorPanel.getTotalMessageProcessed();
				JLabel mq = messageQueueMonitorPanel.getMessageInQueue();
				
				while(executeFlag){
					
				    try {
						/* "poll" method return "null" if the queue is empty
						   NB: is possible provide an amount of time to wait in case of the queue is empty
						*/
						incomingMsg = incomingQueue.poll();
						
						if(incomingMsg != null)
						{
						  //the entry are like: I/AndOfflineSmsProcessor(275): New message: +39349166789 Oct-02-2013-15_10_39 textOfSms	
						  phoneCommandInterpreter.processCommand(incomingMsg);
						  amountOfMessageProcessed ++;
						  messageInQueue = incomingQueue.size();
						  
						  //update the gui						 
						  tm.setText(amountOfMessageProcessed+"");
						  mq.setText(messageInQueue+"");
						  tm.repaint();
						  mq.repaint();						  
						  
						  log.info("Incoming Queue current size: "+incomingQueue.size());
						  
						}else {
						   messageInQueue = 0;						  
						   mq.setText(messageInQueue+"");
						   mq.repaint();
						   						   
						   log.debug("Incoming Queue currently is empty");
						}
						
						Thread.sleep(4000); //dummy delay to slow the process
						
					}catch (InterruptedException e) {
					   log.info("Error stopping IncomingQueueConsumer: ",e);						
					}
				}				
				
				//clean the Queue
				log.info("Queue Consumer stopped");
			}
		}).start();
	}
	
	
	/**
	 * lowers the flag to stop the Thread process the  the Queue consumer
	 */
	public void stopExecuting() {
		log.info("Stoping the queue consumer...");
        this.executeFlag = false;
    }

	public BlockingQueue<String> getIncomingQueue() {
		return incomingQueue;
	}

	public void setIncomingQueue(BlockingQueue<String> incomingQueue) {
		this.incomingQueue = incomingQueue;
	}

	public PhoneCommandInterpreter getPhoneCommandInterpreter() {
		return phoneCommandInterpreter;
	}

	public void setPhoneCommandInterpreter(PhoneCommandInterpreter phoneCommandInterpreter) {
		this.phoneCommandInterpreter = phoneCommandInterpreter;
	}

	public int getAmountOfMessageProcessed() {
		return amountOfMessageProcessed;
	}

	public void setAmountOfMessageProcessed(int amountOfMessageProcessed) {
		this.amountOfMessageProcessed = amountOfMessageProcessed;
	}

	
}
