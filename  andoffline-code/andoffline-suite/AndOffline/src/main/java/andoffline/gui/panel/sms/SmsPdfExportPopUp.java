package andoffline.gui.panel.sms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;

import net.miginfocom.swing.MigLayout;

/**
 *  Create a Pop-Up window to export the currently showed SMS in the JTREE to a pdf file
 *
 */
public class SmsPdfExportPopUp extends JDialog implements ActionListener, ItemListener{

	private static final long serialVersionUID = 1L;
	
    private SmsPdfExportCommandPanel smsPdfExportCommandPanel;
	
	private JTextField fileNameTextField;
	private JLabel destinationfolderLabel;
	private JLabel fileNameLabel;
	private JButton chooseFolderButton;
	private JTextField destinationFolderTextField;
	
	/* JCheckBox that work as a flag to indicates if the user want export to pdf file all the fields of an sms or only the base ones  */
	private JCheckBox exportOnlyBaseSmsFieldsCheckBox;
	
	/* Flag set by the exportOnlyBaseSmsFieldsCheckBox */
	private boolean exportOnlyBaseSmsFieldsFlag = false;	
		
	/**
	 * Constructor
	 * @param mainFrame The root main frame to close when the user press the Close button (ie the full application is closed)	 
	 * @param tree The JTree with the sms to be exported
	 */
	public SmsPdfExportPopUp(JFrame mainFrame, JTree tree) {
		
		this.setLayout(new MigLayout("wrap 3")); //say that we want 3 columns	        
		
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Options"));
		
		this.fileNameTextField = new JTextField();
		
		//-------- Create a sub panel with the command buttons confirm/close and their listener -----
		this.smsPdfExportCommandPanel = new SmsPdfExportCommandPanel(this,tree);
		//-------------------------------------------------------------------------------------------
		
		this.fileNameLabel = new JLabel("File Name: ");
		this.destinationfolderLabel = new JLabel("Destination Folder:");
		this.destinationFolderTextField = new JTextField();
		this.chooseFolderButton = new JButton("Choose");
		this.chooseFolderButton.addActionListener(this);
		
		 //---- The flag checkbox used by the user to indicates if he want export only the base sms data or also their technical details
		this.exportOnlyBaseSmsFieldsCheckBox = new JCheckBox("Export only base fields");
		this.exportOnlyBaseSmsFieldsCheckBox.addItemListener(this);
		
		//---- setup the layout
		this.add(fileNameLabel);
		this.add(fileNameTextField,"span 2,width 420,growx");
		
		this.add(destinationfolderLabel);
		this.add(destinationFolderTextField,"growx,width 330");
		this.add(chooseFolderButton,"width 100");
		
		this.add(exportOnlyBaseSmsFieldsCheckBox,"span3, gap right 300");
		
		this.add(smsPdfExportCommandPanel,"align center,span 3");
  
        //set the dialog as a modal window
        this.setModalityType(ModalityType.APPLICATION_MODAL);

        this.setTitle("PDF Export Options");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(600,250);  			
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
	            	 
	    			  this.destinationFolderTextField.setText(folderChosen);
	              }	  
	          } 	  
	     }
	}
	
	/**
     * Manage the events on the details checkbox: if checked the user want export all the sms fields
     */
	public void itemStateChanged(ItemEvent e) {
		
		 Object source = e.getItemSelectable();
	        
	        //Get the source of the event (only event on exportDetailsCheckBox are accepted)
	        if (source == exportOnlyBaseSmsFieldsCheckBox) {	            	
		       
		        if (e.getStateChange() == ItemEvent.DESELECTED) { //unchecked		        			        		
		        	smsPdfExportCommandPanel.setExportOnlyBaseSmsFieldsFlag(false);
		        }
		        
		        if (e.getStateChange() == ItemEvent.SELECTED) { //checked		        			        	
		        	smsPdfExportCommandPanel.setExportOnlyBaseSmsFieldsFlag(true);
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


	public boolean isExportOnlyBaseSmsFieldsFlag() {
		return exportOnlyBaseSmsFieldsFlag;
	}


	public void setExportOnlyBaseSmsFieldsFlag(boolean exportOnlyBaseSmsFieldsFlag) {
		this.exportOnlyBaseSmsFieldsFlag = exportOnlyBaseSmsFieldsFlag;
	}


	
}




