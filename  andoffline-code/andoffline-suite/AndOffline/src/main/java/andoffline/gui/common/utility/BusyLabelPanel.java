package andoffline.gui.common.utility;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXPanel;

/**
 * Create a Panel with an animation to indicate that a processing is in action. * 
 * Note: this animation can't appear if the required time is too little
 * 
 * (The animation is provided by the Swingx library) 
 *
 */
public class BusyLabelPanel extends JXPanel {
	
	private JXBusyLabel jxBusyLabel;

	/**
	 * Constructor
	 */
	public BusyLabelPanel() {
		
		jxBusyLabel = new JXBusyLabel();
		  
		// Start the animation
		jxBusyLabel.setBusy(true);  
		jxBusyLabel.setText("Processing...");
		jxBusyLabel.setVisible(false); //show it only when a processing is in action
		jxBusyLabel.setEnabled(false); 
		  
		// set the transparency of the JXPanel to 50% transparent
		this.setAlpha(0.7f);		
		 
		JXPanel busylabels = new JXPanel(new FlowLayout(FlowLayout.CENTER, 40, 5));
		busylabels.add(jxBusyLabel);	  
		 
		this.add(busylabels, BorderLayout.CENTER);		
	}
	
	public JXBusyLabel getJxBusyLabel() {
		return jxBusyLabel;
	}

	public void setJxBusyLabel(JXBusyLabel jxBusyLabel) {
		this.jxBusyLabel = jxBusyLabel;
	}  

}
