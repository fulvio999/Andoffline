
package andoffline.gui.panel.server;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import andoffline.gui.common.utility.BusyLabelPanel;
import andoffline.gui.panel.sms.SmsPanel;
import andoffline.server.tomcat.command.TomcatServerStartStopActionListner;

/**
 * Create the command panel for the "Web ServerPanel"
 *
 */
public class WebServerCommandPanel extends JPanel{

	private static final long serialVersionUID = 1L;
    
    private JButton closeButton;  
  
    /* Control to manage the embedded server  */
    private JButton startServerButton;    
    private JButton stopServerButton;
    
    /* A label that display the current status of the embedded server */
    private JLabel serverStatusLabel;
    
    private JLabel serverStatus;
    
    private JLabel errorLabel;
    
    /* A sub-panel with an animation to indicates that a processing is in action */
    private BusyLabelPanel busyLabelPanel;

	/**
	 * Constructor
	 */
	public WebServerCommandPanel() {
		
		this.setBorder(BorderFactory.createTitledBorder("Server Commands"));		
		this.setLayout(new MigLayout("wrap 3")); 
		
		serverStatusLabel = new JLabel("Current Server Status:");		
		serverStatus = new JLabel("STOPPED"); //this value will be updated when the user press start/stop buttons
		serverStatus.setForeground(Color.black);
		
		errorLabel = new JLabel(); //display error messages
		
		startServerButton = new JButton("Start Server");
		startServerButton.setIcon(createImageIcon("play.png","Start Server"));
		
		stopServerButton = new JButton("Stop Server");
		stopServerButton.setIcon(createImageIcon("stop.png","Stop Server"));
		stopServerButton.setEnabled(false);		
		
		this.busyLabelPanel = new BusyLabelPanel();
		this.busyLabelPanel.getJxBusyLabel().setBusy(false);
		
		TomcatServerStartStopActionListner serverStartStopActionListner = new TomcatServerStartStopActionListner(errorLabel,serverStatus,startServerButton,stopServerButton,this);
		startServerButton.addActionListener(serverStartStopActionListner);
		stopServerButton.addActionListener(serverStartStopActionListner);
		
		//--- add components to panel
		this.add(new JLabel(""));
		this.add(startServerButton,"gapleft 300,split 2");
		this.add(stopServerButton,"wrap");	
						
		this.add(new JLabel(""));		
		this.add(serverStatusLabel,"gapleft 300,split 2");
		this.add(serverStatus,"wrap");
		
		this.add(errorLabel,"span 3,align center,gapleft 120");
				
		this.add(busyLabelPanel,"span 3, align center,gapbottom 75,gapleft 40");
	}
	
	
	/**
	 * Create the ImageIcon to be shown in the Server command buttons
	 * @param path The image file to use for ImageIcon creation
	 * @return
	 */	 
	protected static ImageIcon createImageIcon(String path, String desc) {
		
        java.net.URL imgURL = SmsPanel.class.getResource("/"+path);
        if (imgURL != null) {        	
            return new ImageIcon(imgURL,desc);
        } else {         
            return null;
        }
    }
	

	public JButton getCloseButton() {
		return closeButton;
	}


	public void setCloseButton(JButton closeButton) {
		this.closeButton = closeButton;
	}


	public JButton getStartServerButton() {
		return startServerButton;
	}


	public void setStartServerButton(JButton startServerButton) {
		this.startServerButton = startServerButton;
	}


	public JButton getStopServerButton() {
		return stopServerButton;
	}


	public void setStopServerButton(JButton stopServerButton) {
		this.stopServerButton = stopServerButton;
	}


	public JLabel getServerStatusLabel() {
		return serverStatusLabel;
	}


	public void setServerStatusLabel(JLabel serverStatusLabel) {
		this.serverStatusLabel = serverStatusLabel;
	}


	public JLabel getServerStatus() {
		return serverStatus;
	}


	public void setServerStatus(JLabel serverStatus) {
		this.serverStatus = serverStatus;
	}


	public BusyLabelPanel getBusyLabelPanel() {
		return busyLabelPanel;
	}
	
}	
	
