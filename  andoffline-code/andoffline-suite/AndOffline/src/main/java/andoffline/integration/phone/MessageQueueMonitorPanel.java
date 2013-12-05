
package andoffline.integration.phone;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;


/**
 * Create a panel that display some informations about the status of the incoming - outcoming
 * Queue (eg. received messages, messages in queue, processed messages )
 *
 */
public class MessageQueueMonitorPanel extends JPanel {
	
	 private static final long serialVersionUID = 1L;
		
	 private JLabel totalMessageProcessedLabel; //ie messages picked up form the queue and execute
	 private JLabel totalMessageProcessed; //value updated at runtime
	 
	 private JLabel messageInQueueLabel; //ie the messages to process that are currently queued (the queue size)
	 private JLabel messageInQueue;  //value updated at runtime
	 
	/**
	 * Constructor
	 */
	public MessageQueueMonitorPanel(){
		
		this.setBorder(BorderFactory.createTitledBorder("Real Time Monitor Panel"));
		this.setLayout(new MigLayout("wrap 2"));
		
		this.totalMessageProcessedLabel = new JLabel("Total message(s) processed: ");
		this.totalMessageProcessed = new JLabel("0");
				
		this.messageInQueueLabel = new JLabel("Message(s) in queue to process: ");		
		this.messageInQueue = new JLabel("0");		
		
		//--- add component to the panel
		this.add(totalMessageProcessedLabel);
		this.add(totalMessageProcessed,"width 70");
		
		this.add(messageInQueueLabel);
		this.add(messageInQueue,"width 70");		
	}

	
	public JLabel getTotalMessageProcessed() {
		return totalMessageProcessed;
	}

	public void setTotalMessageProcessed(JLabel totalMessageProcessed) {
		this.totalMessageProcessed = totalMessageProcessed;
	}

	public JLabel getMessageInQueue() {
		return messageInQueue;
	}

	public void setMessageInQueue(JLabel messageInQueue) {
		this.messageInQueue = messageInQueue;
	}

}
