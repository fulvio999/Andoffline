package andoffline.integration.database.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import andoffline.gui.common.utility.BusyLabelPanel;
import andoffline.integration.database.DatabaseConfigurationManager;
import andoffline.integration.database.bean.DatabaseConfigurationBean;
import andoffline.integration.database.dao.CallDao;
import andoffline.integration.database.dao.ContactDao;
import andoffline.integration.database.dao.SmsDao;
import andoffline.parser.vcf.VcardRecord;

/**
 * Show a popUp where the user can load an EXISTING Database configuration (Created with the dedicated menu entry)
 * to save to a Database:
 * - SMS
 * - CALL 
 * - CONTACT 
 * The content type stored depends on the gui tab where is invoked 
 * To save content to DB is necessary that the user has previously created a database configuration and schema structure
 * using the "Configuration" menu entry.
 * 
 * NOTE: the popUp is read-only: is used only to chose an existing DB configuration: to change the configuration must be used the dedicated menu entry on the menu bar 
 */
public class DataBaseSaverPopUp extends JDialog implements ActionListener{
	
	 private static final long serialVersionUID = -1848935925091081071L;
	 
	 private static final Logger log = Logger.getLogger(DataBaseSaverPopUp.class);
	 
	 private JTextField dataBaseType;
	 
	 private JLabel dbDriverLabel;
	 private JLabel dbNameLabel;
	 private JLabel dbPathLabel;
	 private JLabel dbUserLabel;
	 private JLabel dbPasswordLabel; 	
	 private JLabel dbPortLabel;
	 private JLabel dbHostLabel;
	 
	 private JTextField dbDriverValue;
	 private JTextField dbNameValue;
	 private JTextField dbPathValue;
	 private JTextField dbUserValue;
	 private JTextField dbPasswordValue; 	
	 private JTextField dbPortValue;
	 private JTextField dbHostValue;	 
	 
	 private JButton closeButton;
	 private JButton confirmButton;
	 
	 /* A sub-panel with an animation to indicates that a processing is in action */
	 private BusyLabelPanel busyLabelPanel;
	 
	 private JTree treeToSave;
	 private String contentTypeToSave; //the content-type to save: contact, sms, call
	 private List<VcardRecord> vcardRecordList; //the list of vcard (this param is set only if the content to save are contacts)
	 
	 /* A label that show operation results/errors */
	 private JLabel messageLabel;
	 
	 /* The dedicated object to read the configurations for database access  */  
	 private DatabaseConfigurationManager databaseConfigurationManager = new DatabaseConfigurationManager();
	 
	/**
	 * Constructor used when the user want save sms or calls
	 * @param mainFrame The root main frame to close when the user press the Close button (ie the full application is closed)	 
	 * @param tree The JTree with the sms to be exported
	 * @param contentType The type of data to store (ie: CALL, SMS, CONTACT)
	 */
	public DataBaseSaverPopUp(JFrame mainFrame, JTree treeToSave, String contentType) {
		
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Settings"));	
		this.contentTypeToSave = contentType;
		this.treeToSave = treeToSave;		
		
		this.buildPopUp();		
	}

	/**
	 * Second constructor dedicated to save ONLY the contacts extracted from a VCF file, because the contact are not displayed with a JTree
	 * @param mainFrame
	 * @param vcardRecordList A list of record built parsing a VCF file
	 * @param contentType
	 */
	public DataBaseSaverPopUp(JFrame mainFrame, List<VcardRecord> vcardRecordList, String contentType) {
		
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Options"));	
		this.vcardRecordList = vcardRecordList;
		this.contentTypeToSave = contentType;
		
		this.buildPopUp();
	}
	
	/**
	 * Utility method that build the popup layout
	 */
	private void buildPopUp()
	{		
		dataBaseType = new JTextField();
		dataBaseType.setEditable(false);
		dataBaseType.setEnabled(false);
       
        this.setLayout(new MigLayout("wrap 2")); //we want 2 columns
        
        this.add(new JLabel("Save "+this.contentTypeToSave+" using an EXISTING Database configuration"),"wrap,span 2,align center");
        this.add(new JLabel("(To edit/create use the 'Configuration' menu entry)"),"wrap,span 2,align center");
        this.add(new JLabel("Database:"),"gapy 20px");
        this.add(dataBaseType,"width 100,wrap");
        
   	    // LABEL
   	    dbDriverLabel = new JLabel("Driver:"); 
	    dbNameLabel = new JLabel("DB Name:");
	    dbPathLabel = new JLabel("DB Path:");   //only for sqlite db
	    dbUserLabel = new JLabel("User:");
	    dbPasswordLabel = new JLabel("Password:");  	 
	    dbPortLabel = new JLabel("Port:");
	    dbHostLabel = new JLabel("Host:");  
	    
  	    // FIELDS
   	    dbDriverValue = new JTextField("");   	    
   	    dbNameValue = new JTextField("");
   	    dbPathValue = new JTextField("");  //only for sqlite db
        dbUserValue = new JTextField("");
        dbPasswordValue = new JTextField(""); 
        dbPortValue = new JTextField("");
        dbHostValue = new JTextField("");
        
        //NOTE: disable ALL the fields: this is a read-only popup. To change the values the user must use the dedicated menu entry 
        dbDriverValue.setEditable(false); 
   	    dbDriverValue.setEnabled(false);
   	    dbNameValue.setEditable(false); 
   	    dbNameValue.setEnabled(false);
   	    dbPathValue.setEditable(false); 
   	    dbPathValue.setEnabled(false);
   	    dbUserValue.setEditable(false); 
   	    dbUserValue.setEnabled(false);
   	    dbPasswordValue.setEditable(false); 
   	    dbPasswordValue.setEnabled(false);
   	    dbPortValue.setEditable(false);
   	    dbPortValue.setEnabled(false);
   	    dbHostValue.setEditable(false);
   	    dbHostValue.setEnabled(false);         
   	    
   	    busyLabelPanel = new BusyLabelPanel();
	    busyLabelPanel.getJxBusyLabel().setEnabled(false);
	    busyLabelPanel.getJxBusyLabel().setBusy(false);
   	    
        closeButton = new JButton("Close");
        confirmButton = new JButton("Confirm");       
        confirmButton.setEnabled(false); //will be enabled only when the user has chosen a target DB
        
        messageLabel = new JLabel("");
        
        closeButton.addActionListener(this);
        confirmButton.addActionListener(this);
        
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
				
				confirmButton.setEnabled(true);
				
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
   	    
   	    this.add(dbDriverLabel);
   	    //specify the component size with the pattern: min:preferred:max 
   	    this.add(dbDriverValue,"width 200:300:300, wrap");   
   	 
   	    this.add(dbNameLabel);
   	    this.add(dbNameValue,"width 100:150:200, wrap");
   	    
   	    this.add(dbPathLabel);
	    this.add(dbPathValue,"width 300:300:300, wrap");
   	      	 
   	    this.add(dbUserLabel);
   	    this.add(dbUserValue,"width 100:150:200, wrap");   	   
   	    
   	    this.add(dbPasswordLabel);
   	    this.add(dbPasswordValue,"wrap,width 100:100:150");   	  
   	 
   	    this.add(dbPortLabel);
   	    this.add(dbPortValue,"wrap,width 100:100:150");   	   
   	 
   	    this.add(dbHostLabel);
   	    this.add(dbHostValue,"width 200:300:300, wrap");   
 
   	    //place-holder   	   
   	    this.add(new JLabel(""),"wrap,span 2");
   	    
   	    this.add(closeButton,"width 100:100:100, gapleft 60");   	      	    
   	    this.add(confirmButton,"width 100:100:100, wrap");   
   	    
   	    this.add(messageLabel,"wrap,span 2,align center");   	    
   	    this.add(busyLabelPanel,"span 1,align center,gapbottom 20");
        
		//set the dialog as a modal window
        this.setModalityType(ModalityType.APPLICATION_MODAL);

        this.setTitle("Store in a database");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);        
        this.setSize(500,500);  			
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
		
		this.confirmButton.setEnabled(false);
	}
	
	/**
	 * Manage the selection event on the Database combo box and buttons of this popup window
	 */
	public void actionPerformed(ActionEvent e) {
		
	   try{		   
		   
		   if (e.getSource() instanceof JButton)  
		   {
		      //close the popup
		      if (e.getActionCommand().equalsIgnoreCase("Close")){		    		  
		    	  this.setVisible(false);
	    		  this.dispose();
		      }
		      // save the sms/call/contact in the chosen Database
		      if (e.getActionCommand().equalsIgnoreCase("Confirm"))
		      {			    	  
		    	    //open a separate thread in case of the operation last a while...
					if (!busyLabelPanel.getJxBusyLabel().isEnabled())
			       	{
			       	   busyLabelPanel.getJxBusyLabel().setEnabled(true);		       	  
			       	}
			       	
			       	if (busyLabelPanel.getJxBusyLabel().isBusy()) {	   //true if the task is finished   	    
			       	    busyLabelPanel.getJxBusyLabel().setBusy(false);
			       	    busyLabelPanel.getJxBusyLabel().setVisible(false);		       	   
			       	   
			       	}else{	
			       		    //show the animation
			       	        busyLabelPanel.getJxBusyLabel().setBusy(true);
			       	        busyLabelPanel.getJxBusyLabel().setVisible(true); 
			       	        
			       	        final String contentType = this.contentTypeToSave;
			       	        log.info("Content-type to store: "+contentType);
			       	        
			       	        final JTree treeContentToSave = this.treeToSave;
			       	        final List<VcardRecord> vcardRecorToStore = this.vcardRecordList;
		    	  
			       	        /**
						     * Create a new thread (different than the Event Dispatch Thread used by Swing to dispatch event) 
						     * because the operation can last some time if the mail server is slow      
						     */
					       	Thread saveToDatabaseThread = new Thread() {
								
					       	     public void run() {
					       	    		
					       	    	  try{ 	  
									    	String dbChosen = dataBaseType.getText();		    	
																														    	
									    	/* Depending on the content to store, choose the right dao */
									    	if(contentType.equalsIgnoreCase("SMS")){
									    		
									    		SmsDao smsDao = new SmsDao();
									    		smsDao.saveData(dbChosen,treeContentToSave);
									    		
									    	}else if(contentType.equalsIgnoreCase("CALL")){
									    		
									    		CallDao callDao = new CallDao();
									    		callDao.saveData(dbChosen,treeToSave);
									    		
									    	}else if(contentType.equalsIgnoreCase("CONTACT")){
									    		
									    		ContactDao contatcDao = new ContactDao();
									    		contatcDao.saveData(dbChosen,vcardRecorToStore);		    		
									    	}									    	
									    	
									    	//hide and stop the animation
										    busyLabelPanel.getJxBusyLabel().setBusy(false);
									   		busyLabelPanel.getJxBusyLabel().setVisible(false);
										    
										    messageLabel.setText("Operation Executed Successfully");
									    	messageLabel.setFont(new Font("Serif", Font.BOLD, 15));
									    	messageLabel.setForeground(Color.GREEN);
									        
									    	messageLabel.setForeground(Color.green);
									    	messageLabel.setText("Operation executed Successfully !");
								    	
					       	    	    }catch (Exception ex){
											
										    // stop and hide the animation
										    busyLabelPanel.getJxBusyLabel().setBusy(false);
									   	    busyLabelPanel.getJxBusyLabel().setVisible(false);						   		  
									   		  
								    	    messageLabel.setText("Failure: database Schema created ?");
								    	    messageLabel.setFont(new Font("Serif", Font.BOLD, 15));
										    messageLabel.setForeground(Color.RED);
								        }
					       	         }
					         };
					         
					     saveToDatabaseThread.start(); 					    	
			       	}		    	
		        }
		   }	  
		
		   // show the right configuration depending on the chosen database in the combo
		   if(e.getSource() instanceof JComboBox)
		   {
		        ItemSelectable is = (ItemSelectable)e.getSource();
		        Object selected[] = is.getSelectedObjects();
		        String dbChosen = ((String)selected[0]).toLowerCase();	
		        
		        if(!dbChosen.equalsIgnoreCase("---"))
		        	confirmButton.setEnabled(true);
		        else
		        	confirmButton.setEnabled(false);
		        
		        if(dbChosen.equalsIgnoreCase("SQLITE"))
		        {         	
		        	//load the saved configuration from conf/database.properties file and show it		        
		        	DatabaseConfigurationBean databaseConfigurationBean = databaseConfigurationManager.loadDatabaseConfiguration(dbChosen);
		        	
		        	dbDriverValue.setText(databaseConfigurationBean.getDbDriver());
		        	dbNameValue.setText(databaseConfigurationBean.getDbName());
		        	dbPathValue.setText(databaseConfigurationBean.getDbPath());
		        	//clean the unused fields for sqlite db
		        	dbUserValue.setText("");
		        	dbPasswordValue.setText("");
		        	dbPortValue.setText("");
		        	dbHostValue.setText("");
		
		        }else if(dbChosen.equalsIgnoreCase("MySQL")){
		        	
		        	//if any, load the saved configuration form conf/database.properties file and show it
		         	//load the saved configuration from conf/database.properties file and show it		        
		        	DatabaseConfigurationBean databaseConfigurationBean = databaseConfigurationManager.loadDatabaseConfiguration(dbChosen);
		        	
		        	dbDriverValue.setText(databaseConfigurationBean.getDbDriver());
		        	dbNameValue.setText(databaseConfigurationBean.getDbName());
		        	dbUserValue.setText(databaseConfigurationBean.getDbUser());
		        	dbPasswordValue.setText(databaseConfigurationBean.getDbPassword());
		        	dbPortValue.setText(databaseConfigurationBean.getDbPort());
		        	dbHostValue.setText(databaseConfigurationBean.getDbHost());		
		        	
		        } else {
		        	confirmButton.setEnabled(false);
		        	log.fatal("Error loading DB configuration, unuspported DB type: "+dbChosen);
		        }
		   }
        
		}catch (Exception ex) {
			log.fatal("Error saving or loading DB configuration, maybe the DB is not configured, cause: ",ex);
			
			confirmButton.setEnabled(false);
			messageLabel.setForeground(Color.red);
			messageLabel.setText("Error saving or loading DB configuration, (see log file)");
		}	
	  
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

	public JTree getTreeToSave() {
		return treeToSave;
	}

	public void setTreeToSave(JTree treeToSave) {
		this.treeToSave = treeToSave;
	}

	public String getContentTypeToSave() {
		return contentTypeToSave;
	}

	public void setContentTypeToSave(String contentTypeToSave) {
		this.contentTypeToSave = contentTypeToSave;
	}

	public List<VcardRecord> getVcardRecordList() {
		return vcardRecordList;
	}

	public void setVcardRecordList(List<VcardRecord> vcardRecordList) {
		this.vcardRecordList = vcardRecordList;
	}

}
