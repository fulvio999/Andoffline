
package andoffline.gui.panel.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXHyperlink;

import andoffline.gui.panel.phonemanager.PhoneManagerCommandPanel;
import andoffline.utility.HyperlinkActionHandler;



/**
 * Create the tab panel with the commands to manage the integrated server 
 *
 */
public class WebServerPanel extends JPanel implements ActionListener{
	
	private static final Logger log = Logger.getLogger(WebServerPanel.class);
	
	private static final long serialVersionUID = 1L;
	private static final String SERVER_URL = "http://localhost:8080/andoffline-web";

	// the frame to close
    private JFrame mainFrame;
    
    // The panel with the buttons to start/stop the embedded server
    private WebServerCommandPanel webServerCommandPanel;      
    private JXHyperlink link;    
    private JButton closeButton;
	
	/**
	 * Constructor
	 */
	public WebServerPanel(JFrame mainFrame) {
		
		 this.mainFrame = mainFrame;
		
		 link = new JXHyperlink(new HyperlinkActionHandler(SERVER_URL));		
		 this.closeButton = new JButton("Close");
		 this.webServerCommandPanel = new WebServerCommandPanel();
		 
		 closeButton.addActionListener(this);
		 
		 this.setBorder(BorderFactory.createTitledBorder("Web Server Management"));		 
		 this.setLayout(new MigLayout("wrap 1"));
		 
		 this.add(new JLabel("<html> Starting the server, the Database saved jobs will be available on the network at: <br/> http://ipaddress:port/application-name/webapp/ <br/> Using the default values the url is:" +
		 		             "</html>"),"align center,gapleft 200");		 
	    
	     this.add(link,"align center");
	     this.add(new JLabel("<html> If your host is network connected instead of 'localhost' you can user your IP address <br/> <br/> </html>"),"gapleft 200");
	     
	     this.add(webServerCommandPanel, "align center, growx");
	     this.add(new JLabel(""),"wrap,dock south");	
	     this.add(new PhoneManagerCommandPanel( this.mainFrame),"gaptop 150");
		 //this.add(closeButton,"width 105,gapleft 850,gaptop 220");
	}	
	
	/* 
	 * The event handler for the Server panel
	 */
	public void actionPerformed(ActionEvent e) {		
		 
	    if (e.getSource() instanceof JButton)  
	    {	      
	        if (e.getActionCommand().equals("Close"))
	    	{	
	        	try{
	        		 if(mainFrame.isDisplayable()) {                     
	            		mainFrame.dispose();
	                 } 
	        	
	        	}catch (Exception ex) {
	        		log.fatal("Error during the shutdown of the server: ",ex);
	        	}	  
	       }
	    }    
    }


	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public WebServerCommandPanel getWebServerCommandPanel() {
		return webServerCommandPanel;
	}

	public void setWebServerCommandPanel(WebServerCommandPanel webServerCommandPanel) {
		this.webServerCommandPanel = webServerCommandPanel;
	}


}
