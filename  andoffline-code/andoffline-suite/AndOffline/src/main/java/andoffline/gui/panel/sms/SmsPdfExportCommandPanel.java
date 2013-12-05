
package andoffline.gui.panel.sms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import andoffline.exporter.pdf.SmsToPdfExporter;


/**
 * Create a panel with the command buttons (confirm and close  button) to be inserted in the Sms popup for the pdf export
 *
 */
public class SmsPdfExportCommandPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(SmsPdfExportCommandPanel.class);
	
	private static String OPERATION_MSG_SUCCESS = "Operation Executed Successfully !";
	private static String INVALID_INPUT_ERROR_MSG = "Invalid Input !";
	
	/* The pop-up option that own this command panel */
	private SmsPdfExportPopUp smsPdfExportPopUp;
	
	/* A label that show the operation result (ie pdf export) */
	private JLabel operationResultLabel;
	
	/* The JTree with the content to be exported to pdf */
	private JTree treeToParse;
	
	/* Flag used to indicates if the user want or not export to PDF file all the sms fields (technical ones + base ones) (if true means export all)
	 * The default export ALL 
	 * */
	private boolean exportOnlyBaseSmsFieldsFlag = false;
	
	
	/**
	 * Constructor
	 * @param SmsPdfExportPopUp the pop-up window that own this sub-panel
	 * @param tree The JTree with the content to be exported in pdf (an empty tree is passed in case of the data to export are contacts extracted from a vcf file)	
	 *
	 */
	public SmsPdfExportCommandPanel(SmsPdfExportPopUp p, JTree tree) { 		
		
		this.smsPdfExportPopUp = p;		
		this.treeToParse = tree;
		
		this.setLayout(new MigLayout("wrap 4")); //say that we want 4 columns		
		
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(this);      
        
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(this);        
       
        operationResultLabel = new JLabel("");       
        
        this.add(confirmButton,"width 100,gapleft 100");
        this.add(closeButton,"width 100,gapright 100,wrap");
        
        this.add(operationResultLabel, "align center, span 4");
	}


    /**
	 * Manage the actions registered with the buttons
	 */
	  public void actionPerformed(ActionEvent e) {			
			  
		      if (e.getSource() instanceof JButton)  
		      {
		    	  //The user confirm the pdf export
		    	  if (e.getActionCommand().equals("Confirm")){
		    		  
		    		  String pdfFileName = this.smsPdfExportPopUp.getFileNameTextField().getText();
		    		  String destinationFolder = this.smsPdfExportPopUp.getDestinationFolderTextField().getText();
		    		  
		    		  //Perform some validations test
		    		  if(StringUtils.isEmpty(pdfFileName) || StringUtils.isEmpty(destinationFolder))
		    		  {
		    			  //show an error message
		    			  this.getOperationResultLabel().setText(INVALID_INPUT_ERROR_MSG);
		    			  this.getOperationResultLabel().setForeground(Color.red);
		    			  
		    		  }else {
		    			  
		    			  try {		    				 
		    				  
		    				  //true if user has made a filtered search that has returned an empty tree
		    				  if(this.treeToParse == null){		    					  
		    					 
				    			  this.getOperationResultLabel().setText(INVALID_INPUT_ERROR_MSG);
				    			  this.getOperationResultLabel().setForeground(Color.red);
				    			  
		    				  }else {
		    							    				  
		    					  //export the JTree content (ie the sms) to the chosen pdf file
			    				  SmsToPdfExporter pdfExporter = new SmsToPdfExporter();			    					    				  
			    				  pdfExporter.exportSms(pdfFileName,destinationFolder,this.treeToParse, this.exportOnlyBaseSmsFieldsFlag);			    				  
							      
				    			  this.operationResultLabel.setText(OPERATION_MSG_SUCCESS);
				    			  this.getOperationResultLabel().setForeground(Color.green);
		    				  }
						
						   } catch (Exception ex) {
								 
							  this.operationResultLabel.setText(ex.getMessage());
							  this.getOperationResultLabel().setForeground(Color.red);
						   }			  
		    		  }		    		  
		    	  }
		    	  
		    	  if (e.getActionCommand().equals("Close")) 
		          {
		    		  this.smsPdfExportPopUp.setVisible(false);
		    		  this.smsPdfExportPopUp.dispose();
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


		public boolean isExportOnlyBaseSmsFieldsFlag() {
			return exportOnlyBaseSmsFieldsFlag;
		}


		public void setExportOnlyBaseSmsFieldsFlag(boolean exportOnlyBaseSmsFieldsFlag) {
			this.exportOnlyBaseSmsFieldsFlag = exportOnlyBaseSmsFieldsFlag;
		}


		public SmsPdfExportPopUp getSmsPdfExportPopUp() {
			return smsPdfExportPopUp;
		}


		public void setSmsPdfExportPopUp(SmsPdfExportPopUp smsPdfExportPopUp) {
			this.smsPdfExportPopUp = smsPdfExportPopUp;
		}


}
