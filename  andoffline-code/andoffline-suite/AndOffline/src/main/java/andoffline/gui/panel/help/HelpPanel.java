
package andoffline.gui.panel.help;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXHyperlink;

import andoffline.utility.HyperlinkActionHandler;

/**
 * Create an Help panel with some informations about the functionality available
 *
 */
public class HelpPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	// The Container frame that the "close" jbutton of the command panel must close
    private JFrame mainFrame;
    
    // The panel with the close button
    private HelpCommandPanel helpCommandPanel;
    
    // The url at the web site where is available the sms-backup-restore android Application
    private static String smsBackupUrl = "http://android.riteshsahu.com/apps/sms-backup-restore";
    
    // The url at the web site where is available the call-logs-backup-restore android Application
    private static String callLogBackupUrl = "http://android.riteshsahu.com/apps/call-logs-backup-restore";
 
    
	/**
	 * Constructor
	 */
	public HelpPanel(JFrame mainFrame) {
		
		 this.setBorder(BorderFactory.createTitledBorder("Help"));		       
		 this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));			 
		 this.mainFrame = mainFrame;
		 
		 //------------ Help for SMS Browser panel ------------
		 String smsHelpMsg = "<html><b><br/> SMS Browser</b> <br/> The SMS Browser allow you to visualize in a graphic mode all the sms exported from your Android phone with the free application: <br/> </html>";
		 
		 this.add(new JLabel(smsHelpMsg));
		 //Special Component offered by Swingx library
		 this.add(new JXHyperlink(new HyperlinkActionHandler(smsBackupUrl)));
		
		 this.add(new JSeparator(SwingConstants.HORIZONTAL));
		 
		 //----------- Help for Log Call panel ---------------
		 String logCallHelpMsg = "<html><b><br/> Log Call Browser</b> <br/> The Log Call Browser allow you to visualize in a graphic mode all the phone call exported from your Android phone with the free application <br/> </html>";
		 
		 this.add(new JLabel(logCallHelpMsg));	
		 //Special Component offered by Swingx library
		 this.add(new JXHyperlink(new HyperlinkActionHandler(callLogBackupUrl)));
		 
		 this.add(new JSeparator(SwingConstants.HORIZONTAL));
		 
		 //----------- Help for contact panel ----------------
		 String contactHelpMsg = "<html><b><br/> Contact Browser</b> <br/> The Contact Browser allow you to visualize in a more human readable mode all the contacts exported from your Android phone with the native exporter of your phone <br/>  </html>";
		 
		 this.add(new JLabel(contactHelpMsg));		 
		 this.add(new JSeparator(SwingConstants.HORIZONTAL));
		 
		 //---------- Help for image encoder/decoder panel -----------------
		 String imageConvertHelpMsg = "<html><b><br/> Image Encoder/Decoder</b> <br/> Convert an image file into his Base64 format and viceversa <br/> </html>";
		 
		 this.add(new JLabel(imageConvertHelpMsg));		 
		 this.add(new JSeparator(SwingConstants.HORIZONTAL));
		 
		 //----------- Help for Server Panel -------------------
		 String serverHelpMsg = "<html><b><br/> Server</b> <br/> Start a server to show the saved informations (sms,call) with the browser <br/> </html>";
		 
		 this.add(new JLabel(serverHelpMsg));		 
		 this.add(new JSeparator(SwingConstants.HORIZONTAL));
		 
		 //----------- Phone Manager Panel
         String phoneManagerHelpMsg = "<html><b><br/> Phone Manager</b> <br/> Allow you to configure the interactiong between USB connected Android phone and the PC <br/> </html>";
		 
		 this.add(new JLabel(phoneManagerHelpMsg));		 
		 this.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		 //----------- Phone Manager Panel
         String jobManagerHelpMsg = "<html><b><br/> Job Manager</b> <br/> Allow you to manage the jobs that are callable by sms <br/> </html>";
		 
		 this.add(new JLabel(jobManagerHelpMsg));		 
		 this.add(new JSeparator(SwingConstants.HORIZONTAL));

		 
		 // dummy place-holder for layout adjusting
		 this.add(new JLabel("<html><br/></html>"));
		 this.add(new JLabel("<html><br/><br/><br/><br/><br/></html>"));
		 
		 helpCommandPanel = new HelpCommandPanel(mainFrame);
		 this.add(helpCommandPanel);		 
	}
	

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public HelpCommandPanel getHelpCommandPanel() {
		return helpCommandPanel;
	}

	public void setHelpCommandPanel(HelpCommandPanel helpCommandPanel) {
		this.helpCommandPanel = helpCommandPanel;
	}

	


}
