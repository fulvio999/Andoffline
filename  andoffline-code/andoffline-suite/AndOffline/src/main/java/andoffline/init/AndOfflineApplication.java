package andoffline.init;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import andoffline.gui.menu.about.AboutMenuPopUp;
import andoffline.gui.menu.configuration.database.DatabaseConfigCreatorPopUp;
import andoffline.gui.menu.configuration.database.DatabaseConfigEditorPopUp;
import andoffline.gui.menu.configuration.database.DatabaseStructureCreatorPopUp;
import andoffline.gui.menu.configuration.mail.MailServerConfigurationPopUp;
import andoffline.gui.menu.configuration.twitter.TwitterConfigurationPopUp;
import andoffline.gui.panel.call.CallPanel;
import andoffline.gui.panel.contact.ContactPanel;
import andoffline.gui.panel.help.HelpPanel;
import andoffline.gui.panel.imageconverter.ImageEncoderDecoderTab;
import andoffline.gui.panel.job.JobManagerTab;
import andoffline.gui.panel.phonemanager.PhoneManagerTab;
import andoffline.gui.panel.server.WebServerPanel;
import andoffline.gui.panel.server.WebServerPanel;
import andoffline.gui.panel.sms.SmsPanel;
import andoffline.server.tomcat.command.StopServerFromCommandLine;


/**
 * Create the "AndOffline" application and show it 
 *
 */
public class AndOfflineApplication implements WindowListener{
	
	private static final Logger log = Logger.getLogger(AndOfflineApplication.class);
	
	private JFrame mainFrame;	
	private JMenuBar menuBar;	
	private JTabbedPane tabbedPane;	
	private SmsPanel smsPanel;	
	private CallPanel logCallPanel;	
	private ContactPanel contactPanel;	
	private ImageEncoderDecoderTab imageEncoderDecoderTab;	
	private WebServerPanel webServerPanel;	
	private PhoneManagerTab phoneManagerTab;	
	private JobManagerTab jobManagerTab;	
	private HelpPanel helpPanel;

	/**
	 * Main class for the application
	 * Create the full gui and show it
	 */
	public AndOfflineApplication() {
		
		try {	
			//To load correctly start the application using the run.sh script instead of java -jar command
	        File log4jfile = new File("conf"+File.separator+"log4j.properties");
	        
	        if(!log4jfile.exists()){
	        	
	        	// initialize manually log4j attaching only a console appender (useful when project executed from Eclipse)
        	
	        	System.out.println("** log4j.properties not found: Initializing manually with only a console appender...");        	
	        	AndOfflineLog4jManager andOfflineLog4jManager = new AndOfflineLog4jManager();
	        	andOfflineLog4jManager.initializeOnlyConsoleLogging();			
           
	        }else{  	        	
	            PropertyConfigurator.configure(log4jfile.getAbsolutePath());
	        }
			
			log.info("Starting the Application...");	
			/*
			 * "Metal" look and feel is only the possible one due to annoying layout problem between OS an look and feel
			 */
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); 
	 
	} catch (Exception e) {		
		log.error("Exception 1: ",e);
		
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		
		} catch (Exception ee) {			
			log.error("Exception 2: ",ee);
		}
	}
	
    mainFrame = new JFrame("AndOffline 1.0"); 
    mainFrame.addWindowListener(this);
    
    //when the user press the frame close button do nothing 
    mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    mainFrame.setPreferredSize(new Dimension(1000,700));
    
    //------------ Creates a menu bar for the JFrame ------------------
    menuBar = new JMenuBar();
    
    // Add the menubar to the frame
    mainFrame.setJMenuBar(menuBar);
    
    //*********** Menu Bar: Item 1: Configuration ********** 
    JMenu configMenu = new JMenu("Configuration");    
    menuBar.add(configMenu);           
     
    JMenu dbConfiguration = new JMenu("DataBase");      
    
    //submenu of the "Database" one
    JMenuItem dbConfigCreateConfigSubmenu = new JMenuItem("Create configuration");  
    JMenuItem dbConfigEditSubmenu = new JMenuItem("Edit configuration");  
    JMenuItem dbCreateSchemaSubmenu = new JMenuItem("Create structure");  
    
    dbConfiguration.add(dbConfigCreateConfigSubmenu); 
    dbConfiguration.add(dbCreateSchemaSubmenu);
    dbConfiguration.add(dbConfigEditSubmenu);   
   
    // listener for: "Configuration --> Database --> Create configuration"
    dbConfigCreateConfigSubmenu.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent event) {        	
           DatabaseConfigCreatorPopUp dataBaseConfigCreatorPopUp = new DatabaseConfigCreatorPopUp();        	
        }
    });   
    
    
    // listener for: "Configuration --> Database --> Edit"
    dbConfigEditSubmenu.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent event) {        	
        	DatabaseConfigEditorPopUp databaseConfigEditorPopUp = new DatabaseConfigEditorPopUp();
        }
    }); 
    
    
    // listener for: "Configuration --> Database --> Create Schema"
    dbCreateSchemaSubmenu.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent event) {
        	DatabaseStructureCreatorPopUp databaseStructureCreatorPopUp = new DatabaseStructureCreatorPopUp();
        }
        
    });    
      
    configMenu.add(dbConfiguration);
    
    JMenuItem twitterConfiguration = new JMenuItem("Twitter");
    configMenu.add(twitterConfiguration);
    
    // listener for: "Configuration --> Twitter
    twitterConfiguration.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent event) {        	 
        	TwitterConfigurationPopUp twitterConfigurationPopUp = new TwitterConfigurationPopUp();
        }
    });    
    
    
    JMenuItem mailConfiguration = new JMenuItem("Mail");
    configMenu.add(mailConfiguration);
    
    mailConfiguration.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent event) {
        	//open a popup where configure the databases 
        	MailServerConfigurationPopUp mailServerConfigurationPopUp = new MailServerConfigurationPopUp();
        }
    });
    
    //********* Menu Bar: Item 2: About ********** 
    JMenu aboutMenu = new JMenu("About");    
    menuBar.add(aboutMenu);    
   
    JMenuItem aboutEntry = new JMenuItem("About");
    aboutMenu.add(aboutEntry);
    
    aboutEntry.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent event) {        	
        	AboutMenuPopUp aboutMenuPopUp = new AboutMenuPopUp();
        }
    });
    
    //-------------- Create the tab panels and add them to the panel --------------------
    tabbedPane = new JTabbedPane(); 
    
    //Sms panel
    smsPanel = new SmsPanel(mainFrame);
    tabbedPane.addTab("SMS Browser", smsPanel);
    
    //Log call panel
    logCallPanel = new CallPanel(mainFrame);
    tabbedPane.addTab("Call Browser", logCallPanel);
    
    //Contacts panel
    contactPanel = new ContactPanel(mainFrame);
    tabbedPane.addTab("Contact Browser", contactPanel);
    
    //Image Converter Panel
    imageEncoderDecoderTab = new ImageEncoderDecoderTab(mainFrame);
    tabbedPane.addTab("Image Encoder/Decoder", imageEncoderDecoderTab);
   
    //Web Server panel
    webServerPanel = new WebServerPanel(mainFrame);
    tabbedPane.addTab("Web Server", webServerPanel);
    
    //Phone Manager panel
    phoneManagerTab = new PhoneManagerTab(mainFrame);
    tabbedPane.addTab("Phone Manager", phoneManagerTab);
    
    //Job Manager panel
    jobManagerTab = new JobManagerTab(mainFrame);
    tabbedPane.addTab("Job Manager", jobManagerTab);
    
    //Help Panel
    helpPanel = new HelpPanel(mainFrame);
    tabbedPane.addTab("Help", helpPanel);    
    //-------------------------------------
    
    mainFrame.add(tabbedPane);
    
    //-------- Display the window ---------
    mainFrame.pack();
    mainFrame.setVisible(true);
    
	}

	
	/**
	 * Utility method used to update the look and feel of all the component according with choice of the user 
	 * Example of call: updateAllComponentRecursively(mainFrame);
	 * currently NOT used
	 * @param window
	 */
	public static void updateAllComponentRecursively(Window window)
	{
	    for(Window childWindow : window.getOwnedWindows()) {
	    	updateAllComponentRecursively(childWindow);
	    }
	    SwingUtilities.updateComponentTreeUI(window);
	}
	
	
	
	
	public JMenuBar getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(JMenuBar menuBar) {
		this.menuBar = menuBar;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public SmsPanel getSmsPanel() {
		return smsPanel;
	}

	public void setSmsPanel(SmsPanel smsPanel) {
		this.smsPanel = smsPanel;
	}

	public CallPanel getLogCallPanel() {
		return logCallPanel;
	}

	public void setLogCallPanel(CallPanel logCallPanel) {
		this.logCallPanel = logCallPanel;
	}

	public ContactPanel getContactPanel() {
		return contactPanel;
	}

	public void setContactPanel(ContactPanel contactPanel) {
		this.contactPanel = contactPanel;
	}

	public HelpPanel getHelpPanel() {
		return helpPanel;
	}

	public void setHelpPanel(HelpPanel helpPanel) {
		this.helpPanel = helpPanel;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	public void windowOpened(WindowEvent e) {		
		
	}

	/**
	 * Called when the user press the Frame Close button.
	 * Is not used the default close operation because before close the frame is necessary stop Tomcat server
	 * to free the tcp port used
	 */
	public void windowClosing(WindowEvent e) {

		try{
			//stop the server before close the windows
			StopServerFromCommandLine.stopServer();
			
			mainFrame.dispose();	
			
		}catch (Exception ex) {
		   log.fatal("Error during the shutdown of the server (maybe was not started): ",ex);		  
		}		
	}	
	

	public void windowClosed(WindowEvent e) {		
		
	}

	public void windowIconified(WindowEvent e) {	
		
	}

	public void windowDeiconified(WindowEvent e) {				
	}

	public void windowActivated(WindowEvent e) {		
		
	}

	public void windowDeactivated(WindowEvent e) {		
		
	}

	public ImageEncoderDecoderTab getImageEncoderDecoderTab() {
		return imageEncoderDecoderTab;
	}

	public void setImageEncoderDecoderTab(
			ImageEncoderDecoderTab imageEncoderDecoderTab) {
		this.imageEncoderDecoderTab = imageEncoderDecoderTab;
	}


	public WebServerPanel getWebServerPanel() {
		return webServerPanel;
	}


	public void setWebServerPanel(WebServerPanel webServerPanel) {
		this.webServerPanel = webServerPanel;
	}

}
