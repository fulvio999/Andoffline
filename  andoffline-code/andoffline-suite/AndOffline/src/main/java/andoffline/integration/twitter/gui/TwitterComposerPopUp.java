
package andoffline.integration.twitter.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import andoffline.gui.common.utility.BusyLabelPanel;
import andoffline.integration.twitter.action.TwitterSenderAction;

import net.miginfocom.swing.MigLayout;

/**
 * Create a popUp window where the user can compose a twit and send to Twitter (after a successful Twitter service configuration)
 *
 */
public class TwitterComposerPopUp extends JDialog implements ActionListener{
	
	 private static final long serialVersionUID = 1L;	
	
	 private JLabel messageAreaLabel;
	 private JTextArea messageValue; 
	 
	 private JButton closeButton;
	 private JButton confirmButton;	
		 
	 /* A label that show operation results/errors */
	 private JLabel messageLabel;	 
	 
	 /* A sub-panel with an animation to indicates that a processing is in action */
	 private BusyLabelPanel busyLabelPanel;

	/**
	 * Constructor
	 * @param message The message contained in the selected text node to be sent
	 */
	public TwitterComposerPopUp(String message) {
		
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Tweet Composer"));		
		this.buildPopUp();	
		
		this.messageValue.setText(message);
	}
	
	/**
	 * Utility method that build the popup layout
	 */
	private void buildPopUp(){
		
		 this.setLayout(new MigLayout("wrap 1")); //say that we want 1 columns	        
	     this.add(new JLabel("Send a Tweet to the configured account"),"wrap,span 2,align center");	    
		
		 messageAreaLabel = new JLabel("Message:");
		
		 messageValue = new JTextArea("");
		 messageValue.setBorder(BorderFactory.createLineBorder(Color.blue));
		 messageValue.setMinimumSize(new Dimension(500,300));
		 messageValue.setMaximumSize(new Dimension(500,900));
		 
		 busyLabelPanel = new BusyLabelPanel();
		 busyLabelPanel.getJxBusyLabel().setEnabled(false);
		 busyLabelPanel.getJxBusyLabel().setBusy(false);
		 
		 closeButton = new JButton("Close");
	     confirmButton = new JButton("Send"); 	     
	        
	     messageLabel = new JLabel("");
	        
	     closeButton.addActionListener(this);
	     confirmButton.addActionListener(this);
	     
	     //--------- ADD THE ITEMS ----------	    
	     this.add(messageAreaLabel,"wrap");
	     this.add(messageValue,"growx,width 200:500:900");
	     
	     this.add(closeButton,"split 2,align center");
         this.add(confirmButton);
         
         this.add(messageLabel,"span,align center");
         
         this.add(busyLabelPanel,"span 1,align center,gapbottom 20");
         
	     this.setTitle("Send Tweet");
	     this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);	     
	     this.setSize(500,500);  		   
	     this.setVisible(true);   
	}     

	/**
	 * Manage the action on the buttons in the popUp
	 */
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() instanceof JButton)  
		{		   
		    if (e.getActionCommand().equalsIgnoreCase("Close")){	
		    	
		   	  this.setVisible(false);
		   	  this.dispose();
		    }
				      
			//send msg to Twitter
			if (e.getActionCommand().equalsIgnoreCase("Send"))
			{				 
				//open a separate thread in case of the operation last a while...
				if (!busyLabelPanel.getJxBusyLabel().isEnabled())
		       	{
		       	   busyLabelPanel.getJxBusyLabel().setEnabled(true);		       	  
		       	}
		       	
		       	if (busyLabelPanel.getJxBusyLabel().isBusy()) {	   //true if task finished   	    
		       	    busyLabelPanel.getJxBusyLabel().setBusy(false);
		       	    busyLabelPanel.getJxBusyLabel().setVisible(false);		       	   
		       	   
		       	}else{	
		       		   //show the animation
		       	       busyLabelPanel.getJxBusyLabel().setBusy(true);
		       	       busyLabelPanel.getJxBusyLabel().setVisible(true); 	       	    
	       	    
		       	       final TwitterSenderAction tweetSenderAction = new TwitterSenderAction();
		       	      
				       /**
				        * Create a new thread (different than the Event Dispatch Thread used by Swing to dispatch event) 
				        * because the operation can last some time if the mail server is slow      
				        */
			       	   Thread sendTweetThread = new Thread() {
						
			       	     	public void run() {					                       
						        
							    try{ 
						    	      tweetSenderAction.send(messageValue.getText());
									    
								      // hide and stop the animation
								      busyLabelPanel.getJxBusyLabel().setBusy(false);
								 	  busyLabelPanel.getJxBusyLabel().setVisible(false);
									    
									  messageLabel.setText("Operation Executed Successfully");
								      messageLabel.setFont(new Font("Serif", Font.BOLD, 15));
								      messageLabel.setForeground(Color.GREEN);
							     					    
							        }catch (Exception ex) {
											
									  // stop and hide the animation
									  busyLabelPanel.getJxBusyLabel().setBusy(false);
								   	  busyLabelPanel.getJxBusyLabel().setVisible(false);						   		  
								   		  
							    	  messageLabel.setText("Operation Failed: "+ex.getMessage());
							    	  messageLabel.setFont(new Font("Serif", Font.BOLD, 15));
									  messageLabel.setForeground(Color.RED);
							        }
				                 }
							 };
							                
					sendTweetThread.start(); 	
		    	
			     }
		     }		
	      }
     }
}
