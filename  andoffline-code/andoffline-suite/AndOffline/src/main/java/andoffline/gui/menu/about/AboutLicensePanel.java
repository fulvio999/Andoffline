
package andoffline.gui.menu.about;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * A sub-panel that compose the AboutMenuPopUp.
 * Contains information about the License of the product and the third part library used 
 *
 */
public class AboutLicensePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JLabel applicationLicenseLabel;
	
	private JLabel licenseMsgThirdPartLabel;

	/**
	 * Constructor
	 */
	public AboutLicensePanel() {	
		
		 this.setBorder(BorderFactory.createTitledBorder(""));
		
		 setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		 // The message for the license for the AndoffLine application
		 applicationLicenseLabel = new JLabel("<html> <br/> <b>AndOffline</b> <br/><br/> This application is released under the term of the: <br/>  GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007 <br/> See the License.txt file or go to: http://www.gnu.org/licenses/gpl-3.0.en.html <br/><br/> </html>");
		 
		 this.add(applicationLicenseLabel);		 
		 this.add(new JSeparator());
		 
		 // The message for the license for the third part library used
		 licenseMsgThirdPartLabel = new JLabel("<html> <br/> <b>Third Part Library License:</b> <br/><br/> Apache commons codes, See: http://www.apache.org/licenses/ <br/><br/> Itext library, See: http://itextpdf.com/terms-of-use/agpl.php <br/><br/> Izpack library, See: http://izpack.org/features/ <br/><br/> Swingx library See: http://www.opensource.org/licenses/lgpl-2.1.php </br> </html>");
		 
		 this.add(licenseMsgThirdPartLabel);
	}

}
