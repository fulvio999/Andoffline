
package andoffline.utility.validation;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

/**
 * Validator class for the input inserted in JTextField components
 *
 */
public class TextFieldValidator extends InputVerifier{

	/**
	 * Constructor
	 */
	public TextFieldValidator() {
		
	}

	@Override
	public boolean verify(JComponent input) {
		
		 if (input instanceof JTextField)
		 {			 		 
			 JTextField f = (JTextField) input;
			 f.setForeground(Color.BLACK);
			 
			 if(StringUtils.isEmpty(f.getText()))
			 {	
				 f.setText("REQUIRED FIELD");
				 f.setBorder(BorderFactory.createLineBorder(Color.RED));				 
				 return false;
				 
			 }else{
				 f.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				 return true; 
			 }
				
		 }
		
		return false;
	}

}
