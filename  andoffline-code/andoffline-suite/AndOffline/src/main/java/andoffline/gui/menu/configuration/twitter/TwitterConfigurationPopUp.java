package andoffline.gui.menu.configuration.twitter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import andoffline.integration.twitter.dao.TwitterConfigurationDao;

/**
 * Create a popUp window where the user can configure the parameters for Twitter integration (OAuth authentication)
 *
 */
public class TwitterConfigurationPopUp extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(TwitterConfigurationPopUp.class);
	
	private JTextField oauthConsumerKeyField;
	private JTextField oauthConsumerSecretField;
	
	private JTextField oauthAccessTokenField;
	private JTextField	oauthAccessTokenSecretField;
	
	private JButton closeButton;
	private JButton saveButton;
	
	/* A label that show operation results/errors */
	private JLabel messageLabel;
	 
	/* The dedicated object to read/write the configuration for the  Twitter integration */
	private TwitterConfigurationDao twitterConfigurationDao;
	
	/**
	 * Constructor
	 */
	public TwitterConfigurationPopUp(){
		
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Twitter Settings"));
		
		// load the saved values (if any) in the configuration file placed in the "conf" folder to fill the text fields
		twitterConfigurationDao = new TwitterConfigurationDao();
		twitterConfigurationDao.readConfiguration();	
		
		this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setTitle("Twitter Configuration");         
        this.setLayout(new MigLayout("wrap 2")); //say that we want 2 columns
        
        oauthConsumerKeyField = new JTextField(twitterConfigurationDao.getOauthConsumerKey());
        oauthConsumerSecretField = new JTextField(twitterConfigurationDao.getOauthConsumerSecret());
        
        oauthAccessTokenField = new JTextField(twitterConfigurationDao.getOauthAccessToken());
    	oauthAccessTokenSecretField = new JTextField(twitterConfigurationDao.getOauthAccessTokenSecret());
        
        closeButton = new JButton("Close");
        saveButton = new JButton("Save"); 
        
        closeButton.addActionListener(this);
        saveButton.addActionListener(this);
        
        messageLabel = new JLabel("");
        
        this.add(new JLabel("Set the Twitter configuration parameters"),"wrap,span 2,align center");
        
        this.add(new JLabel("oauthConsumerKey:"),"gapy 20px"); 
        this.add(oauthConsumerKeyField,"width 200:300:300, wrap");        
        
        this.add(new JLabel("oauthConsumerSecret."),"gapy 20px");
        this.add(oauthConsumerSecretField,"width 200:300:300, wrap");        
        
        this.add(new JLabel("oauthAccessToken:"),"gapy 20px"); 
        this.add(oauthAccessTokenField,"width 200:300:300, wrap"); 
        
        this.add(new JLabel("oauthAccessTokenSecret:"),"gapy 20px"); 
        this.add(oauthAccessTokenSecretField,"width 200:300:300, wrap"); 
        
        //place-holder   	   
   	    this.add(new JLabel(""),"wrap,span 2");
   	    
   	    this.add(closeButton,"width 100:100:100, gapleft 120");   	      	    
   	    this.add(saveButton,"width 100:100:100, wrap");   
   	    this.add(messageLabel,"wrap,span 2,align center");
         
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(450,400);  			
        this.setVisible(true);	
	}	


	/**
	 * Manage the Close/Save action on the button
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
		      //save/update the configuration to conf/twitter4j.properties file 
		      if (e.getActionCommand().equalsIgnoreCase("Save")){
		    	  
		    	  String oauthConsumerKey = oauthConsumerKeyField.getText();
		    	  String oauthConsumerSecret = oauthConsumerSecretField.getText();
		    	  
		    	  String oauthAccessToken = oauthAccessTokenField.getText();
		    	  String oauthAccessTokenSecret = oauthAccessTokenSecretField.getText();
		    	  
		    	  if(checkInput(oauthConsumerKey,oauthConsumerSecret)){
		    		  
			    	    if(twitterConfigurationDao.storeConfiguration(oauthConsumerKey, oauthConsumerSecret,oauthAccessToken, oauthAccessTokenSecret)){
			    	    	messageLabel.setForeground(Color.green);
							messageLabel.setText("Configuration Saved Successfully !");
							
			    	    }else{
			    	    	messageLabel.setForeground(Color.red);
							messageLabel.setText("Error saving Configuration! (See log file)");
			    	    }
				    	
		    	  }else{ 
				    	messageLabel.setForeground(Color.red);
						messageLabel.setText("Invalid input, empty value not allowed !");
		    	  }	
		    	  
		    	  log.info("Twitter Configuration Saved successfully !");
		      }
		  }			
	}
	
	
	/**
	 * Utility method that validate the input of the user
	 * 
	 * @param oauthConsumerKey
	 * @param oauthConsumerSecret
	 * @return true if the input is valid (ie both the parameters are not null)
	 */
	private boolean checkInput(String oauthConsumerKey, String oauthConsumerSecret){
		
		if(StringUtils.isNotEmpty(oauthConsumerKey) && StringUtils.isNotEmpty(oauthConsumerSecret))
			return true;
		else
			return false;
	}


	public JButton getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(JButton closeButton) {
		this.closeButton = closeButton;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(JButton saveButton) {
		this.saveButton = saveButton;
	}

	public JLabel getMessageLabel() {
		return messageLabel;
	}

	public void setMessageLabel(JLabel messageLabel) {
		this.messageLabel = messageLabel;
	}

	public JTextField getOauthConsumerKeyField() {
		return oauthConsumerKeyField;
	}


	public void setOauthConsumerKeyField(JTextField oauthConsumerKeyField) {
		this.oauthConsumerKeyField = oauthConsumerKeyField;
	}


	public JTextField getOauthConsumerSecretField() {
		return oauthConsumerSecretField;
	}


	public void setOauthConsumerSecretField(JTextField oauthConsumerSecretField) {
		this.oauthConsumerSecretField = oauthConsumerSecretField;
	}


	public TwitterConfigurationDao getTwitterConfigurationDao() {
		return twitterConfigurationDao;
	}


	public void setTwitterConfigurationDao(
			TwitterConfigurationDao twitterConfigurationDao) {
		this.twitterConfigurationDao = twitterConfigurationDao;
	}


	public JTextField getOauthAccessTokenField() {
		return oauthAccessTokenField;
	}


	public void setOauthAccessTokenField(JTextField oauthAccessTokenField) {
		this.oauthAccessTokenField = oauthAccessTokenField;
	}


	public JTextField getOauthAccessTokenSecretField() {
		return oauthAccessTokenSecretField;
	}


	public void setOauthAccessTokenSecretField(
			JTextField oauthAccessTokenSecretField) {
		this.oauthAccessTokenSecretField = oauthAccessTokenSecretField;
	}



}
