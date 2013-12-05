package andoffline.gui.panel.imageconverter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Create a panel with some commands to manage the jtree with the sms (eg. filter the sms displayed, export them in pdf...)
 *
 */
public class OptionPanel extends JPanel{
    
   private static final long serialVersionUID = 1L;
   
   //Available output format for "IMAGE TO BASE64" conversion
   private static String[] outputFormatTypeOption = {"TXT file", "VCF file"};
   
   //the output text file format to write
   private JComboBox outputFormatTypeCombo;
   
   private JCheckBox appendMetaDataCheckBox;
  
	/**
	 * Constructor
	 *
	 */
	public OptionPanel() {		
		
		this.setLayout(new GridBagLayout());
       
        GridBagConstraints c = new GridBagConstraints();
        
        this.setBorder(BorderFactory.createTitledBorder("Options (only for Image to Base64 conversion)"));	
        outputFormatTypeCombo = new JComboBox(outputFormatTypeOption);
        
        appendMetaDataCheckBox = new JCheckBox();
        
        //------ 1st row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        c.weighty = 0;
        c.gridwidth = 1; //the column number to span
        this.add(new JLabel("Output File Type:"),c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1; //the column number to span
        this.add(outputFormatTypeCombo,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0.6;
        c.weighty = 0; 
        c.gridwidth = 1;
        this.add(new JLabel(""),c);
         
	}

	public JComboBox getOutputFormatTypeCombo() {
		return outputFormatTypeCombo;
	}

	public void setOutputFormatTypeCombo(JComboBox outputFormatTypeCombo) {
		this.outputFormatTypeCombo = outputFormatTypeCombo;
	}

	public JCheckBox getAppendMetaDataCheckBox() {
		return appendMetaDataCheckBox;
	}

	public void setAppendMetaDataCheckBox(JCheckBox appendMetaDataCheckBox) {
		this.appendMetaDataCheckBox = appendMetaDataCheckBox;
	}

}
