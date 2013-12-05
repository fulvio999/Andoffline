
package andoffline.gui.menu.about;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A sub-panel that compose the AboutMenuPopUp.
 * Contains some general information about Application
 *
 */
public class GeneralinfoPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JLabel generalInfoLabel;

	/**
	 * Constructor
	 */
	public GeneralinfoPanel() {
		
		this.setBorder(BorderFactory.createTitledBorder(""));
		
		generalInfoLabel = new JLabel("<html> <br/><b> AndOffline</b> <br/><br/> A support tool for exported SMS, Log Call and Contact for Android Phones <br/> See The Help panel of the application for details  <br/><br/> Author: fulvio999@gmail.com </html>");
		
		this.add(generalInfoLabel);		
	}

}
