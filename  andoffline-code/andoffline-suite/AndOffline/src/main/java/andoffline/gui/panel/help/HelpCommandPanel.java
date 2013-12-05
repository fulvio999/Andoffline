
package andoffline.gui.panel.help;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/**
 * Create a command panel to be placed in the Help Panel (ie a panel with only the close button)
 *
 */
public class HelpCommandPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;

	 // The parent JFrame that the close button must close
    private JFrame mainFrame;
    
    private JButton closeButton;
	
	/**
	 * Constructor
	 * @param mainFrame 
	 */
	public HelpCommandPanel(JFrame mainFrame) {
		
		this.setBorder(BorderFactory.createTitledBorder("Commands"));
		this.setLayout(new MigLayout("wrap 1")); // we want 1 columns
				
		this.mainFrame = mainFrame;
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		this.add(closeButton,"width 105,align right, gapleft 850");		
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public JButton getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(JButton closeButton) {
		this.closeButton = closeButton;
	}

	public void actionPerformed(ActionEvent e) 
	{
		  //--- Check if user has pressed some button 
	      if (e.getSource() instanceof JButton)  
	      {	    	  
	          //------------------- True if the user has pressed the Close button
	    	  if (e.getActionCommand().equals("Close")){
        		  if (mainFrame.isDisplayable()) {                     
        			  mainFrame.dispose();
                  }		
	      }
	}
   }

}