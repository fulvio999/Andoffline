
package andoffline.gui.panel.phonemanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/**
 * Command panel (ie: panel with the available commands, buttons...) for the 'Phone Manager' tab panel
 *
 */
public class PhoneManagerCommandPanel extends JPanel implements ActionListener{
	
    private static final long serialVersionUID = 1L;
   
    private JButton closeButton;    
    
    //The parent JFrame that the "Close" button must close
    private JFrame mainFrame;
    
    /**
     * Constructor
     */
    public PhoneManagerCommandPanel(JFrame mainFrame){
    	
    	this.setBorder(BorderFactory.createTitledBorder("Commands"));
		this.setLayout(new MigLayout("wrap 3")); // we want 3 columns
		
		this.mainFrame = mainFrame;		
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);

        this.add(new JLabel(""));
        this.add(new JLabel(""),"gapleft 280");
        this.add(closeButton,"width 105,gapleft 550");
    }

    
	public JButton getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(JButton closeButton) {
		this.closeButton = closeButton;
	}


	public void actionPerformed(ActionEvent e) {		
		 
	      if (e.getSource() instanceof JButton)
	      {	
              if (e.getActionCommand().equals("Close")){
            	  
        		  if(mainFrame.isDisplayable()) {                     
        			 mainFrame.dispose();
                  }
        	  }
	      }		
	}

}
