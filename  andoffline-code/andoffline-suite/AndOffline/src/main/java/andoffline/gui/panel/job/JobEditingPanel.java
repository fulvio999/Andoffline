
package andoffline.gui.panel.job;

import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

/**
 * Panel that show the info of the selected job to edit them or insert a new one.
 *
 */
public class JobEditingPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JLabel headerNoteLabel;
	
	private JLabel jobNameLabel;
	private JLabel jobInterpreterLabel;
	private JLabel jobInterpreterArgumentLabel;
	private JLabel jobScriptLabel;
	private JLabel jobScriptArgumentLabel;
	private JLabel jobDescriptionLabel;
	
	// NOTE: the job field "lastExecutionDateColumn" is updated each time the job is executed
	
	private JTextField jobNameTextField;
	private JTextField jobInterpreterTextField;
	private JTextField jobInterpreterArguemtTextField;  //the argument for the interpreter, eg: -jar
	private JTextField jobScriptTextField;
	private JTextField jobScriptArgumentTextField;  //the arguments for the script
	private JTextField jobDescriptionTextField;
	
	private JButton browseInterpreterButton;
	private JButton browseScriptButton;
	
	/* Update the currently loaded job in the editing form */
	private JButton updateButton;
	
	/* Insert as new job using the currently inserted data in the editing form */
	private JButton newJobButton;
	
	/**
	 * Constructor
	 */
	public JobEditingPanel(ActionListener jobManagerActionListener) 
	{		
		this.setBorder(BorderFactory.createTitledBorder("Edit/Add Job"));
        this.setLayout(new MigLayout("wrap 3")); // we want 3 columns
        
        headerNoteLabel = new JLabel("NOTE: Leave 'Interpreter' blank to run a '.bat' script on Windows OS");
        
        jobNameLabel = new JLabel("* Name:");
        //blank if the script is a .bat file on Windows (set to /bin/bash for .sh file on Linux)
        jobInterpreterLabel = new JLabel("Interpreter:");
        jobInterpreterArgumentLabel = new JLabel("Interpreter Arg.:");
        jobScriptLabel = new JLabel("* Script:");
        jobScriptArgumentLabel = new JLabel("Script Argument:");
        jobDescriptionLabel = new JLabel("Description:");
        
        browseInterpreterButton = new JButton("Browse");
        browseInterpreterButton.setActionCommand("Browse Interpreter");
        browseInterpreterButton.addActionListener(jobManagerActionListener);
        
        browseScriptButton = new JButton("Browse");
        browseScriptButton.setActionCommand("Browse Script");
        browseScriptButton.addActionListener(jobManagerActionListener);
        
        updateButton = new JButton("Update");
        updateButton.addActionListener(jobManagerActionListener);       
        
        newJobButton = new JButton("New Job");
        newJobButton.addActionListener(jobManagerActionListener);        
        
        jobNameTextField = new JTextField();
        jobInterpreterTextField = new JTextField();
        jobInterpreterArguemtTextField = new JTextField();
        jobScriptTextField = new JTextField();
        jobScriptArgumentTextField = new JTextField();        
        jobDescriptionTextField = new JTextField();       
        
        this.add(headerNoteLabel,"span 3,align center");
        this.add(jobNameLabel);
        this.add(jobNameTextField,"width 550,wrap");
        
        this.add(jobInterpreterLabel);
        this.add(jobInterpreterTextField,"width 550,split");
        this.add(browseInterpreterButton,"width 100,wrap");
        
        this.add(jobInterpreterArgumentLabel);
        this.add(jobInterpreterArguemtTextField,"width 550,wrap");
        
        this.add(jobScriptLabel);
        this.add(jobScriptTextField,"width 550,split");
        this.add(browseScriptButton,"width 100,wrap");
        
        this.add(jobScriptArgumentLabel);
        this.add(jobScriptArgumentTextField,"width 550,wrap");
        
        this.add(jobDescriptionLabel);     
        this.add(jobDescriptionTextField,"width 550,wrap");	
        
        this.add(new JLabel("* field required"),"span 1"); 
        this.add(newJobButton,"gapx 600,width 100");
        this.add(updateButton,"width 100,wrap");        
	}

	
	public JTextField getJobNameTextField() {
		return jobNameTextField;
	}

	public void setJobNameTextField(JTextField jobNameTextField) {
		this.jobNameTextField = jobNameTextField;
	}

	public JTextField getJobInterpreterTextField() {
		return jobInterpreterTextField;
	}

	public void setJobInterpreterTextField(JTextField jobInterpreterTextField) {
		this.jobInterpreterTextField = jobInterpreterTextField;
	}

	public JTextField getJobDescriptionTextField() {
		return jobDescriptionTextField;
	}

	public void setJobDescriptionTextField(JTextField jobDescriptionTextField) {
		this.jobDescriptionTextField = jobDescriptionTextField;
	}

	public JButton getUpdateButton() {
		return updateButton;
	}

	public void setUpdateButton(JButton updateButton) {
		this.updateButton = updateButton;
	}

	public JButton getNewJobButton() {
		return newJobButton;
	}

	public void setNewJobButton(JButton newJobButton) {
		this.newJobButton = newJobButton;
	}

	public JTextField getJobScriptTextField() {
		return jobScriptTextField;
	}

	public void setJobScriptTextField(JTextField jobScriptTextField) {
		this.jobScriptTextField = jobScriptTextField;
	}


	public JTextField getJobScriptArgumentTextField() {
		return jobScriptArgumentTextField;
	}


	public void setJobScriptArgumentTextField(JTextField jobScriptArgumentTextField) {
		this.jobScriptArgumentTextField = jobScriptArgumentTextField;
	}


	public JTextField getJobInterpreterArguemtTextField() {
		return jobInterpreterArguemtTextField;
	}


	public void setJobInterpreterArguemtTextField(
			JTextField jobInterpreterArguemtTextField) {
		this.jobInterpreterArguemtTextField = jobInterpreterArguemtTextField;
	}


}
