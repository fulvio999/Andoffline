
package andoffline.gui.menu.configuration.database;

import java.awt.Color;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import andoffline.integration.database.DatabaseConfigurationManager;
import andoffline.integration.database.bean.DatabaseConfigurationBean;

/**
 * Show a popUp where the user can: 
 * - load a PREVIOUSLY stored database configuration to edit/update it
 * 
 * Is showed choosing the menu entry: 'Database --> Configuration' in the menu bar
 *
 */
public class DatabaseConfigEditorPopUp extends JDialog implements ActionListener, ItemListener{
	
	 private static final long serialVersionUID = -1848935925091081071L;
	 
	 private static final Logger log = Logger.getLogger(DatabaseConfigEditorPopUp.class);
	
	 private JTextField dataBaseType;
	 
	 private JLabel dbDriverLabel;
	 private JLabel dbNameLabel;
	 private JLabel dbPathLabel; //used only for sqlite db
	 private JLabel dbUserLabel;
	 private JLabel dbPasswordLabel; 	
	 private JLabel dbPortLabel;
	 private JLabel dbHostLabel;
	 
	 private JTextField dbDriverValue;
	 private JTextField dbNameValue;
	 private JTextField dbPathValue; //used only for sqlite db
	 private JTextField dbUserValue;
	 private JTextField dbPasswordValue; 	
	 private JTextField dbPortValue;
	 private JTextField dbHostValue;	 
	 
	 private JButton closeButton;
	 private JButton updateConfigurationButton;		
	 
	 /* A label that show operation results/errors */
	 private JLabel messageLabel;
	 
	 /* Save configuration for the Database */
	 private DatabaseConfigurationManager databaseConfigurationManager;
	
	 /**
	  * Constructor
	  */
	 public DatabaseConfigEditorPopUp() {
			
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Edit database Settings"));			
		buildPopUp();		
	 }

	/**
	 * Utility method that build the popup layout
	 */
	private void buildPopUp(){
		
		dataBaseType = new JTextField();
		dataBaseType.setEditable(false);
		dataBaseType.setEnabled(false);
       
        this.setLayout(new MigLayout("wrap 2")); //we want 2 columns
        
        this.add(new JLabel("Edit current database configuration"),"wrap,span 2,align center");
        this.add(new JLabel("* Database Type:"),"gapy 20px");
        this.add(dataBaseType,"width 100,wrap"); 
        
        dbDriverLabel = new JLabel("* Driver:"); 
   	    dbNameLabel = new JLabel("* DB Name:");
   	    dbPathLabel = new JLabel("* DB File system path:");
   	    dbUserLabel = new JLabel("User:");
   	    dbPasswordLabel = new JLabel("Password:");  	 
   	    dbPortLabel = new JLabel("Port:");
   	    dbHostLabel = new JLabel("Host:");   
   	    
   	    dbDriverValue = new JTextField("");   	
   	    dbDriverValue.setEditable(false); //the jdbc driver have a fix value for each db
   	    dbDriverValue.setEnabled(false);
   	
   	    dbNameValue = new JTextField("");
   	    dbPathValue = new JTextField(""); //used only for sqlite databases
        dbUserValue = new JTextField("");
        dbPasswordValue = new JTextField(""); 
        dbPortValue = new JTextField("");
        dbHostValue = new JTextField("");        
        
        closeButton = new JButton("Close");       
        updateConfigurationButton = new JButton("Update");      
        
        messageLabel = new JLabel("* Required field");
        
        closeButton.addActionListener(this);
        updateConfigurationButton.addActionListener(this);
        
        // try to fill the text fields using the configured connection        
        databaseConfigurationManager = new DatabaseConfigurationManager();
        
		/* check if an existing DB configuration is present: if true fill the text fields */
		try{
			String configuredDbType = DatabaseConfigurationManager.getConfiguredDatabaseType();
			
			if(! StringUtils.isEmpty(configuredDbType))  //a previously stored configuration exist
			{ 				
				DatabaseConfigurationBean databaseConfigurationBean = databaseConfigurationManager.loadDatabaseConfiguration(configuredDbType);
				
				dataBaseType.setText(configuredDbType);
				dbDriverValue.setText(databaseConfigurationBean.getDbDriver());
				dbNameValue.setText(databaseConfigurationBean.getDbName());
				
				if(configuredDbType.equalsIgnoreCase("SQLITE"))
				   dbPathValue.setText(databaseConfigurationBean.getDbPath()); //only for sqlite
					
				if(configuredDbType.equalsIgnoreCase("MYSQL"))
				{					
				  dbUserValue.setText(databaseConfigurationBean.getDbUser());
				  dbPasswordValue.setText(databaseConfigurationBean.getDbPassword());
				  dbPortValue.setText(databaseConfigurationBean.getDbPort());
				  dbHostValue.setText(databaseConfigurationBean.getDbHost());
				  
				  dbPathValue.setEnabled(false);
				  dbPathValue.setEditable(false);
					
				}else{  //sqlite
					
				  dbUserValue.setEnabled(false);
				  dbPasswordValue.setEnabled(false);
				  dbPortValue.setEnabled(false);
				  dbHostValue.setEnabled(false);
				}
				
			}else{ // NO existing configuration found, create it with Configuration --> Database --> Create Configuration
				
				log.info("No database configuration found for editing");							
				this.disableAllInput();	
				
				messageLabel.setForeground(Color.red);
    			messageLabel.setText("No configuration found, please create it !");
			}
			
		}catch (Exception e) {
			log.error("Error loading DB configuration for editing, cause: "+e);
			this.disableAllInput();	
			
			messageLabel.setForeground(Color.red);
			messageLabel.setText("No configuration found, please create it !");
		}
        
        //--------------------------------
   	    
   	    this.add(dbDriverLabel);
   	    //specify the component size with the pattern: min:preferred:max 
   	    this.add(dbDriverValue,"width 200:200:200, wrap");   
   	 
   	    this.add(dbNameLabel);
   	    this.add(dbNameValue,"width 100:150:200, wrap");
   	    
   	    this.add(dbPathLabel);
	    this.add(dbPathValue,"width 200:200:200, wrap");
   	      	 
   	    this.add(dbUserLabel);
   	    this.add(dbUserValue,"width 100:150:200, wrap");   	   
   	    
   	    this.add(dbPasswordLabel);
   	    this.add(dbPasswordValue,"wrap,width 100:100:150");   	  
   	 
   	    this.add(dbPortLabel);
   	    this.add(dbPortValue,"wrap,width 100:100:150");   	   
   	 
   	    this.add(dbHostLabel);
   	    this.add(dbHostValue,"width 200:200:200, wrap");   
 
   	    //place-holder   	   
   	    this.add(new JLabel(""),"wrap,span 2");
   	    
   	    this.add(closeButton,"width 100:100:100, gapleft 120");   	  
   	    this.add(updateConfigurationButton,"width 100:100:100, wrap");   	    
   	    this.add(messageLabel,"wrap,span 2,align center");
        
		//set the dialog as a modal window
        this.setModalityType(ModalityType.APPLICATION_MODAL);

        this.setTitle("Edit a database configuration");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);       
        this.setSize(550,470);  
        this.setResizable(false);
        this.setVisible(true);	
	}

	
	/**
	 * Utility that disable alla the input text of the gui
	 */
	private void disableAllInput()
	{		
		this.dataBaseType.setEditable(false);
		this.dataBaseType.setEnabled(false);
		
		this.dbDriverValue.setEditable(false);
		this.dbDriverValue.setEnabled(false);
		
		this.dbNameValue.setEditable(false);
		this.dbNameValue.setEnabled(false);
		
		this.dbPathValue.setEditable(false);
		this.dbPathValue.setEnabled(false);
		
		this.dbUserValue.setEditable(false);
		this.dbUserValue.setEnabled(false);
		
		this.dbPasswordValue.setEditable(false);
		this.dbPasswordValue.setEnabled(false);
		
		this.dbPortValue.setEditable(false);
		this.dbPortValue.setEnabled(false);
		
		this.dbHostValue.setEditable(false);
		this.dbHostValue.setEnabled(false);
		
		this.updateConfigurationButton.setEnabled(false);
	}
	
	
	/**
	 * Manage the selection event on the Database combo box and buttons of this popup window
	 */
	public void actionPerformed(ActionEvent e) {
		
	   try{			   
		   if (e.getSource() instanceof JButton)  
		   {		     
		      if (e.getActionCommand().equalsIgnoreCase("Close")){		    		  
		    	  this.setVisible(false);
	    		  this.dispose();
		      }
		      
		      // pressed the button 'Update': update the DB configuration to conf/database.properties file 
		      if (e.getActionCommand().equalsIgnoreCase("Update")) 
		      {		    	  
		    	 //String dbType = (String)dataBaseCombo.getSelectedItem();
		    	 String dbType = dataBaseType.getText();
		    	  
		    	 if(this.inputValid(dbType))
		    	 {	    		 
		    		 DatabaseConfigurationBean databaseConfigurationBean = new DatabaseConfigurationBean();	
		    		 databaseConfigurationBean.setDbType(dbType.toLowerCase());
		    		 databaseConfigurationBean.setDbDriver(dbDriverValue.getText());
		    		 databaseConfigurationBean.setDbName(dbNameValue.getText());
		    			
		    		 if(dbType.equalsIgnoreCase("SQLITE"))
		    		    databaseConfigurationBean.setDbPath(dbPathValue.getText());

		    		 databaseConfigurationBean.setDbUser(dbUserValue.getText());
		    		 databaseConfigurationBean.setDbPassword(dbPasswordValue.getText());
		    		 databaseConfigurationBean.setDbPort(dbPortValue.getText());
		    		 databaseConfigurationBean.setDbHost(dbHostValue.getText());
		    		 
		    		 if(databaseConfigurationManager.saveOrUpdateDatabaseConfiguration(databaseConfigurationBean)){		    		 	
		    			 messageLabel.setForeground(Color.green);
		    			 messageLabel.setText("Configuration Saved Successfully. (Can be necessary create structure)");
		    		 }else{
		    			 messageLabel.setForeground(Color.red);
		    			 messageLabel.setText("Error saving configuration. See log File !");
		    		 }
		    			 
		    	 }else{
		    		 messageLabel.setForeground(Color.red);
		    		 messageLabel.setText("Some values are invalid !"); 
		    	 }
		      }		     
		   }	  
			
		   // Manage the visibility of the fields and fill the fields with default values according with the chosen DB 
		   if(e.getSource() instanceof JComboBox)
		   {				   
		        ItemSelectable is = (ItemSelectable)e.getSource();
		        Object selected[] = is.getSelectedObjects();
		        String dbChosen = (String)selected[0];	//the chosen value in the combo box
		        
		        //according with the DB chosen load the configuration from the file
		        DatabaseConfigurationBean databaseConfigurationBean = databaseConfigurationManager.loadDatabaseConfiguration(dbChosen);
		        
		        if(!dbChosen.equalsIgnoreCase("---"))
		        	updateConfigurationButton.setEnabled(true);
		        else
		        	updateConfigurationButton.setEnabled(false);
		        
		        //fill the fields.... and manage the visibility according with the DB chosen
		        
		        if(dbChosen.equalsIgnoreCase("SQLITE")) //for sqlite only driver and db-name are necessary
		        {   
		        	//update label adding a '*' sign
		        	dbUserLabel.setText("User");
		        	dbPortLabel.setText("Port");		        	
		        	dbHostLabel.setText("Host");
		        	
		        	dbDriverValue.setText(databaseConfigurationBean.getDbDriver());
		        	dbNameValue.setText(databaseConfigurationBean.getDbName());
		        	dbPathValue.setText(databaseConfigurationBean.getDbPath());
		        	
		        	dbPathValue.setEnabled(true);
		        	dbPathValue.setEditable(true);
		        	
			    	dbUserValue.setEditable(false);
			    	dbUserValue.setEnabled(false);
			    	dbUserValue.setText(""); //empty old value
			    	  
			    	dbPasswordValue.setEditable(false);
			    	dbPasswordValue.setEnabled(false);
			    	dbPasswordValue.setText("");
			    	  
			    	dbPortValue.setEditable(false);
			    	dbPortValue.setEnabled(false);
			    	dbPortValue.setText("");
			    	  
			    	dbHostValue.setEditable(false);
			    	dbHostValue.setEnabled(false);
			    	dbHostValue.setText("");
		
		        }else{   //MySQL   	
		        	
		        	//update label adding a '*' sign	       	
		        	dbUserLabel.setText("* User");
		        	dbPortLabel.setText("* Port");
		        	//password can be empty
		        	dbHostLabel.setText("* Host");
		        	
		        	dbDriverValue.setText(databaseConfigurationBean.getDbDriver());
		        	dbNameValue.setText(databaseConfigurationBean.getDbName());		        	
		        	dbUserValue.setText(databaseConfigurationBean.getDbUser());
		        	dbPasswordValue.setText(databaseConfigurationBean.getDbPassword());
		        	dbPortValue.setText(databaseConfigurationBean.getDbPort());
		        	dbHostValue.setText(databaseConfigurationBean.getDbHost());
		        	
		        	//disable fields used only for sqlite
		        	dbPathValue.setEnabled(false);
		        	dbPathValue.setEditable(false);
		        	
		        	dbUserValue.setEditable(true);
			    	dbUserValue.setEnabled(true);
			    	  
			    	dbPasswordValue.setEditable(true);
			    	dbPasswordValue.setEnabled(true);
			    	  
			    	dbPortValue.setEditable(true);
			    	dbPortValue.setEnabled(true);
			    	  
			    	dbHostValue.setEditable(true);
			    	dbHostValue.setEnabled(true);
		        }		             
		   }
        
		}catch (Exception ex) {
			log.fatal("Error saving/updating DB configuration, cause: ",ex);
			
			updateConfigurationButton.setEnabled(false);			
			messageLabel.setForeground(Color.red);
			messageLabel.setText("Error saving/loading DB configuration, (see log file)");
		}		
	  
	}	
	
	/**
	 * Utility method that validate the user input according with the chosen database
	 * @return true if the input is valid (the required fields are filled)
	 */
	private boolean inputValid(String dbType){
		
		if(dbType.equalsIgnoreCase("MYSQL")) //only driver and db-name are necessary
        {
			if(StringUtils.isNotEmpty(dbDriverValue.getText()) && 
			   StringUtils.isNotEmpty(dbNameValue.getText())   &&
			   StringUtils.isNotEmpty(dbUserValue.getText())   &&
			   StringUtils.isNotEmpty(dbPortValue.getText())   &&
			   StringUtils.isNotEmpty(dbHostValue.getText())
			   )
   		       return true;
			else
			  return false;
			
        }else if(dbType.equalsIgnoreCase("SQLITE")){
        	
        	if(StringUtils.isNotEmpty(dbDriverValue.getText()) && 
        	   StringUtils.isNotEmpty(dbNameValue.getText())   &&
        	   StringUtils.isNotEmpty(dbPathValue.getText())
        	  )		
        	  return true;
			else
			  return false;
       	
        }else
        	return false;		
	}
	

	public void itemStateChanged(ItemEvent e) {		
		
	}

	public JLabel getDbDriverLabel() {
		return dbDriverLabel;
	}

	public void setDbDriverLabel(JLabel dbDriverLabel) {
		this.dbDriverLabel = dbDriverLabel;
	}

	public JLabel getDbNameLabel() {
		return dbNameLabel;
	}

	public void setDbNameLabel(JLabel dbNameLabel) {
		this.dbNameLabel = dbNameLabel;
	}

	public JLabel getDbUserLabel() {
		return dbUserLabel;
	}

	public void setDbUserLabel(JLabel dbUserLabel) {
		this.dbUserLabel = dbUserLabel;
	}

	public JLabel getDbPasswordLabel() {
		return dbPasswordLabel;
	}

	public void setDbPasswordLabel(JLabel dbPasswordLabel) {
		this.dbPasswordLabel = dbPasswordLabel;
	}

	public JLabel getDbPortLabel() {
		return dbPortLabel;
	}

	public void setDbPortLabel(JLabel dbPortLabel) {
		this.dbPortLabel = dbPortLabel;
	}

	public JLabel getDbHostLabel() {
		return dbHostLabel;
	}

	public void setDbHostLabel(JLabel dbHostLabel) {
		this.dbHostLabel = dbHostLabel;
	}

	public JTextField getDbDriverValue() {
		return dbDriverValue;
	}

	public void setDbDriverValue(JTextField dbDriverValue) {
		this.dbDriverValue = dbDriverValue;
	}

	public JTextField getDbNameValue() {
		return dbNameValue;
	}

	public void setDbNameValue(JTextField dbNameValue) {
		this.dbNameValue = dbNameValue;
	}

	public JTextField getDbUserValue() {
		return dbUserValue;
	}

	public void setDbUserValue(JTextField dbUserValue) {
		this.dbUserValue = dbUserValue;
	}

	public JTextField getDbPasswordValue() {
		return dbPasswordValue;
	}

	public void setDbPasswordValue(JTextField dbPasswordValue) {
		this.dbPasswordValue = dbPasswordValue;
	}

	public JTextField getDbPortValue() {
		return dbPortValue;
	}

	public void setDbPortValue(JTextField dbPortValue) {
		this.dbPortValue = dbPortValue;
	}

	public JTextField getDbHostValue() {
		return dbHostValue;
	}

	public void setDbHostValue(JTextField dbHostValue) {
		this.dbHostValue = dbHostValue;
	}

	public JLabel getMessageLabel() {
		return messageLabel;
	}

	public void setMessageLabel(JLabel messageLabel) {
		this.messageLabel = messageLabel;
	}

	public JTextField getDataBaseType() {
		return dataBaseType;
	}

	public void setDataBaseType(JTextField dataBaseType) {
		this.dataBaseType = dataBaseType;
	}

	public JTextField getDbPathValue() {
		return dbPathValue;
	}

	public void setDbPathValue(JTextField dbPathValue) {
		this.dbPathValue = dbPathValue;
	}

	public JButton getUpdateConfigurationButton() {
		return updateConfigurationButton;
	}

	public void setUpdateConfigurationButton(JButton updateConfigurationButton) {
		this.updateConfigurationButton = updateConfigurationButton;
	}

}
