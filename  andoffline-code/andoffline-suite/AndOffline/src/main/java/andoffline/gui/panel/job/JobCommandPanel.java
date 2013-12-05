
package andoffline.gui.panel.job;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/**
 * JPanel with the buttons to manage the DB stored Jobs (edit, update....) 
 *
 */
public class JobCommandPanel extends JPanel {	
	
	private static final long serialVersionUID = 1L;
	
	private JButton editSelectedJobButton;		
	private JButton showJobDetailsButton;
	private JButton deleteJobButton;
	private JButton exportJobToPdfButton;
	
	private JButton loadJobButton;
	
	/* show some messages to the user about the executed operations */
	private JLabel operationResultLabel;
	
	/**
	 * Constructor
	 * @param actionListener The listener that manage the events on the buttons of the panel
	 */
	public JobCommandPanel(JobManagerTab actionListener) {
		
		this.setBorder(BorderFactory.createTitledBorder("Command"));
        this.setLayout(new MigLayout("wrap 5")); // we want 4 columns
        
		this.editSelectedJobButton = new JButton("Edit Selected");
		editSelectedJobButton.addActionListener(actionListener);
		
		this.showJobDetailsButton = new JButton("Show Job Details");
		showJobDetailsButton.addActionListener(actionListener);
		
		this.deleteJobButton = new JButton("Delete Selected");
		deleteJobButton.addActionListener(actionListener);
		
		this.loadJobButton= new JButton("Load Jobs/Refresh");
		loadJobButton.addActionListener(actionListener);
		
		this.exportJobToPdfButton = new JButton("Export to PDF");
		exportJobToPdfButton.addActionListener(actionListener);
		
		this.operationResultLabel = new JLabel("");
		
		this.add(loadJobButton,"width 100,gapright 30px");
		this.add(editSelectedJobButton,"gapleft 30,width 100");		
		this.add(showJobDetailsButton,"width 100");
		this.add(deleteJobButton,"gapleft 80,width 100");	
		this.add(exportJobToPdfButton,"gapleft 50");
		
		this.add(operationResultLabel,"span 6,align center");
	}

	public JButton getDeleteJobButton() {
		return deleteJobButton;
	}

	public void setDeleteJobButton(JButton deleteJobButton) {
		this.deleteJobButton = deleteJobButton;
	}

    public JButton getEditSelectedJobButton() {
		return editSelectedJobButton;
	}

	public void setEditSelectedJobButton(JButton editSelectedJobButton) {
		this.editSelectedJobButton = editSelectedJobButton;
	}

	public JButton getLoadJobButton() {
		return loadJobButton;
	}

	public void setLoadJobButton(JButton loadJobButton) {
		this.loadJobButton = loadJobButton;
	}

	public JLabel getOperationResultLabel() {
		return operationResultLabel;
	}


	public void setOperationResultLabel(JLabel operationResultLabel) {
		this.operationResultLabel = operationResultLabel;
	}

 }
