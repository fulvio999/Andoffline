package andoffline.gui.common.panel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;


/**
 * Create a log panel where show the execution log and others messages.
 * 
 * Currently is used in the Tab Image encoder/decoder
 *
 */
public class LogPanel extends JPanel {
	
	private static final long serialVersionUID = 3880679443211225584L;
	
	private JTextArea executionLogTextArea;
    private JScrollPane executionLogScrollPanel;
 
	/**
	 * Constructor
	 */
	public LogPanel() {
		
		this.setBorder(BorderFactory.createTitledBorder("Output Messages:"));
		this.setLayout(new MigLayout("wrap 1")); // we want 1 columns
      
        executionLogTextArea = new JTextArea();    
		executionLogTextArea.setEditable(false);
		executionLogTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
        executionLogScrollPanel = new JScrollPane(executionLogTextArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       
        this.add(executionLogScrollPanel,"growx,growy,width 950,height 500");      
	}
	
	/**
	 * Utility method that show a message in the text area
	 * @param message The message to show
	 */
	public void showMessage(String message){
		this.executionLogTextArea.setText(message);
	}


	public JTextArea getExecutionLogTextArea() {
		return executionLogTextArea;
	}


	public JScrollPane getExecutionLogScrollPanel() {
		return executionLogScrollPanel;
	}

	public void setExecutionLogTextArea(JTextArea executionLogTextArea) {
		this.executionLogTextArea = executionLogTextArea;
	}


	public void setExecutionLogScrollPanel(JScrollPane executionLogScrollPanel) {
		this.executionLogScrollPanel = executionLogScrollPanel;
	}
  
}
