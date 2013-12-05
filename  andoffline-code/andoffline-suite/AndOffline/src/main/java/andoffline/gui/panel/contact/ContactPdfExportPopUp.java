
package andoffline.gui.panel.contact;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import andoffline.exporter.pdf.ContactToPdfExporter;
import andoffline.parser.vcf.VcardRecord;

/**
 *  Create a Pop-Up window showed when the user press the "Export To PDF" button placed in Contact Command panel
 *  In the showed Pop-Up the user can insert some options (ie file name and destination folder) about the pdf file where
 *  will be 
 *
 */
public class ContactPdfExportPopUp extends JDialog implements ActionListener{
	
    private static final long serialVersionUID = 1L;
    
    private static final Logger log = Logger.getLogger(ContactPdfExportPopUp.class);
    
    private static String OPERATION_MSG_SUCCESS = "Operation Executed Successfully !";
	private static String INVALID_INPUT_ERROR_MSG = "Invalid Input !";
	
    private JLabel fileNameLabel;
	private JTextField fileNameTextField;
	private JLabel destinationfolderLabel;
	private JButton chooseFolderButton;
	private JTextField destinationFolderTextField;
	
	private JButton confirmButton;
	private JButton closeButton;
	
	/* A label that show the operation result (ie pdf export) */
	private JLabel operationResultLabel;
	
	/*  The list of vcard records extracted from a vcf file */
	private List<VcardRecord> vcardRecordList;

	/**
	 * Constructor
	 * @param mainFrame The root main frame to close when the user press the Close button (ie the full application is closed)
	 *
	 */
	public ContactPdfExportPopUp(JFrame mainFrame,List<VcardRecord> vcardRecordList) {
		
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Options"));
	
		this.setLayout(new MigLayout("wrap 3")); // we want 3 columns		
		this.vcardRecordList = vcardRecordList;		
		this.fileNameTextField = new JTextField();
		
		fileNameLabel = new JLabel("File Name: ");
		destinationfolderLabel = new JLabel("Destination Folder:");
		destinationFolderTextField = new JTextField();
		chooseFolderButton = new JButton("Choose");
		chooseFolderButton.addActionListener(this);
        
		confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(this);
        
        closeButton = new JButton("Close");
        closeButton.addActionListener(this);       
		
        //--- 1st row       
        this.add(fileNameLabel);
        this.add(fileNameTextField,"width 450");
        this.add(new JLabel("")); //placeholder
        
        //--- 2nd row
        this.add(destinationfolderLabel);
        this.add(destinationFolderTextField,"width 450"); 
        this.add(chooseFolderButton,"width 105");         
         
        //---- 3rd row
        this.add(new JLabel("")); //placeholder    
        this.add(confirmButton,"width 105,split 2");        
        this.add(closeButton,"width 105");   
        this.add(new JLabel("")); //placeholder 
        
        /* operation result Label */
        operationResultLabel = new JLabel("");
        this.add(operationResultLabel,"span 3,align center");
   
        /* set the dialog as a modal window */
        this.setModalityType(ModalityType.APPLICATION_MODAL);

        this.setTitle("PDF Export Options");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(600,200);  			
        this.setVisible(true);		
	}

	/**
	 * Manage the events on the "Choose" button
	 */
	public void actionPerformed(ActionEvent e) {
		
		  final JFileChooser destinationFolderChooser = new JFileChooser();		
		 
	      if (e.getSource() instanceof JButton)  
	      { 
	    	  if (e.getActionCommand().equals("Choose")) 
	          {
	    		  destinationFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //only folder allowed
	    		  destinationFolderChooser.setDialogTitle("Choose a destination folder");
	    		  
	    		  int value = destinationFolderChooser.showOpenDialog(this);
	    		  
	    		  if (value == JFileChooser.APPROVE_OPTION)
	              {
	    			  File destinationFolderChosen = destinationFolderChooser.getSelectedFile();
	            	  
	            	  //get the absolute path of the chosen file
	            	  String folderChosen = destinationFolderChosen.getAbsolutePath(); 
	    			  this.destinationFolderTextField.setText(folderChosen);
	              }	  
	          }
	    	  
	    	  //The user confirm the pdf export operation
	    	  if (e.getActionCommand().equals("Confirm"))
	    	  {	    		  
	    		  String pdfFileName = this.fileNameTextField.getText();
	    		  String destinationFolder = this.destinationFolderTextField.getText();
	    		  log.debug("Exporting Contact to pdf file: "+pdfFileName +" in the folder: "+destinationFolder);
	    		 
	    		  //Perform some validations test
	    		  if(StringUtils.isEmpty(pdfFileName) || StringUtils.isEmpty(destinationFolder) ||
	    		     this.vcardRecordList == null || this.vcardRecordList.size() == 0)
	    		  {  
	    			  this.operationResultLabel.setText(INVALID_INPUT_ERROR_MSG);
	    			  this.operationResultLabel.setForeground(Color.red);
	    			  
	    			  log.trace("Invalid input for PDF contact export");
	    			  
	    		  }else {
	    			  
	    			  try {		
	    				     log.trace("ok, valid input for PDF contact export");
	    				  
	    				     ContactToPdfExporter contactToPdfExporter = new ContactToPdfExporter();			    					    				  
	    				     contactToPdfExporter.exportContact(pdfFileName,destinationFolder,this.vcardRecordList);			    				  
						      
			    			 this.operationResultLabel.setText(OPERATION_MSG_SUCCESS);
			    			 this.operationResultLabel.setForeground(Color.green);		    				
					
					   } catch (Exception ex) {
							 
							 this.operationResultLabel.setText(ex.getMessage());
							 this.operationResultLabel.setForeground(Color.red);
					   }   			  
	    		  }		    		  
	    	  }
	    	  
	    	  if (e.getActionCommand().equals("Close")) //close the popup
	          {
	    		  this.setVisible(false);
	    		  this.dispose();
	          }	    	  
	      }		
	}

	public JTextField getFileNameTextField() {
		return fileNameTextField;
	}

	public void setFileNameTextField(JTextField fileNameTextField) {
		this.fileNameTextField = fileNameTextField;
	}

	public JLabel getDestinationfolderLabel() {
		return destinationfolderLabel;
	}

	public void setDestinationfolderLabel(JLabel destinationfolderLabel) {
		this.destinationfolderLabel = destinationfolderLabel;
	}

	public JButton getChooseFolderButton() {
		return chooseFolderButton;
	}

	public void setChooseFolderButton(JButton chooseFolderButton) {
		this.chooseFolderButton = chooseFolderButton;
	}

	public JTextField getDestinationFolderTextField() {
		return destinationFolderTextField;
	}

	public void setDestinationFolderTextField(JTextField destinationFolderTextField) {
		this.destinationFolderTextField = destinationFolderTextField;
	}

	public List<VcardRecord> getVcardRecordList() {
		return vcardRecordList;
	}

	public JLabel getFileNameLabel() {
		return fileNameLabel;
	}

	public void setFileNameLabel(JLabel fileNameLabel) {
		this.fileNameLabel = fileNameLabel;
	}

	public JButton getConfirmButton() {
		return confirmButton;
	}

	public void setConfirmButton(JButton confirmButton) {
		this.confirmButton = confirmButton;
	}

	public JButton getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(JButton closeButton) {
		this.closeButton = closeButton;
	}

	public void setVcardRecordList(List<VcardRecord> vcardRecordList) {
		this.vcardRecordList = vcardRecordList;
	}

	public JLabel getOperationResultLabel() {
		return operationResultLabel;
	}

	public void setOperationResultLabel(JLabel operationResultLabel) {
		this.operationResultLabel = operationResultLabel;
	}

	

}
