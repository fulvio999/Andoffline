
package andoffline.gui.menu.configuration.database;

import java.awt.Color;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import andoffline.constant.gui.GuiParametersEnum;
import andoffline.integration.database.DatabaseConfigurationManager;
import andoffline.integration.database.bean.DatabaseConfigurationBean;
import andoffline.utility.filefilter.FolderFileFilter;

/**
 * Show a popUp where the user can:
 * - create a Database configuration that will be stored in 'conf/database.properties' file* 
 * 
 * Is showed choosing the menu entry: 'Database --> Configuration' in the menu bar
 *
 */
public class DatabaseConfigCreatorPopUp extends JDialog implements ActionListener, ItemListener{
	
	 private static final long serialVersionUID = -1848935925091081071L;
	 
	 private static final Logger log = Logger.getLogger(DatabaseConfigCreatorPopUp.class);
	 
	 private JComboBox dataBaseCombo;
	 
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
	 private JButton dbPathBrowseButton; //used only for sqlite db
	 private JTextField dbUserValue;
	 private JTextField dbPasswordValue; 	
	 private JTextField dbPortValue;
	 private JTextField dbHostValue;	 
	 
	 private JButton closeButton;	
	 private JButton createUpdateButton;	
	 
	 /* A label that show operation results/errors */
	 private JLabel messageLabel;
	 
	 /* Save configuration for the Database */
	 private DatabaseConfigurationManager databaseConfigurationManager;

	 /* Values for the combo box with supported databases */
	 private static String[] databasesSupported = { "---", "MySQL", "SQLITE"};
	
	 /**
	  * Constructor
	  */
	 public DatabaseConfigCreatorPopUp() {
			
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Create Database Settings"));			
		buildPopUp();		
	 }

	/**
	 * Utility method that build the popup layout
	 */
	private void buildPopUp(){
		
		databaseConfigurationManager = new DatabaseConfigurationManager();
		
		dataBaseCombo = new JComboBox(databasesSupported); 
		dataBaseCombo.addActionListener(this);		
       
        this.setLayout(new MigLayout("wrap 2")); //we want 2 columns
        
        this.add(new JLabel("Create a NEW Database configuration"),"wrap,span 2,align center");
        this.add(new JLabel("* Database:"),"gapy 20px");
        this.add(dataBaseCombo,"wrap");   //go next line  
        
        dbDriverLabel = new JLabel("* Driver:"); 
   	    dbNameLabel = new JLabel("* DB Name:");
   	    dbPathLabel = new JLabel("* DB File path:"); //used only for sqlite databases
   	    dbUserLabel = new JLabel("User:");
   	    dbPasswordLabel = new JLabel("Password:");  	 
   	    dbPortLabel = new JLabel("Port:");
   	    dbHostLabel = new JLabel("Host:");   
   	    
   	    dbDriverValue = new JTextField("");   	
   	    dbDriverValue.setEditable(false); //the jdbc driver have a fix value for each db
   	    dbDriverValue.setEnabled(false);
   	
   	    dbNameValue = new JTextField("");
   	    
   	    /* used only for sqlite databases */
   	    dbPathValue = new JTextField(""); 
   	    dbPathValue.setToolTipText("Choose a folder where place Database file");
   	    dbPathBrowseButton = new JButton("Browse");
   	    dbPathBrowseButton.addActionListener(this);
   	    
        dbUserValue = new JTextField("");
        dbPasswordValue = new JTextField(""); 
        dbPortValue = new JTextField("");
        dbHostValue = new JTextField("");
         
        closeButton = new JButton("Close");       
        createUpdateButton = new JButton("Save");      
        createUpdateButton.setEnabled(false); //will be enabled only when the user has choose a target DB
        
        messageLabel = new JLabel("* Required field");
        
        closeButton.addActionListener(this);
        createUpdateButton.addActionListener(this);
   	    
   	    this.add(dbDriverLabel);
   	    //specify the component size with the pattern: min:preferred:max 
   	    this.add(dbDriverValue,"width 200:200:200, wrap");   
   	 
   	    this.add(dbNameLabel);
   	    this.add(dbNameValue,"width 100:150:200, wrap");
   	    
   	    /* used only for sqlite DB */
   	    this.add(dbPathLabel);
	    this.add(dbPathValue,"width 200:200:200,split 2");
	    this.add(dbPathBrowseButton,"width 100,wrap");
   	      	 
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
   	    this.add(createUpdateButton,"width 100:100:100, wrap");   	    
   	    this.add(messageLabel,"wrap,span 2,align center");        
   	   
   	    disableAllWidget();
   	    
		//set the dialog as a modal window
        this.setModalityType(ModalityType.APPLICATION_MODAL);

        this.setTitle("Create a database configuration");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);       
        this.setSize(550,470);  
        this.setResizable(false);
        this.setVisible(true);	
	}

	/**
	 * Utility method that disable all the fields, except the DB type chooser combo. 
	 * They will enable according with chosen DB type
	 */
	private void disableAllWidget(){
		
	  dbDriverValue.setEditable(false);
	  dbDriverValue.setEnabled(false);
	  
	  dbNameValue.setEditable(false);
	  dbNameValue.setEnabled(false);
	  
	  dbPathValue.setEditable(false); 
	  dbPathValue.setEnabled(false);
	  
	  dbPathBrowseButton.setEnabled(false);
	  dbPathBrowseButton.setEnabled(false);
	  
	  dbUserValue.setEditable(false);
	  dbUserValue.setEnabled(false);
	  
	  dbPasswordValue.setEditable(false); 
	  dbPasswordValue.setEnabled(false);
	  
	  dbPortValue.setEditable(false);
	  dbPortValue.setEnabled(false);
	  
	  dbHostValue.setEditable(false);	
	  dbHostValue.setEnabled(false);
	 
	  createUpdateButton.setEnabled(false);		
	  createUpdateButton.setEnabled(false);
	}
	
	/**
	 * Manage the selection event on the Database combo box and buttons of this popup window
	 */
	public void actionPerformed(ActionEvent e) {
		
	   final JFileChooser dbPathFileChooser = new JFileChooser();	
		
	   try{			   
		   if (e.getSource() instanceof JButton)  
		   {		     
		      if (e.getActionCommand().equalsIgnoreCase("Close")){		    		  
		    	 this.setVisible(false);
	    		 this.dispose();
		      }
		      
		      /* Choose the folder on the file-system where store the sqlite database path */
		      if (e.getActionCommand().equalsIgnoreCase("Browse"))
		      {
		    	  dbPathFileChooser.setFileFilter(new FolderFileFilter());
		    	  dbPathFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    	  dbPathFileChooser.setDialogTitle("Choose a Folder where store Database");
	              
	              int value = dbPathFileChooser.showOpenDialog(this);
	              
	              // Return value if approved (ie yes, ok) is chosen.
	              if (value==JFileChooser.APPROVE_OPTION)
	              {
                     File dbPathFolder = dbPathFileChooser.getSelectedFile();            	  
	            	 String  dbPathFolderPath = dbPathFolder.getAbsolutePath();
	            	  
	                 // set the file path in the textField
	            	 dbPathValue.setText(dbPathFolderPath);	            	 
	              }
		      }
		      
		      //Pressed the button 'Save': save the DB configuration to conf/database.properties file 
		      if (e.getActionCommand().equalsIgnoreCase("Save"))
		      {		    	  
		    	 String dbType = (String)dataBaseCombo.getSelectedItem();  
		    	  
		    	 if(this.inputValid(dbType))
		    	 {	 		 
		    		 DatabaseConfigurationBean databaseConfigurationBean = new DatabaseConfigurationBean();	
		    		 databaseConfigurationBean.setDbType(dbType.toLowerCase());
		    		 databaseConfigurationBean.setDbDriver(dbDriverValue.getText());
		    		 databaseConfigurationBean.setDbName(dbNameValue.getText());
		    			
		    		 if(dbType.equalsIgnoreCase("sqlite"))
		    		    databaseConfigurationBean.setDbPath(dbPathValue.getText());

		    		 databaseConfigurationBean.setDbUser(dbUserValue.getText());
		    		 databaseConfigurationBean.setDbPassword(dbPasswordValue.getText());
		    		 databaseConfigurationBean.setDbPort(dbPortValue.getText());
		    		 databaseConfigurationBean.setDbHost(dbHostValue.getText());	
		    		 
		    		 databaseConfigurationManager.saveOrUpdateDatabaseConfiguration(databaseConfigurationBean); 
		    		 	
		    		 messageLabel.setForeground(Color.green);
		    		 messageLabel.setText("Configuration Saved Successfully, now create the structure");
		    		 
		    	  }else{
		    		 messageLabel.setForeground(Color.red);
		    		 messageLabel.setText("Some values are invalid !"); 
		    	 }
		      }		     
		   }	  
			
		   // Manage the visibility of the fields and fill the fields with DEFAULT values according with the chosen DB 
		   if(e.getSource() instanceof JComboBox)
		   {				   
		        ItemSelectable is = (ItemSelectable)e.getSource();
		        Object selected[] = is.getSelectedObjects();
		        String dbChosen = (String)selected[0];	//the chosen value in the combo box
		        
		        if(!dbChosen.equalsIgnoreCase("---"))
		        	createUpdateButton.setEnabled(true);
		        else
		        	createUpdateButton.setEnabled(false);
		        
		        //fill the fields with the default values		        
		        if(dbChosen.equalsIgnoreCase("SQLITE"))
		        { 		        	
		        	dbUserLabel.setText("User");
		        	dbPortLabel.setText("Port");		        	
		        	dbHostLabel.setText("Host");
		        	
		        	dbDriverValue.setText(GuiParametersEnum.SQLITE_JDBC_DRIVER.getParamValue());
		        	dbNameValue.setText(GuiParametersEnum.SQLITE_DEFAULT_DB_NAME.getParamValue());
		        	dbNameValue.setEditable(true);
		        	dbNameValue.setEnabled(true);
		        	//if the folder don't exist will be created
		        	dbPathValue.setText(GuiParametersEnum.SQLITE_DEFAULT_DB_PATH.getParamValue());		        	
		        	dbPathValue.setEnabled(true);
		        	dbPathValue.setEditable(true);
		        	
		        	dbPathBrowseButton.setEnabled(true);
		        	
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
		
		        }else if(dbChosen.equalsIgnoreCase("MYSQL")) {	
		        	 	
		        	dbUserLabel.setText("* User");
		        	dbPortLabel.setText("* Port");		        	
		        	dbHostLabel.setText("* Host");
		        	
		        	dbDriverValue.setText(GuiParametersEnum.MYSQL_JDBC_DRIVER.getParamValue());
		        	dbNameValue.setText(GuiParametersEnum.MYSQL_DEFAULT_DB_NAME.getParamValue());
		        	dbNameValue.setEditable(true);
		        	dbNameValue.setEnabled(true);
		        	
		        	dbUserValue.setText(GuiParametersEnum.MYSQL_DEFAULT_DB_USER.getParamValue());
		        	//password can be empty
		        	dbPasswordValue.setText(GuiParametersEnum.MYSQL_DEFAULT_DB_PASSWORD.getParamValue());
		        	dbPortValue.setText(GuiParametersEnum.MYSQL_DEFAULT_DB_PORT.getParamValue());
		        	dbHostValue.setText(GuiParametersEnum.MYSQL_DEFAULT_DB_HOST.getParamValue());
		        	
		        	//disable fields used only for sqlite
		        	dbPathValue.setEnabled(false);
		        	dbPathValue.setEditable(false);
		        	dbPathBrowseButton.setEnabled(false);
		        	
		        	dbUserValue.setEditable(true);
			    	dbUserValue.setEnabled(true);
			    	  
			    	dbPasswordValue.setEditable(true);
			    	dbPasswordValue.setEnabled(true);
			    	  
			    	dbPortValue.setEditable(true);
			    	dbPortValue.setEnabled(true);
			    	  
			    	dbHostValue.setEditable(true);
			    	dbHostValue.setEnabled(true);
			    	
		        }else   // selected  "---"
		        	disableAllWidget();
		   }
        
		}catch (Exception ex) {
			log.fatal("Error saving/loading DB configuration, cause: ",ex);
			
			createUpdateButton.setEnabled(false);			
			messageLabel.setForeground(Color.red);
			messageLabel.setText("Error saving/loading DB configuration, (see log file)");
		}		
	  
	}	
	
	/**
	 * Utility method that validate the user input according with the chosen database
	 * @return true if the input is valid (the required fields are filled)
	 */
	private boolean inputValid(String dbType){
		
		if(dbType.equalsIgnoreCase("MYSQL")) 
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
			
        }else if(dbType.equalsIgnoreCase("SQLITE")){   //only driver and db-name are necessary
        	
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

	public JComboBox getDataBaseCombo() {
		return dataBaseCombo;
	}

	public void setDataBaseCombo(JComboBox dataBaseCombo) {
		this.dataBaseCombo = dataBaseCombo;
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

	public static String[] getDatabasesSupported() {
		return databasesSupported;
	}

	public static void setDatabasesSupported(String[] databasesSupported) {
		DatabaseConfigCreatorPopUp.databasesSupported = databasesSupported;
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

}
