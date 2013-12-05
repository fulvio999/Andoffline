
package andoffline.gui.menu.configuration.database;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import andoffline.integration.database.DatabaseConfigurationManager;
import andoffline.integration.database.DatabaseStructureManager;

/**
 * Show a popUp window where the user can create a Database schema and tables for the previously configured Database
 * using menu 'Database --> Create Configuration'
 * 
 * Is showed choosing the menu entry: 'Database --> Create Schema' in the menu bar
 *
 */
public class DatabaseStructureCreatorPopUp extends JDialog implements ActionListener, ItemListener{
	
	 private static final long serialVersionUID = -1848935925091081071L;
	 
	 private static final Logger log = Logger.getLogger(DatabaseStructureCreatorPopUp.class);
	 
	 /* The target Database name previously set during the DB configuration creation */
	 private JTextField targetDbValue;
	 
	 private JButton closeButton;	
	 private JButton createUpdateButton;	
	 
	 /* A label that show operation results/errors */
	 private JLabel messageLabel;
	 
	 /* configuration manager for the Database */
	 private DatabaseStructureManager databaseStructureManager;
	
	 /**
	  * Constructor
	  */
	 public DatabaseStructureCreatorPopUp() {
			
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Create Database Structure"));			
		buildPopUp();		
	 }

	/**
	 * Utility method that build the popup layout
	 */
	private void buildPopUp(){
		
		this.targetDbValue = new JTextField();
		
		this.setLayout(new MigLayout("wrap 2")); //we want 2 columns
        
        this.add(new JLabel("Create Database schema and tables"),"wrap,span 2,align center");
        this.add(new JLabel("* Database:"),"gapy 20px");         
        this.add(targetDbValue,"width 100"); 
         
        closeButton = new JButton("Close");       
        createUpdateButton = new JButton("Create");     
        
        messageLabel = new JLabel("* Required field");
        
        closeButton.addActionListener(this);
        createUpdateButton.addActionListener(this);
  
   	    //place-holder   	   
   	    this.add(new JLabel(""),"wrap,span 2");
   	    
   	    this.add(closeButton,"width 100:100:100, gapleft 120");    	  
   	    this.add(createUpdateButton,"width 100:100:100, wrap");   	    
   	    this.add(messageLabel,"wrap,span 2,align center");
        
		//set the dialog as a modal window
        this.setModalityType(ModalityType.APPLICATION_MODAL);        	
		
		databaseStructureManager = new DatabaseStructureManager();
		String configuredDbType = "";
		
		try {
			configuredDbType = DatabaseConfigurationManager.getConfiguredDatabaseType();
			log.info("Configured db type:"+configuredDbType);
			
			if(StringUtils.isEmpty(configuredDbType))
			{				
				log.fatal("No target DB configured to create Schema and Tables");
				targetDbValue.setEditable(false);
				targetDbValue.setEnabled(false);				
				createUpdateButton.setEnabled(false);
				
				messageLabel.setForeground(Color.red);
	    		messageLabel.setText("No database configuration found, create it");
	    		
			}else{  //found a configured target DB				
				targetDbValue.setText(configuredDbType); 
				targetDbValue.setEditable(false);
				targetDbValue.setEnabled(false);
			}
			
		} catch (Exception e) {
			log.fatal("Error looking for configured target DB to create Schema and Tables, cause: ",e);
			targetDbValue.setEditable(false);
			targetDbValue.setEnabled(false);
			
			messageLabel.setForeground(Color.red);
    		messageLabel.setText("No database configuration found, create it");
		}	
		
		this.setTitle("Create the database schema and tables");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);       
        this.setSize(550,270);  
        this.setResizable(false);
        this.setVisible(true);	
	}

	/**
	 * Manage the selection event on the Database type chooser combo box and buttons of this popup window
	 */
	public void actionPerformed(ActionEvent e) {
		
	   try{		   
		   if (e.getSource() instanceof JButton)  
		   {		     
		      if (e.getActionCommand().equalsIgnoreCase("Close")) {		    		  
		    	  this.setVisible(false);
	    		  this.dispose();
		      }
		      
		      // pressed button 'Create': create schema and tables 
		      if (e.getActionCommand().equalsIgnoreCase("Create")) {		    	  
		    	  
		    	 String dbType = targetDbValue.getText();  
		    	 log.info("DB type where create schema: "+dbType);
		    	  
		    	 if(!StringUtils.isEmpty(dbType))
		    	 {   		
		    		 //check if the DB configuration found in the properties file is correct
		    		 if(databaseStructureManager.isDatabaseConfigurationPresent(dbType))
		    		 {		    		 
			    		 if(databaseStructureManager.createTable(dbType))
			    		 {		    		 	
			    		    messageLabel.setForeground(Color.green);
			    		    messageLabel.setText("Schema created successfully !");
			    		 }else{
			    			messageLabel.setForeground(Color.red);
				    		messageLabel.setText("Error creating DB, see logs !"); 
			    		 }	
			    		 
		    		 }else{
		    			 messageLabel.setForeground(Color.red);
			    		 messageLabel.setText("No database configuration found, create it");  
		    		 }
		    		 
		    	  }else{
		    		 messageLabel.setForeground(Color.red);
		    		 messageLabel.setText("Invalid database name or configuration !"); 
		    	  }
		      }		     
		}
       
		}catch (Exception ex) {
			log.fatal("Error saving/loading DB configuration, cause: ",ex);
			
			createUpdateButton.setEnabled(false);			
			messageLabel.setForeground(Color.red);
			messageLabel.setText("Error saving/loading DB configuration, (see log file)");
		}		
	  
	}	

	public void itemStateChanged(ItemEvent e) {		
		
	}

	public JLabel getMessageLabel() {
		return messageLabel;
	}

	public void setMessageLabel(JLabel messageLabel) {
		this.messageLabel = messageLabel;
	}

}
