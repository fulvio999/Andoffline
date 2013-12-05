
package andoffline.gui.panel.call;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

import org.apache.log4j.Logger;

import andoffline.exporter.pdf.CallLogToPdfExporter;
import andoffline.gui.menu.configuration.database.DatabaseConfigEditorPopUp;


/**
 * Create the panel with the confirm and close button to be used in the PDF export PopUp window
 *
 */
public class CallPdfExportPopUpCommandPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(CallPdfExportPopUpCommandPanel.class);
	
	private static String OPERATION_MSG_SUCCESS = "Operation Executed Successfully !";
	private static String INVALID_INPUT_ERROR_MSG = "Invalid Input !";
	
	private CallPdfExportPopUp callPdfExportPopUp;
	
	/* A label that show the operation result (ie pdf export) */
	private JLabel operationResultLabel;
	
	/* The JTree with the content to be exported to pdf */
	private JTree treeToParse;
	
	/**
	 * Constructor
	 * @param SmsAndCallPdfExportPopUp the popup window with the export options (filename, destination folder) that own this panel
	 * @param tree The JTree with the Calls to be exported in pdf (an empty tree is passed in case of the data to export are contacts extracted from a vcf file)	
	 * @param exportTechnicalDetails  A flag to indicates if the user want export to pdf file all the fields of an sms or only the base ones	 
	 */
	public CallPdfExportPopUpCommandPanel(CallPdfExportPopUp p, JTree tree) {  
		
		// The popup where extract the pdf export options (ie filename, destination folder)
		this.callPdfExportPopUp = p;		
		this.treeToParse = tree;			
		this.setLayout(new GridBagLayout());		
		
		// Close button that close the pop up window
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(this);
      
        // Confirm button that when clicked start pdf export
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(this);
        
        //The pdf export operation result Label
        operationResultLabel = new JLabel("");       
        
        //TODO convert to MigLayout
        
        // Setup the layout
        GridBagConstraints c = new GridBagConstraints();
        
        //--- 1st row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        this.add(new JLabel(""), c); //placeholder
         
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;  //The column number (horizontal axis) 
        c.gridy = 0;  //The row number (vertical axis)
        c.weightx = 0.5;	 
        c.weighty = 0;
        c.gridwidth = 1;
        this.add(confirmButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridwidth = 1;
        this.add(closeButton, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridwidth = 1;
        this.add(new JLabel(""), c); //placeholder
        
        //--- 2nd row (Operation result label)
        c.fill = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0.5;
        c.gridwidth = 4;
        this.add(operationResultLabel, c);
        
	}

		/**
		 * Manage the actions registered with the buttons
		 */
		public void actionPerformed(ActionEvent e) {			
			  
		      if (e.getSource() instanceof JButton)  
		      {		    	 
		    	  if (e.getActionCommand().equals("Confirm"))
		    	  {		    		  
		    		  String pdfFileName = this.callPdfExportPopUp.getFileNameTextField().getText();
		    		  String destinationFolder = this.callPdfExportPopUp.getDestinationFolderTextField().getText();
		    		  
		    		  //Perform some validations test
		    		  if(pdfFileName.equalsIgnoreCase("") || pdfFileName == null || destinationFolder.equalsIgnoreCase("") || destinationFolder == null)
		    		  {		    			 
		    			  this.getOperationResultLabel().setText(INVALID_INPUT_ERROR_MSG);
		    			  this.getOperationResultLabel().setForeground(Color.red);
		    			  
		    		  }else{
		    			  
		    			  try {		    				 
		    				  
		    				  //true if user has made a filtered search that has returned an empty tree
		    				  if(this.treeToParse == null) {		    	
		    					  
		    					  log.fatal("Error exporting to PDF the Call tree: is empty !");		    					  
				    			  this.getOperationResultLabel().setText(INVALID_INPUT_ERROR_MSG);
				    			  this.getOperationResultLabel().setForeground(Color.red);
				    			  
		    				  }else{		    							    				  
		    					  //export the JTree content (log calls) to the chosen pdf file
		    					  CallLogToPdfExporter callLogToPdfExporter = new CallLogToPdfExporter();    				  
		    					  callLogToPdfExporter.exportLogCall(pdfFileName,destinationFolder,this.treeToParse);			    				  
							     
				    			  this.operationResultLabel.setText(OPERATION_MSG_SUCCESS);
				    			  this.getOperationResultLabel().setForeground(Color.green);
		    				  }
						
						   }catch (Exception ex){
								 
							   log.fatal("Error exporting to PDF the Call tree, cause: "+ex.getMessage());		   
							   this.operationResultLabel.setText(ex.getMessage());
							   this.getOperationResultLabel().setForeground(Color.red);
						   }	    			  
		    			 
		    		   }		    		  
		    	  }
		    	  
		    	  if (e.getActionCommand().equals("Close")) 
		          {
		    		  this.callPdfExportPopUp.setVisible(false);
		    		  this.callPdfExportPopUp.dispose();
		          }
		      }
		
	}


		public JLabel getOperationResultLabel() {
			return operationResultLabel;
		}


		public void setOperationResultLabel(JLabel operationResultLabel) {
			this.operationResultLabel = operationResultLabel;
		}


		public JTree getTreeToParse() {
			return treeToParse;
		}


		public void setTreeToParse(JTree treeToParse) {
			this.treeToParse = treeToParse;
		}

		public CallPdfExportPopUp getCallPdfExportPopUp() {
			return callPdfExportPopUp;
		}


		public void setCallPdfExportPopUp(CallPdfExportPopUp callPdfExportPopUp) {
			this.callPdfExportPopUp = callPdfExportPopUp;
		}


}
