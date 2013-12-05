package andoffline.integration.mailserver.gui;

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

import net.miginfocom.swing.MigLayout;
import andoffline.gui.common.utility.BusyLabelPanel;
import andoffline.integration.mailserver.action.MailSenderAction;
import andoffline.integration.mailserver.dao.MailServerConfigurationDao;

/**
 * Create a popUp window where the user can compose and send an email (after a successful mail server configuration)
 *
 */
public class MailComposerPopUp extends JDialog implements ActionListener{

	 private static final long serialVersionUID = 1L;
	 
	 /* The dedicated DAO to read/write the configuration for mail server connection */
	 private MailServerConfigurationDao mailServerConfigDao;

	 private JLabel fromLabel;
	 private JLabel toLabel;
	 private JLabel subjectLabel;
	 private JLabel messageAreaLabel;
	 
	 private JTextField fromValue;
	 private JTextField toValue;
	 private JTextField subjectValue;
	 private JTextArea messageValue; 
	 
	 private JButton closeButton;
	 private JButton confirmButton;
	 
	 /* A sub-panel with an animation to indicates that a processing is in action */
	 private BusyLabelPanel busyLabelPanel;
		 
	 /* A label that show operation results/errors */
	 private JLabel messageLabel;	 
	
	 /**
	  * Constructor
	  * @param message The message contained in the selected text node to be sent
	  */
	public MailComposerPopUp(String message) {
		
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Mail Composer"));		
		this.buildPopUp();	
		
		this.messageValue.setText(message);
	}
	
	/**
	 * Utility method that build the popup layout
	 */
	private void buildPopUp(){
		
		 this.setLayout(new MigLayout("wrap 1")); //say that we want 1 columns	        
	     this.add(new JLabel("Compose a mail to send"),"wrap,span 2,align center");	    
	     
	     // load the saved values (if any) in the configuration file placed in the "conf" folder to already fill the text fields
		 mailServerConfigDao = new MailServerConfigurationDao();
		 mailServerConfigDao.readConfiguration();		
	     
		 fromLabel = new JLabel("From:");
		 toLabel = new JLabel("To:");
		 subjectLabel = new JLabel("Subject:");
		 messageAreaLabel = new JLabel("Message:");
		 
		 fromValue = new JTextField("");
		 fromValue.setText(mailServerConfigDao.getMailSenderAddress());
		 
		 toValue = new JTextField("");
		 subjectValue = new JTextField("");
		 
		 messageValue = new JTextArea("");
		 messageValue.setBorder(BorderFactory.createLineBorder(Color.blue));
		 messageValue.setMinimumSize(new Dimension(500,300));
		 
		 busyLabelPanel = new BusyLabelPanel();
		 busyLabelPanel.getJxBusyLabel().setEnabled(false);
		 busyLabelPanel.getJxBusyLabel().setBusy(false);
		 
		 closeButton = new JButton("Close");
	     confirmButton = new JButton("Send"); 	     
	      
	     messageLabel = new JLabel("");
	     
	     /* Check if the required configuration parameters are set */
	     if(!mailServerConfigDao.configurationIsValid()){    
	    	 
	    	 messageLabel.setText("A required value is missing \n Check mail server configuration !");
	    	 messageLabel.setFont(new Font("Serif", Font.BOLD, 15));
	    	 messageLabel.setForeground(Color.RED);	 
	    	 
	    	 confirmButton.setEnabled(false);
	     }
	        
	     closeButton.addActionListener(this);
	     confirmButton.addActionListener(this);
	     
	     //--------- ADD THE ITEMS ----------
	     this.add(fromLabel,"split 2"); //split the CELL in two part: the next component, will placed in the same cell
	     this.add(fromValue,"growx,gapleft 21");
	     
	     this.add(toLabel,"split 2");
	     this.add(toValue,"growx,gapleft 40"); //size with the pattern: min:preferred:max
	     
	     this.add(subjectLabel,"split 2");
	     this.add(subjectValue,"growx");
	     
	     //text area
	     this.add(messageAreaLabel);
	     this.add(messageValue,"growx,align center");    
	     
	     this.add(closeButton,"split 2,width 120,align center");
         this.add(confirmButton,"width 120,");
         
         this.add(messageLabel,"span 1,align center");         
         this.add(busyLabelPanel,"span 1,align center,gapbottom 20");
	     
	     this.setTitle("Send Mail");
	     this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);	     
	     this.setSize(530,610);  		   
	     this.setVisible(true);	
	}

	
	public void actionPerformed(ActionEvent e) {		
		
		if (e.getSource() instanceof JButton)  
		{
			//close the popup
		    if (e.getActionCommand().equalsIgnoreCase("Close"))
		    {		    	
		   	   this.setVisible(false);
	    	   this.dispose();
		    }
		      
		    //send the email
		    if (e.getActionCommand().equalsIgnoreCase("Send")){	
		    	
		    	final MailSenderAction mailSenderAction = new MailSenderAction();
		    	
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
	       	    
				       	/**
				       	 * Create a new thread (different than the Event Dispatch Thread used by Swing to dispatch event) 
				       	 * because the operation can last some time if the mail server is slow      
				       	 */
			       	    Thread sendMailThread = new Thread() {
		                  
						   public void run() {					                       
						        
						       try{ 		
								     mailSenderAction.send(subjectValue.getText(),messageValue.getText(),fromValue.getText(),toValue.getText());
								    
								     //hide and stop the animation
								     busyLabelPanel.getJxBusyLabel().setBusy(false);
							   		 busyLabelPanel.getJxBusyLabel().setVisible(false);
								    
								     messageLabel.setText("Operation Executed Successfully");
							    	 messageLabel.setFont(new Font("Serif", Font.BOLD, 15));
							    	 messageLabel.setForeground(Color.GREEN);
						     					    
						           } catch (Exception ex) {
										
									  // stop and hide the animation
									  busyLabelPanel.getJxBusyLabel().setBusy(false);
							   		  busyLabelPanel.getJxBusyLabel().setVisible(false);
							   		  
							   		  messageLabel.setText("Error sending the mail !"+ex.getMessage());
							    	  messageLabel.setFont(new Font("Serif", Font.BOLD, 15));
							    	  messageLabel.setForeground(Color.RED);
						           }
			                 }
						 };
						                
						sendMailThread.start(); 		      
		         }				
		    }
		}
		
	}

	public JTextField getFromValue() {
		return fromValue;
	}

	public void setFromValue(JTextField fromValue) {
		this.fromValue = fromValue;
	}

	public JTextField getToValue() {
		return toValue;
	}

	public void setToValue(JTextField toValue) {
		this.toValue = toValue;
	}

	public JTextField getSubjectValue() {
		return subjectValue;
	}

	public void setSubjectValue(JTextField subjectValue) {
		this.subjectValue = subjectValue;
	}

	public JTextArea getMessageValue() {
		return messageValue;
	}

	public void setMessageValue(JTextArea messageValue) {
		this.messageValue = messageValue;
	}

}
