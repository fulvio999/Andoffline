package andoffline.gui.panel.job;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

/**
 * Open a popUP that show some secondary fields of the selected job that are not shown in the JTable with all the jobs.
 * The fields are:
 * "description", "last-execution-date", "executor-msisdn" 
 *
 */
public class JobDetailsPopUp extends JDialog implements ActionListener {

	 private JButton closeButton;	
		
	 private JLabel jobDescriptionLabel;
	 private JLabel jobLastExecutionDateLabel;
	 private JLabel jobExecutorMsisdnLabel;
	
	/**
	 * Constructor
	 */
	public JobDetailsPopUp(String jobDescription, String jobLastExecutionDate,String jobExecutorMsisdn) {
		
		super.rootPane.setBorder(BorderFactory.createTitledBorder("Job Details"));			
		buildPopUp(jobDescription,jobLastExecutionDate,jobExecutorMsisdn);	
		 this.setVisible(true);	
	}
	
	/**
	 * Build the popup
	 */
	private void buildPopUp(String jobDescription, String jobLastExecutionDate, String jobExecutorMsisd)
	{		
	   this.setLayout(new MigLayout("wrap 1")); //we want 1 columns	
		
	   jobDescriptionLabel = new JLabel("Description: "+jobDescription);
	   jobLastExecutionDateLabel = new JLabel("Last Execution Date: "+jobLastExecutionDate);
	   jobExecutorMsisdnLabel = new JLabel("Executor MSISDN: "+jobExecutorMsisd);
	   
	   closeButton = new JButton("Close");	        
	   closeButton.addActionListener(this);	
	   
	   this.add(jobDescriptionLabel);
	   this.add(jobLastExecutionDateLabel);
	   this.add(jobExecutorMsisdnLabel);
	   this.add(closeButton,"width 100,align center");
	   
	   //set the dialog as a modal window
       this.setModalityType(ModalityType.APPLICATION_MODAL);
       this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);  
      
	   this.setSize(600, 200);
	   this.setTitle("Selected Job Details");
	}

	
	public void actionPerformed(ActionEvent e) 
	{					   
		if (e.getSource() instanceof JButton)  
		{		     
		   if (e.getActionCommand().equalsIgnoreCase("Close")){		    		  
		   	  this.setVisible(false);
			  this.dispose();
		   }
	    }	    
    }

	
	public JLabel getJobDescriptionLabel() {
		return jobDescriptionLabel;
	}

	public void setJobDescriptionLabel(JLabel jobDescriptionLabel) {
		this.jobDescriptionLabel = jobDescriptionLabel;
	}

	public JLabel getJobLastExecutionDateLabel() {
		return jobLastExecutionDateLabel;
	}

	public void setJobLastExecutionDateLabel(JLabel jobLastExecutionDateLabel) {
		this.jobLastExecutionDateLabel = jobLastExecutionDateLabel;
	}

	public JLabel getJobExecutorMsisdnLabel() {
		return jobExecutorMsisdnLabel;
	}

	public void setJobExecutorMsisdnLabel(JLabel jobExecutorMsisdnLabel) {
		this.jobExecutorMsisdnLabel = jobExecutorMsisdnLabel;
	}

}