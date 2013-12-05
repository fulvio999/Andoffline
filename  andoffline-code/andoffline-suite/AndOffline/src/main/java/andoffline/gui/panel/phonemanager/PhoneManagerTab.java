
package andoffline.gui.panel.phonemanager;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import andoffline.integration.phone.MessageQueueMonitorPanel;
import andoffline.integration.phone.PcListener;
import andoffline.utility.validation.TextFieldValidator;

/**
 * Main class that create the tab named 'Phone Manager'. 
 * It contains the necessary settings to "connect" the tool with the phone using a swap file
 * filled by Android utility logcat executed in a shell:
 * adb logcat -s AndOfflineSmsProcessor > /home/test/destinationfile.txt 
 *  
 * NOTE: the phone must be connected with the PC the USB cable and the SD card must be mounted
 * and readable 
 *
 */
public class PhoneManagerTab extends JPanel implements ActionListener{
	
    private static final long serialVersionUID = 1L;
    
    private static final Logger log = Logger.getLogger(PhoneManagerTab.class);
	
	// The parent frame that the "close" JButton of the command panel must close
    private JFrame mainFrame;
    
    private PcListener pcListener;
    
    private JLabel swapFileLabel;    
    private JTextField swapFileTextField;    
    private JButton swapFileBrowseButton;  
    
    // the swap file written by Android logcat utility
 	private File monitoredSwapFile;
 	// the absolute path to the swap file
 	private String pathMonitoredSwapFile;
 	
 	private JLabel userMesageLabel;
 	
 	private JButton startListenerButton; 
 	private JButton stopListenerButton; 
 	
 	private JSpinner tailPollingFreqSpinner;
 	private JLabel tailPollingFreqLabel;
 	
 	/* values (in second) for the polling frequency spinner */
	private static int SPINNER_MIN_VALUE = 1;
	private static int SPINNER_MAX_VALUE = 100;
	private static int SPINNER_INITIAL_VALUE = 1;
	private static int SPINNER_STEP_SIZE = 1;
 	
	/* A panel with the current status of the Queue and the currently processed messages */
	private MessageQueueMonitorPanel messageQueueMonitorPanel;
	
	/* The panel with the Close button */
    private PhoneManagerCommandPanel phoneMnagerCommandPanel;
    
    /* The panel with the help step */
    private PhoneManagerHelpPanel phoneManagerHelpPanel;
    
    /* The validator for the input values inserted by user in this panel */
	private TextFieldValidator verifier = new TextFieldValidator();
        	
	/**
	 * Constructor
	 */
	public PhoneManagerTab(JFrame mainFrame) {
		
		 this.setBorder(BorderFactory.createTitledBorder("Phone Manager"));	
		 this.setLayout(new MigLayout("wrap 3"));		 
		 this.mainFrame = mainFrame;
		
		 String headerMsg = "<html><b><br/> PHONE Manager: </b> Allow you to configure the interaction between USB connected Android phone and the PC <br/> </html>";
		
		 messageQueueMonitorPanel = new MessageQueueMonitorPanel();
		 phoneMnagerCommandPanel = new PhoneManagerCommandPanel(mainFrame);
		 phoneManagerHelpPanel = new PhoneManagerHelpPanel();
		 
		 this.swapFileLabel = new JLabel("* Swap file:");
		 this.swapFileTextField = new JTextField();	
		 this.swapFileTextField.setInputVerifier(verifier);
		 
		 this.swapFileBrowseButton = new JButton("Browse");
		 swapFileBrowseButton.addActionListener(this);	
		 
		 this.userMesageLabel = new JLabel("");
		 
		 SpinnerModel model = new SpinnerNumberModel(SPINNER_INITIAL_VALUE,SPINNER_MIN_VALUE,SPINNER_MAX_VALUE,SPINNER_STEP_SIZE);		
			
		 this.tailPollingFreqSpinner = new JSpinner(model);
		 // get the default editor of the spinner and disable it to prevent editing from keyboard
		 JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) tailPollingFreqSpinner.getEditor();
		 editor.getTextField().setEnabled(true);
		 editor.getTextField().setEditable(false);
		 
		 tailPollingFreqLabel = new JLabel("Polling freq.(seconds):");
		 
		 this.startListenerButton = new JButton("Start Listener");
		 this.userMesageLabel.setForeground(Color.green);
		 startListenerButton.addActionListener(this);
		 
		 this.stopListenerButton =  new JButton("Stop Listener");
		 stopListenerButton.addActionListener(this);
		 
		 this.add(new JLabel(headerMsg),"span 3");	
		 
		 this.add(new JSeparator(SwingConstants.HORIZONTAL),"span 3");
		 
		 this.add(swapFileLabel,"align left");
		 this.add(swapFileTextField,"growx,width 760");
		 this.add(swapFileBrowseButton,"width 130");
		 
		 this.add(tailPollingFreqLabel,"width 105");
		 this.add(tailPollingFreqSpinner,"width 105");
		 this.add(startListenerButton,"width 100"); 
		 this.add(userMesageLabel,"span 2,align center");
		 this.add(stopListenerButton,"width 100"); 		 
				 
		 this.add(messageQueueMonitorPanel,"span 3,growx");	
		 this.add(phoneManagerHelpPanel,"span 3,growx,wrap");
		 this.add(phoneMnagerCommandPanel,"span 3,gaptop 170");
	}
	
	
	/**
	 * Manage the events on the buttons
	 */
	public void actionPerformed(ActionEvent e) {
		
		final JFileChooser swapFileChooser = new JFileChooser();
				
	    if (e.getSource() instanceof JButton)
	    {	
	    	/**
	    	 * Choose the swap text file written by Android logcat utility and monitored in tail (FileTailListener) 
	    	 */
            if (e.getActionCommand().equals("Browse"))
            {          	  
            	swapFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            	swapFileChooser.setDialogTitle("Choose input file");
	              
	            int value = swapFileChooser.showOpenDialog(this);           
	           
	            if (value==JFileChooser.APPROVE_OPTION)
	            {           	  
	            	monitoredSwapFile = swapFileChooser.getSelectedFile();	            	  
	            	pathMonitoredSwapFile = monitoredSwapFile.getAbsolutePath();
	            	swapFileTextField.setText(pathMonitoredSwapFile); 
      	        }
	        }
            
            /* Start the Tail listener on the file written by LogCat listener  */
            if (e.getActionCommand().equals("Start Listener"))
            {
            	//validate input
            	if(verifier.verify(swapFileTextField))
            	{
            		this.messageQueueMonitorPanel.getTotalMessageProcessed().setText("0"); //reset the counter
            		this.messageQueueMonitorPanel.getMessageInQueue().setText("0");
	            	log.info("Starting tail file listener...");
	            	
	            	Integer freqPolling = (Integer) tailPollingFreqSpinner.getValue();
	            	int f = Integer.parseInt(freqPolling.toString());
	            	Long freq = Long.parseLong(f+"")*1000; //to convert from ms to second the polling frequency
	            	
	            	/* Disable some widgets */
	            	updateGui(false);	
	            	
	            	/* start the tail listener on txt written by Android 'logCat' command previously executed in a shell */
	            	this.pcListener = new PcListener(this.swapFileTextField.getText(),freq,this.messageQueueMonitorPanel);    	
	            	this.pcListener.startListener();
	            	            	
	             	this.userMesageLabel.setText("Listener status: RUNNING");             	
                }
            } 
            
            if (e.getActionCommand().equals("Stop Listener"))
            {
            	log.info("Stopping tail file listener...");
            	
            	/* Enable some widget */
            	updateGui(true);
            	
            	this.pcListener.stopListener();            	
            	this.userMesageLabel.setText("Listener status: STOPPED");            	
            }
	    }   
	}
	
	/**
	 * Utility method that enable/disable some widget in the UI when the user has pressed the start/stop listener button.
	 * For example disable some widget when the start listener button is pressed to prevent mofifications while the service is running 
	 * 
	 * @param flag boolean flag to enable/disable a component
	 */
	private void updateGui(boolean flag){
		
		this.startListenerButton.setEnabled(flag); 
    	this.swapFileTextField.setEditable(flag);
    	this.swapFileTextField.setEnabled(flag);
    	this.swapFileBrowseButton.setEnabled(flag);
    	this.tailPollingFreqSpinner.setEnabled(flag);
	}
	
	
	public JLabel getSwapFileLabel() {
		return swapFileLabel;
	}

	public void setSwapFileLabel(JLabel swapFileLabel) {
		this.swapFileLabel = swapFileLabel;
	}

	public JTextField getSwapFileTextField() {
		return swapFileTextField;
	}

	public void setSwapFileTextField(JTextField swapFileTextField) {
		this.swapFileTextField = swapFileTextField;
	}

	public JButton getSwapFileBrowseButton() {
		return swapFileBrowseButton;
	}

	public void setSwapFileBrowseButton(JButton swapFileBrowseButton) {
		this.swapFileBrowseButton = swapFileBrowseButton;
	}

}
	
	
	
	
