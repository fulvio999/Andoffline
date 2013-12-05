package andoffline.gui.panel.imageconverter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/**
 * Create a panel with some commands to execute Image <---> Base64 conversion
 *
 */
public class ConvertImageCommandPanel extends JPanel implements ActionListener{    
    
   private static final long serialVersionUID = 1L;
	
   private JButton startConversionButton;
   private JButton closeButton;   
    
    // The parent JFrame that the close button must close
    private JFrame mainFrame;
    
    private ImageEncoderDecoderTab imageEncoderDecoderTab;
    
 
	/**
	 * Constructor
	 * @param imageConvertPanel The panel with the source and destination files
	 */
	public ConvertImageCommandPanel(JFrame mainFrame, ImageEncoderDecoderTab imageConvertPanel) {
		
		this.setBorder(BorderFactory.createTitledBorder("Commands"));
		this.setLayout(new MigLayout("wrap 2")); // we want 2 columns
		
		this.mainFrame = mainFrame;
		this.imageEncoderDecoderTab = imageConvertPanel;
		
		startConversionButton  = new JButton("Convert");
		startConversionButton.addActionListener(this);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(this); 	       
       
        this.add(new JLabel(""),"gapleft 700,growx");       
        this.add(startConversionButton,"split 2,width 105");   
        this.add(closeButton,"width 105");         
	}
	
    /**
     * Manage the actions registered on the "Close" and "Convert" buttons
     * 
     */
	public void actionPerformed(ActionEvent e) {
		
		  // Check if user has pressed some button 
	      if (e.getSource() instanceof JButton)  
	      {	    	  
	          //-- True if the use ha pressed the Close button
	          if (e.getActionCommand().equals("Close")){
	        	  
        		  if (mainFrame.isDisplayable()) {                     
        			  mainFrame.dispose();
                  }
        	  }
	          //-- True if the use ha pressed the Convert button
	          if (e.getActionCommand().equals("Convert")){
	        	  
        		  String sourceFilePath = this.imageEncoderDecoderTab.getSourceFileTextField().getText();        		 
        		  String destFilePath = this.imageEncoderDecoderTab.getDestinationFileTextField().getText();        		        		  
        		  String conversionType = (String) this.imageEncoderDecoderTab.getConvertTypeCombo().getSelectedItem();
        		  
        		  //The conversion type, necessary to know which decoder create
        		  if(conversionType.equalsIgnoreCase("Base64 TO Image")){
        			  
        			  ConverterBase64ToImage convertBase64ToImage = new ConverterBase64ToImage();
        			  
        			  try {        			
        				    //The output format is obtained from the metadata in the source file (if any) otherwise is asked to the user
							String fileTypeWritten = convertBase64ToImage.convert(sourceFilePath, destFilePath);
							//System.out.println("Image type out format:"+fileTypeWritten);
							
							this.imageEncoderDecoderTab.getLogPanel().getExecutionLogTextArea().append("Conversion Started... \n"); 
							this.imageEncoderDecoderTab.getLogPanel().getExecutionLogTextArea().append("Source text file to convert: "+sourceFilePath+"\n"); 
							this.imageEncoderDecoderTab.getLogPanel().getExecutionLogTextArea().append("Image written to file: "+destFilePath+"."+fileTypeWritten+"\n");							
		        			this.imageEncoderDecoderTab.getLogPanel().repaint();
		        			
        			  } catch (Exception ex) {
        				  	this.imageEncoderDecoderTab.getLogPanel().getExecutionLogTextArea().setText("Error: " +ex.getMessage()); 
					  }
       			  
        		  }else{ //Image --> Base64
        			  
        			  ConverterImageToBase64 convertImageToBase64 = new ConverterImageToBase64();
       			  
        			  try {   
        				    this.imageEncoderDecoderTab.getLogPanel().getExecutionLogTextArea().append("Conversion Started... \n"); 
        				    
							String operationResult = convertImageToBase64.convert(sourceFilePath, destFilePath);	
							
							this.imageEncoderDecoderTab.getLogPanel().getExecutionLogTextArea().append("Image to convert: "+sourceFilePath+"\n"); 
							this.imageEncoderDecoderTab.getLogPanel().getExecutionLogTextArea().append("Result written to file: "+destFilePath+".txt\n"); 
							this.imageEncoderDecoderTab.getLogPanel().getExecutionLogTextArea().append(operationResult+"\n"); 
		        			this.imageEncoderDecoderTab.getLogPanel().repaint();
		        			
					  } catch (Exception ex1) {
						 this.imageEncoderDecoderTab.getLogPanel().getExecutionLogTextArea().setText("Error: "+ex1.getMessage()); 
					  }
        		  }        			  
        	  } 
	        	  
	       }		
	}	

	
	public JButton getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(JButton closeButton) {
		this.closeButton = closeButton;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}


	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}


	public JButton getStartConversionButton() {
		return startConversionButton;
	}


	public void setStartConversionButton(JButton startConversionButton) {
		this.startConversionButton = startConversionButton;
	}

	public ImageEncoderDecoderTab getImageEncoderDecoderTab() {
		return imageEncoderDecoderTab;
	}

	public void setImageEncoderDecoderTab(
			ImageEncoderDecoderTab imageEncoderDecoderTab) {
		this.imageEncoderDecoderTab = imageEncoderDecoderTab;
	}


}
