package andoffline.gui.panel.call;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;

/**
 *  This class create a Pop-Up window where the user must insert some option about the destination pdf file
 *  (ie file name and destination folder) where export the sms or log calls
 *
 */
public class CallPdfExportPopUp extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;	
	
    private CallPdfExportPopUpCommandPanel callPdfExportCommandPanel;
	
	private JTextField fileNameTextField;
	private JLabel destinationfolderLabel;
	private JButton chooseFolderButton;
	private JTextField destinationFolderTextField;
		
	/**
	 * Constructor
	 * @param mainFrame The root main frame to close when the user press the Close button (ie the full application is closed)	
	 * @param tree The JTree with the content to be exported (an empty tree is passed in case of the data to export are contacts extracted from a vcf file)
	 */
	public CallPdfExportPopUp(JFrame mainFrame, JTree tree) {
		
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Options"));
	
		this.setLayout(new GridBagLayout());		
		
		this.fileNameTextField = new JTextField();
		
		//---- Create a sub panel with the command button confirm/close ---------
		this.callPdfExportCommandPanel = new CallPdfExportPopUpCommandPanel(this,tree);
		//-----------------------------------------------------------------------
		
		destinationfolderLabel = new JLabel("Destination Folder:");
		destinationFolderTextField = new JTextField();
		chooseFolderButton = new JButton("Choose");
		chooseFolderButton.addActionListener(this);
		       
		//TODO convert to MigLayout
		
        //---- Setup the layout
        GridBagConstraints c = new GridBagConstraints();
	       
        //--- 1st row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;  //The column number (horizontal axis) 
        c.gridy = 0;  //The row number (vertical axis)
        c.weightx = 0;	 
        c.weighty = 0;
        this.add(new JLabel("File Name: "), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        this.add(fileNameTextField, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        this.add(new JLabel(""), c); //placeholder
        
        //--- 2nd row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1; //the number of cell in a row to fill
        this.add(destinationfolderLabel,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1; //the number of cell in a row to fill
        this.add(destinationFolderTextField,c); 
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1; //the number of cell in a row to fill
        this.add(chooseFolderButton,c);         
        
        //---- 3rd row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 3; //the number of cell in a row to fill
        this.add(new JLabel(""),c); //placeholder   
        
        //---- 4th row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1; //the number of cell in a row to fill
        this.add(new JLabel(""),c); //placeholder        
        
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 3; //the number of cell in a row to fill
        this.add(callPdfExportCommandPanel,c); /* A sub panel with the command button for the pdf export */
  
        //set the dialog as a modal window
        this.setModalityType(ModalityType.APPLICATION_MODAL);

        this.setTitle("PDF Export Options");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(600,200);  			
        this.setVisible(true);		
	}

	
	/**
	 * Manage the actions registered with the buttons
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
	    			  
	            	  //update the input text filed with the chosen folder
	    			  this.destinationFolderTextField.setText(folderChosen);
	              }	  
	          }	  
	    	  
	      }
	}
	

	public JTextField getFileNameTextField() {
		return fileNameTextField;
	}

	public void setFileNameTextField(JTextField fileNameTextField) {
		this.fileNameTextField = fileNameTextField;
	}


	public JTextField getDestinationFolderTextField() {
		return destinationFolderTextField;
	}


	public void setDestinationFolderTextField(JTextField destinationFolderTextField) {
		this.destinationFolderTextField = destinationFolderTextField;
	}
	
}




