
package andoffline.gui.panel.imageconverter;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import andoffline.gui.common.panel.LogPanel;



/**
 * Main panel for the image Converter Panel
 *
 */
public class ImageEncoderDecoderTab extends JPanel implements ActionListener, ChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	// The Container frame that the "close" jbutton must close
    private JFrame mainFrame;
	
	/* Source File data (ie an image file or a text file with a base 64 content)*/
	private JLabel sourceImageLabel;
    private JTextField sourceFileTextField; //the textField with the name of the source file
    private String pathSourceFile; //the absolute path to the source file
    
    /* Destination File data */ 
    private JLabel destinationImageLabel;
    private JTextField destinationFileTextField; //the textField with the name of the destination file
    private String pathDestinationFile;
    
    private JButton sourceBrowseButton; //the textField with the name of the input image
    private JButton destinationBrowseButton; //the textField used to select the destination image file  
    
    //The panel with the convertion options
    private OptionPanel optionPanel;    
    private LogPanel logPanel;    
    private JLabel operationResultLabel;    
    private ConvertImageCommandPanel convertImageCommandPanel;
    
    private static String[] convertTypeOption = { "Image TO Base64", "Base64 TO Image"};
    
    //the conversion to execute image <--> base64
    private JComboBox convertTypeCombo;
    
    //the output text file format to write
    private JComboBox outputFormatTypeCombo;

    /**
     * Constructor
     * @param mainFrame
     */
	public ImageEncoderDecoderTab(JFrame mainFrame) 
	{		
		this.setBorder(BorderFactory.createTitledBorder("Configuration"));		
		this.setLayout(new MigLayout("wrap 3")); // we want 3 columns
		
		this.mainFrame = mainFrame;		
		this.optionPanel = new OptionPanel();		
		this.logPanel = new LogPanel();
		
		//The panel with the command buttons
		convertImageCommandPanel = new ConvertImageCommandPanel(mainFrame,this);
       
        sourceImageLabel = new JLabel("Source File:");
        sourceFileTextField = new JTextField();       
       
        destinationImageLabel = new JLabel("Destination File:");
        destinationFileTextField = new JTextField();        
       
        JTextField imageTypeTextField = new JTextField();
        imageTypeTextField.setEditable(false);
        imageTypeTextField.setEnabled(false);
        
        JTextField imageSizeTextField = new JTextField();
        imageSizeTextField.setEditable(false);
        imageSizeTextField.setEnabled(false);
        
        sourceBrowseButton = new JButton("Find");
        sourceBrowseButton.addActionListener(this);
         
        destinationBrowseButton = new JButton("Choose");
        destinationBrowseButton.addActionListener(this);         
        
        convertTypeCombo = new JComboBox(convertTypeOption);        
       
        //---- 1st row  
		this.add(new JLabel("<html> <b>Base64 TO Image</b>  <br/> - This conversion accept as input only a .txt file or .vcf file containing ONLY the Base64 data to convert <br/><br/> </html>"),"span 3,wrap");
		this.add(new JLabel("<html> <b>Image TO Base64</b>  <br/> - This conversion accept as input only an image file and produce a <b>.txt</b> file with the Base64 data <br/><br/> </html>"),"span 3,wrap");
          
        //---- 2nd row       
        this.add(new JLabel("Conversion Type:"));  
        this.add(convertTypeCombo);       
        this.add(new JLabel(""));
          
        //------- 3rd row       
        this.add(sourceImageLabel);      
        this.add(sourceFileTextField,"width 720");      
        this.add(sourceBrowseButton,"width 105");        
       
        //--------- 4th row       
        this.add(destinationImageLabel);
        this.add(destinationFileTextField,"width 720");    
        this.add(destinationBrowseButton,"width 105"); 
      
        //------ 5th row        
        this.add(logPanel,"span 3,height 1000,growx,wrap");        
        
        //------ 6th row       
        this.add(convertImageCommandPanel,"span 3,growx,width 1000");  		
	}


	/* 
	 * Manage the Actions registered on the buttons for choose the source / destination file
	 */
	public void actionPerformed(ActionEvent e) 
	{
		  final JFileChooser sorceFolderFileChooser;
	      final JFileChooser destinationFolderFileChooser;
	   
	      //-- Check if user has pressed some button 
	      if (e.getSource() instanceof JButton)  
	      {
	          //if true user want choose a source image file
	          if (e.getActionCommand().equals("Find")) 
	          {
	        	  sorceFolderFileChooser = new JFileChooser();
	              sorceFolderFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	              sorceFolderFileChooser.setDialogTitle("Choose the source file");
	              sorceFolderFileChooser.setAcceptAllFileFilterUsed(false); //disable the default fileChooserFilter
	              //sorceFolderFileChooser.setFileFilter(new XmlFileFilter());
	              
	              int value = sorceFolderFileChooser.showOpenDialog(this);
	             
	              //Return value if approve (yes, ok) is chosen.
	              if (value==JFileChooser.APPROVE_OPTION)
	              {
	            	  File  f = sorceFolderFileChooser.getSelectedFile();
	            	  //get the absolute path of the chosen file
	                  pathSourceFile = f.getAbsolutePath(); 
	                  //set the file path in the textField
	                  sourceFileTextField.setText(pathSourceFile); 
	          
	              }
	           
	          }
	          if (e.getActionCommand().equals("Choose")) //show the file chooser for the destination file 
	          {
	              
	        	  destinationFolderFileChooser =  new JFileChooser();
	        	  destinationFolderFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        	  destinationFolderFileChooser.setDialogTitle("Choose the destination File");
	        	  destinationFolderFileChooser.setAcceptAllFileFilterUsed(false); //disable the default fileChooserFilter
	        	  //destinationFolderFileChooser.setFileFilter(new DirectoryFilter());
	        	  
	        	  if(pathSourceFile !=null)
	        		  destinationFolderFileChooser.setCurrentDirectory(new java.io.File(pathSourceFile));
	        	  
	              int value = destinationFolderFileChooser.showOpenDialog(this);
	              
	              /* Create a loop toCheck if the destination file has the extension: no file extension must be inserted
	               * The file type is detected in auto by the decoder
	               */
	              while(value == JFileChooser.APPROVE_OPTION && getFileExtension(destinationFolderFileChooser.getSelectedFile().getName())!=null){
	            	    JOptionPane.showMessageDialog(null,"<html>Remove the Extension from the file name chosen<br>The output image format will be autodetected<html> ", "Open Error", JOptionPane.ERROR_MESSAGE);
	            	    value = destinationFolderFileChooser.showOpenDialog(this);
	              }
	              
	              File  f = destinationFolderFileChooser.getSelectedFile();
                  
                  //get the absolute path of the choosed file
                  pathDestinationFile = f.getAbsolutePath(); 
              
                  //set the file path in the textField
                  destinationFileTextField.setText(pathDestinationFile); 
	        	   
	          }

	      }
	}

	
	/**
	  * Utility method to extract the extension of the given file
	  * @param fullPath
	  * @return
	  */
	 private String getFileExtension(String fullPath) 
	 {
		 try {
			 int dot = fullPath.lastIndexOf(System.getProperty("file.separator"));
			 String file = fullPath.substring(dot + 1); //.split(".")[0];
			 
			 return file.split("\\.")[1];
			 
		 } catch(Exception e){
			 return null; //the file have no extension
		 }
		
	 }
	
	
	public void stateChanged(ChangeEvent e) {		
		
	}


	public JFrame getMainFrame() {
		return mainFrame;
	}


	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	
	public ConvertImageCommandPanel getConvertImageCommandPanel() {
		return convertImageCommandPanel;
	}


	public void setConvertImageCommandPanel(
			ConvertImageCommandPanel convertImageCommandPanel) {
		this.convertImageCommandPanel = convertImageCommandPanel;
	}


	public JLabel getSourceImageLabel() {
		return sourceImageLabel;
	}


	public void setSourceImageLabel(JLabel sourceImageLabel) {
		this.sourceImageLabel = sourceImageLabel;
	}


	public String getPathSourceFile() {
		return pathSourceFile;
	}


	public void setPathSourceFile(String pathSourceFile) {
		this.pathSourceFile = pathSourceFile;
	}


	public JLabel getDestinationImageLabel() {
		return destinationImageLabel;
	}


	public void setDestinationImageLabel(JLabel destinationImageLabel) {
		this.destinationImageLabel = destinationImageLabel;
	}


	public JLabel getOperationResultLabel() {
		return operationResultLabel;
	}


	public void setOperationResultLabel(JLabel operationResultLabel) {
		this.operationResultLabel = operationResultLabel;
	}


	public JComboBox getConvertTypeCombo() {
		return convertTypeCombo;
	}


	public void setConvertTypeCombo(JComboBox convertTypeCombo) {
		this.convertTypeCombo = convertTypeCombo;
	}


	public JButton getSourceBrowseButton() {
		return sourceBrowseButton;
	}


	public void setSourceBrowseButton(JButton sourceBrowseButton) {
		this.sourceBrowseButton = sourceBrowseButton;
	}


	public JButton getDestinationBrowseButton() {
		return destinationBrowseButton;
	}


	public void setDestinationBrowseButton(JButton destinationBrowseButton) {
		this.destinationBrowseButton = destinationBrowseButton;
	}


	public JTextField getDestinationFileTextField() {
		return destinationFileTextField;
	}


	public void setDestinationFileTextField(JTextField destinationFileTextField) {
		this.destinationFileTextField = destinationFileTextField;
	}

	public JTextField getSourceFileTextField() {
		return sourceFileTextField;
	}


	public void setSourceFileTextField(JTextField sourceFileTextField) {
		this.sourceFileTextField = sourceFileTextField;
	}


	public String getPathDestinationFile() {
		return pathDestinationFile;
	}


	public void setPathDestinationFile(String pathDestinationFile) {
		this.pathDestinationFile = pathDestinationFile;
	}


	public JComboBox getOutputFormatTypeCombo() {
		return outputFormatTypeCombo;
	}


	public void setOutputFormatTypeCombo(JComboBox outputFormatTypeCombo) {
		this.outputFormatTypeCombo = outputFormatTypeCombo;
	}


	public OptionPanel getOptionPanel() {
		return optionPanel;
	}


	public void setOptionPanel(OptionPanel optionPanel) {
		this.optionPanel = optionPanel;
	}


	public LogPanel getLogPanel() {
		return logPanel;
	}


	public void setLogPanel(LogPanel logPanel) {
		this.logPanel = logPanel;
	}
	
}
