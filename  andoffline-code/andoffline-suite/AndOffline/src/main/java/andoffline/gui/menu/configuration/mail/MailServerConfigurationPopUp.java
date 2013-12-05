package andoffline.gui.menu.configuration.mail;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import andoffline.integration.mailserver.dao.MailServerConfigurationDao;
import andoffline.utility.validation.TextFieldValidator;

/**
 * Create a popup window where the user can create/edit a configuration to connect with an EXTERNAL
 * mail server to send email (eg GMail)
 * 
 * the configuration is stored in the file conf/mail.properties
 *
 */
public class MailServerConfigurationPopUp extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(MailServerConfigurationPopUp.class);
	
	/* The dedicated DAO to read/write the configuration for mail server connection */
	private MailServerConfigurationDao mailServerConfigDao;

	private JTextField mailServerAddress;
	private JTextField mailServerPort;
	private JTextField mailServerPassword;
	private JTextField mailServerUser;
	
	// the mail address associated at the account
	private JTextField mailSenderAddress; 
	
	private JLabel mailServerAddressLabel;
	private JLabel mailServerPortLabel;
	private JLabel mailServerPasswordLabel;
	private JLabel mailServerUserLabel;
	private JLabel mailSenderAddressLabel; 
		
	private JButton closeButton;
	private JButton saveButton;
	
	/* A label that show operations results/errors */
	private JLabel messageLabel;
	
	/* The validator for the input values inserted in this panel */
	private TextFieldValidator verifier = new TextFieldValidator();

	/**
	 * Constructor
	 */
	public MailServerConfigurationPopUp() {
		
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Mail Server Settings"));
		
		// load the saved values (if any) in the configuration file placed in the "conf" folder to fill the text fields
		mailServerConfigDao = new MailServerConfigurationDao();
		mailServerConfigDao.readConfiguration();
		
		this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setTitle("Email Configuration");         
        this.setLayout(new MigLayout("wrap 2")); //say that we want 2 columns
        
        mailServerAddressLabel = new JLabel("* Mail Server Address:");
    	mailServerPortLabel = new JLabel("* Mail Server Port:");
    	mailServerPasswordLabel = new JLabel("* Mail Server Password");
    	mailServerUserLabel = new JLabel("* Mail Server User:");
    	mailSenderAddressLabel = new JLabel("* Sender email:");
        
        mailServerAddress = new JTextField("");
        mailServerAddress.setText(mailServerConfigDao.getMailServerAddress());
        
        // when the component lose the focus is called the verifier associated at this component
        // Focus is transfered only if the validate method returns true
        mailServerAddress.setInputVerifier(verifier);
        
        mailServerPort = new JTextField("");
        mailServerPort.setText(mailServerConfigDao.getMailServerPort());
        
        mailServerPassword = new JTextField("");
        mailServerPassword.setText(mailServerConfigDao.getMailServerPassword());
        
        mailServerUser = new JTextField(""); 
        mailServerUser.setText(mailServerConfigDao.getMailServerUser());
        
        mailSenderAddress = new JTextField(""); 
        mailSenderAddress.setText(mailServerConfigDao.getMailSenderAddress());
      
        messageLabel = new JLabel("* Required field");
        
        closeButton = new JButton("Close");
        saveButton = new JButton("Save"); 
        
        closeButton.addActionListener(this);
        saveButton.addActionListener(this);
         
        this.add(new JLabel("Set the Mail server configuration parameters"),"wrap,span 2,align center");
        
        this.add(mailServerAddressLabel,"gapy 20px"); 
        this.add(mailServerAddress,"width 200:300:300, wrap");        
        
        this.add(mailServerPortLabel,"gapy 20px");
        this.add(mailServerPort,"width 200:300:300, wrap");
        
        this.add(mailServerPasswordLabel,"gapy 20px");
        this.add(mailServerPassword,"width 200:300:300, wrap");
        
        this.add(mailServerUserLabel,"gapy 20px");
        this.add(mailServerUser,"width 200:300:300, wrap");
        
        this.add(mailSenderAddressLabel,"gapy 20px");
        this.add(mailSenderAddress,"width 200:300:300, wrap");        
        
        //place-holder   	   
   	    this.add(messageLabel,"wrap,span 2,align center");
   	    
   	    this.add(closeButton,"width 100:100:100, gapleft 100");   	      	    
   	    this.add(saveButton,"width 100:100:100, wrap");   	   
         
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);        
        this.setSize(450,430);  			
        this.setVisible(true);		
	}
	

	/**
	 * Manage the action on the two buttons
	 * - close popup
	 * - save new configuration
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		
		   //true if user has pressed some button 
		   if (e.getSource() instanceof JButton)  
		   {
		      //close the popup
		      if (e.getActionCommand().equalsIgnoreCase("Close")){		    		  
		    	  this.setVisible(false);
	    		  this.dispose();
		      }
		      //save/update the configuration to conf/mail.properties file 
		      if (e.getActionCommand().equalsIgnoreCase("Save"))
		      {		    	  
		    	 String serverAddress = mailServerAddress.getText();
		    	 String port = mailServerPort.getText();
		    	 String password = mailServerPassword.getText();
		    	 String user = mailServerUser.getText();
		    	 String senderAddress = mailSenderAddress.getText();
		    	  
		    	 if(mailServerConfigDao.storeConfiguration(serverAddress, port, password, user, senderAddress))
		    	 {		    	  
		    		 messageLabel.setForeground(Color.green);
		    		 messageLabel.setText("Configuration Saved Successfully !");
				    	
		    		 log.info("Mail Server Configuration Saved successfully !");
		    	 }else{
		    		 
		    		 messageLabel.setForeground(Color.red);
		    		 messageLabel.setText("Error saving Mail Server Configuration !");
		    	 }
		       } 
		    	
	      }
	}	
		
	public MailServerConfigurationDao getMailServerConfigDao() {
		return mailServerConfigDao;
	}

	public void setMailServerConfigDao(
			MailServerConfigurationDao mailServerConfigDao) {
		this.mailServerConfigDao = mailServerConfigDao;
	}


}
