package andoffline.gui.panel.job;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

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

import andoffline.exporter.pdf.JobToPdfExporter;
import andoffline.integration.database.dto.JobBean;


/* 
 * Create a pupup with the options to export the job list to pdf
 */
public class JobPdfExportPopUp extends JDialog implements ActionListener {
	
	private static final Logger log = Logger.getLogger(JobPdfExportPopUp.class);

	private static final long serialVersionUID = 1L;
	
	private static String OPERATION_MSG_SUCCESS = "Operation Executed Successfully !";
	private static String INVALID_INPUT_ERROR_MSG = "Invalid Input:";
	
	private JLabel fileNameLabel;
	
	private JTextField fileNameTextField;
	private JLabel destinationfolderLabel;
	private JButton chooseFolderButton;
	private JButton confirmButton;
	private JButton closeButton;
	private JTextField destinationFolderTextField;
	
	private ArrayList<JobBean> jobBeanList;
	
	/* A label that show the operation result (ie pdf export) */
	private JLabel operationResultLabel;
	
	/**
	 * Constructor
	 * @param mainFrame The root main frame to close when the user press the Close button (ie the full application is closed)	
	 * @param tree The JTree with the content to be exported (an empty tree is passed in case of the data to export are contacts extracted from a vcf file)
	 */
	public JobPdfExportPopUp(JFrame mainFrame, ArrayList<JobBean> jobBeanList) {
		
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Options"));
		
		this.jobBeanList = jobBeanList; 
        this.fileNameTextField = new JTextField();				
		
        this.operationResultLabel = new JLabel("");
        
		destinationfolderLabel = new JLabel("Destination Folder:");
		destinationFolderTextField = new JTextField();
		chooseFolderButton = new JButton("Choose");
		chooseFolderButton.addActionListener(this);
		
		this.setLayout(new MigLayout("wrap 3")); //say that we want 3 columns	
		
		this.fileNameLabel = new JLabel("File Name: ");
		this.destinationfolderLabel = new JLabel("Destination Folder:");
		this.destinationFolderTextField = new JTextField();
		this.chooseFolderButton = new JButton("Choose");
		this.chooseFolderButton.addActionListener(this);
		
		this.confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(this);
		
		this.closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		
		// setup the layout
		this.add(fileNameLabel);
		this.add(fileNameTextField,"span 2,width 420,growx");
		
		this.add(destinationfolderLabel);
		this.add(destinationFolderTextField,"growx,width 330");
		this.add(chooseFolderButton,"width 100");
		
		this.add(new JLabel("")); //place-holder
		this.add(closeButton,"width 100,split 2");
		this.add(confirmButton,"width 100");
		this.add(new JLabel(""));
		
		this.add(operationResultLabel, "align center, span 3");
  
        //set the dialog as a modal window
        this.setModalityType(ModalityType.APPLICATION_MODAL);

        this.setTitle("Job PDF Export");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(600,250);  			
        this.setVisible(true);	
	}

	
	/**
	 * Manage the actions on the buttons
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
	    			  this.destinationFolderTextField.setText(destinationFolderChosen.getAbsolutePath());	    			  
	              }	  
	          }	
	    	  
	    	  if (e.getActionCommand().equals("Confirm")) 
	          {
	    		  String pdfFileName = this.fileNameTextField.getText();
	    		  String destinationFolder = this.destinationFolderTextField.getText();
	    		  
	    		  //Perform a validations test
	    		  if(StringUtils.isEmpty(pdfFileName) || StringUtils.isEmpty(destinationFolder))
	    		  {	    			 
	    			  this.operationResultLabel.setText(INVALID_INPUT_ERROR_MSG+ "missing parameter");
	    			  this.operationResultLabel.setForeground(Color.red);
	    			  
	    		  }else{
	    			  
	    			  try{				  
	    				  if(this.jobBeanList == null)
	    				  { 					  
			    			 this.operationResultLabel.setText(INVALID_INPUT_ERROR_MSG+ " job list is empty");
			    			 this.operationResultLabel.setForeground(Color.red);
			    			  
	    				  }else{	    							    				  
	    					 JobToPdfExporter jobToPdfExporter = new JobToPdfExporter();
	    					 jobToPdfExporter.exportJob(fileNameTextField.getText(),destinationFolder,this.jobBeanList);
	    					  
			    			 this.operationResultLabel.setText(OPERATION_MSG_SUCCESS);
			    			 this.operationResultLabel.setForeground(Color.green);
	    				  }
	    		  
	                   }catch (Exception ex){
						  log.fatal("Error exporting jobs to PDF, cause: "+e.toString());
						  this.operationResultLabel.setText(ex.getMessage());
						  this.operationResultLabel.setForeground(Color.red);
					   }
	    		  } 	    	  
	          }	
	    	  
	    	  if (e.getActionCommand().equals("Close")) 
	          {	    		 
	    		  this.setVisible(false);
	    		  this.dispose();
	          }
	    }	

	}
	
}	
