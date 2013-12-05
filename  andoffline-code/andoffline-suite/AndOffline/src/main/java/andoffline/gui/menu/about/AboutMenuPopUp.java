
package andoffline.gui.menu.about;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

/**
 * The pop-up window showed when the user click on the "About" entry in the menu bar
 *
 */
public class AboutMenuPopUp extends JDialog{

	
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;

	/**
	 * Constructor
	 */
	public AboutMenuPopUp() {	
		
			this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));			
			
			tabbedPane = new JTabbedPane(); 
			
			tabbedPane.addTab("General Info", new GeneralinfoPanel());
			tabbedPane.addTab("License", new AboutLicensePanel());
			
			this.add(tabbedPane);
	
		    //---- The Close button that close the pop up window
		    JButton closeButton = new JButton("Close");
		    closeButton.addActionListener(new ActionListener() {
	
			    public void actionPerformed(ActionEvent event) {
			        dispose();
			    }
		    });
		    //---------------------
	       
	        this.add(closeButton);

	        //set the dialog as a modal window
	        this.setModalityType(ModalityType.APPLICATION_MODAL);

	        this.setTitle("About");
	        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        this.setLocationRelativeTo(null);
	        this.setSize(700, 400);    
				
	        this.setVisible(true);		
	}

}
