
package andoffline.gui.panel.phonemanager;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

/**
 * Sub-panel of {@link PhoneManagerTab} with the necessary steps to follow
 * to start the phone manager.
 *
 */
public class PhoneManagerHelpPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(PhoneManagerHelpPanel.class);
	
	private JLabel steptoFollowLabel;

	/**
	 * Constructor
	 */
	public PhoneManagerHelpPanel() {
		
		this.setBorder(BorderFactory.createTitledBorder("Quick ordered steps to follow"));	
		this.setLayout(new MigLayout("wrap 1"));
		
		steptoFollowLabel = new JLabel("<html> <ul> <li>1) Connect the phone to pc with USB cable and enable debug USB on phone</li> <li>2) On pc, start the message interceptor (See 'pc-listener-service.script' in the 'doc' folder) </li>  <li>3) In the tab panel 'Phone Manager' set the path to swap file created during the previous step (eg: andofflinePhone.txt)</li>  <li>4) Start phone manger in the tab 'Phone Manager' </li> <li>4) Configure and start the Android appliction on phone</li> </ul> </html>");
		this.add(steptoFollowLabel);		
	}

}
