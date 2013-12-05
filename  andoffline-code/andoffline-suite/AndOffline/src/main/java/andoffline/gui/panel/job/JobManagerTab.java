package andoffline.gui.panel.job;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import andoffline.integration.database.DatabaseConfigurationManager;
import andoffline.integration.database.dao.JobDao;
import andoffline.integration.database.dto.JobBean;

/**
 * Create the "Job Manager" tab panel where the user can manage (edit,insert,delete) the job stored on the chosen DB (ie: Sqlite or Mysql)
 */
public class JobManagerTab extends JPanel implements ActionListener, ListSelectionListener {
	
    private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(JobManagerTab.class);

	// The Container frame that the "close" JButton of the command panel must close
    private JFrame mainFrame;
    
    private JTable jobsTable;
    private JButton closeButton;   
        
    private JobCommandPanel jobCommandPanel;    
    private JobEditingPanel jobEditingPanel;    
    private JScrollPane jobsTableScrollPane;
    
    private JobDao jobDao = new JobDao();
	
	/**
	 * Constructor
	 * @param mainFrame
	 */
	public JobManagerTab(JFrame mainFrame) {
			
			this.setBorder(BorderFactory.createTitledBorder("Job Executable"));
	        this.setLayout(new MigLayout("wrap 1")); // we want 1 columns
			
			this.mainFrame = mainFrame;
			
			this.jobCommandPanel = new JobCommandPanel(this);			
			this.jobEditingPanel = new JobEditingPanel(this);
			
			this.closeButton = new JButton("Close");
			this.closeButton.addActionListener(this);
		    			
			this.jobsTable = new JTable(); //set default data model
			this.jobsTable.setModel(new JobTableModel());
			this.jobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//to clean edit panel fields each time the user select a different job  
			this.jobsTable.getSelectionModel().addListSelectionListener(this); 
			
			TableColumn firstCol = this.jobsTable.getColumnModel().getColumn(0);		
			firstCol.setPreferredWidth(2);				
			this.jobsTableScrollPane = new JScrollPane(jobsTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		 	       
			this.add(jobsTableScrollPane,"span 1,height 300,width 960,growy");			
			this.add(jobCommandPanel,"span 1,align center,growx");			
			this.add(jobEditingPanel,"span 2,align center,growx");			
			this.add(closeButton,"gapleft 850px,width 100");			
	 }
	
	/**
	 * Utility method that blank all the filed in the edit job panel 
	 */
	private void cleanEditPanelField()
	{		
		this.jobEditingPanel.getJobNameTextField().setText("");
        this.jobEditingPanel.getJobInterpreterTextField().setText("");
        this.jobEditingPanel.getJobInterpreterArguemtTextField().setText("");
        this.jobEditingPanel.getJobScriptTextField().setText("");
        this.jobEditingPanel.getJobScriptArgumentTextField().setText("");
        this.jobEditingPanel.getJobDescriptionTextField().setText("");		
	}
	
	
	/**
	 * Manage the click action on the buttons placed in the sub-panel named "jobEditingCommandPanel" and "JobCommandPanel"
	 */
	public void actionPerformed(ActionEvent e) {
				 
		    final JFileChooser scriptAndInterpreterFileChooser = new JFileChooser();
		
		    /* The configured target DB read from database.properties file */
		    String configuredDbType = null;
		
		    try {
		    	configuredDbType = DatabaseConfigurationManager.getConfiguredDatabaseType();
		    	log.info("Configured target database: "+configuredDbType);
		    	
		    }catch (IOException ex) {
		    	log.fatal("NO target database configured in database.properties !"+ex.getMessage());		    	
		    }			
		
		    if (e.getSource() instanceof JButton)  
		    {	
		    	//clean previous displayed message
		    	jobCommandPanel.getOperationResultLabel().setText("");	    	
		        
		    	/*
		    	 * Load the Database stored job(s)
		    	 */
		        if (e.getActionCommand().equals("Load Jobs/Refresh")) 
		        {		        		
		        	try{    		
			        	ArrayList<JobBean> jobList = jobDao.loadAllJobs(configuredDbType);
			        	log.info("Loaded: "+jobList.size()+" jobs from database");  
			        			        	
			        	JobTableModel tm = (JobTableModel) jobsTable.getModel();		      
			        	tm.setJobTableBeanList(jobList);
			        	tm.fireTableDataChanged();	
			        	
			        	this.cleanEditPanelField();
					    this.setOperationLabel(true,"loaded "+jobList.size()+" jobs");
	
		        	}catch (Exception ex) {
		        	   log.fatal("Error loading Jobs from DB, configuration is ok ?, cause: "+ex.getMessage());
		        	   this.setOperationLabel(false,"Error Loading Jobs, database configuration is ok ? See log for details");
					}
		        }
		        
		        /*
		         *  Get the selected job from the table and fill the editing form
		         */
		        if (e.getActionCommand().equals("Edit Selected")) 
		        {	        	 
		        	if(jobsTable.getSelectedRow() !=-1)
		        	{ 	  
		               JobBean selectedJobBean = this.getSelectedJobBean();
		               log.debug("Selected job for Editing, id: "+selectedJobBean.getId()+" Command: "+selectedJobBean.getScript()+" Interpreter: "+selectedJobBean.getInterpreter()+ " Description: "+selectedJobBean.getDescription());
			    	   
			    	   //fill the JobDetails panel with the selected job
		               this.jobEditingPanel.getJobNameTextField().setText(selectedJobBean.getName());
		               this.jobEditingPanel.getJobInterpreterTextField().setText(selectedJobBean.getInterpreter());
		               this.jobEditingPanel.getJobInterpreterArguemtTextField().setText(selectedJobBean.getInterpreterArgument());
		               this.jobEditingPanel.getJobScriptTextField().setText(selectedJobBean.getScript());
		               this.jobEditingPanel.getJobScriptArgumentTextField().setText(selectedJobBean.getScriptArgument());
		               this.jobEditingPanel.getJobDescriptionTextField().setText(selectedJobBean.getDescription()); 
		            }else 	     	 
		               JOptionPane.showMessageDialog(null, "Please, select a job");
		        }
		        
		        /*
		         * Insert a new job using the data currently present in the form
		         */
		        if (e.getActionCommand().equals("New Job")) 
		        {	 
		        	if(this.validateUserInput())
		        	{	        	
				 	    int userChoice = JOptionPane.showConfirmDialog(this,"Insert as new job ?","Confirm",JOptionPane.YES_NO_OPTION);
				        	
					    if (userChoice == JOptionPane.YES_OPTION)
					    {	        	
				        	JobBean newJobBean = this.buildJobBean();	        	
				        	log.info("Inserting new job, with name: "+this.jobEditingPanel.getJobNameTextField().getText());
				        	
				        	try{			        		
				        		jobDao.insertNewJob(configuredDbType, newJobBean);	        		
				        		//refresh currently showed jobs                     
			                    this.jobCommandPanel.getLoadJobButton().doClick();
				        		this.setOperationLabel(true,"");
				        		
				        		this.cleanEditPanelField();
			
				        	}catch (Exception ex) {
				        		log.fatal("Error inserting the new job, configuration is ok ?, cause: "+ex.getMessage());
				        		this.setOperationLabel(false,"Error inserting new job, database configuration is ok ? See log for details");
							}	        	
					    }
				    
		        	}else 
		        		JOptionPane.showMessageDialog(null, "Please, fill required fields");
		        }
		        
		        /* 
		         * Show some secondary fields of the currently selected job:
		         * "description", "last-execution-date", "executor-msisdn" that are NOT shown in the Job JTable
		         */
		        if (e.getActionCommand().equals("Show Job Details"))
		        {	     	
		        	if(jobsTable.getSelectedRow() !=-1)
		        	{ 	  
		               JobBean selectedJobBean = this.getSelectedJobBean();
		               log.debug("Selected job to shows his details, id: "+selectedJobBean.getId()+" Command: "+selectedJobBean.getScript()+" Interpreter: "+selectedJobBean.getInterpreter()+ " Description: "+selectedJobBean.getDescription());
			    	   
		               //Open a PopUP that show the secondary fields of the currently selected job
		               JobDetailsPopUp jobDetailsPopUp = new JobDetailsPopUp(selectedJobBean.getDescription(),selectedJobBean.getLastExecutionDate(),selectedJobBean.getExecutorMsisdn());
		            }else 	     	 
		               JOptionPane.showMessageDialog(null, "Please, select a job");
		        }
		        
		        /* 
		         * Delete the currently selected job in the job table list
		         */
		        if (e.getActionCommand().equals("Delete Selected")) 
		        {       		
			        if(jobsTable.getSelectedRow() !=-1)
			        {		        		 
			 	      	int userChoice = JOptionPane.showConfirmDialog(this,"Delete the selected job ?","Confirm",JOptionPane.YES_NO_OPTION);
				        	
				       	if (userChoice == JOptionPane.YES_OPTION)
				       	{		        		 
			        	  try{		        		  
				              JobBean selectedJobBean = this.getSelectedJobBean();
				                
		                      log.info("Deleting job with id: "+selectedJobBean.getId());
		                      jobDao.deleteJob(selectedJobBean.getId(),configuredDbType);	                        
		                      //refresh currently shown jobs                     
		                      this.jobCommandPanel.getLoadJobButton().doClick();
		                      this.setOperationLabel(true,"");
		                        
			        	   }catch (Exception ex) {
			        		  log.fatal("Error deleting Jos from DB, configuration is ok?, cause: "+ex.getMessage());
			        	      this.setOperationLabel(false,"Error deleting job,database configuration is ok ? See log for details");
						   }
			            } 
	                }else		       
		        	    JOptionPane.showMessageDialog(null, "Please, select a job");
		        }     
		        
		        /*
		         *  Update the currently selected job showed in the editing form
		         */
		        if(e.getActionCommand().equals("Update")) 
		        {    
		        	 if(jobsTable.getSelectedRow() !=-1)
		        	 {
		        		if(this.validateUserInput())
		        		{	 
		        			int userChoice = JOptionPane.showConfirmDialog(this,"Update the selected job ?","Confirm",JOptionPane.YES_NO_OPTION);
				        	
						    if(userChoice == JOptionPane.YES_OPTION)
						    {	 
					        	JobBean jobBean = this.buildJobBean();
					        	JobBean selectedJobBean = this.getSelectedJobBean();
					        	
					        	try{
					        		jobDao.updateJob(configuredDbType,selectedJobBean.getId(),jobBean);	        	
					        		//refresh currently shown jobs in the table                 
					        		this.jobCommandPanel.getLoadJobButton().doClick();
					        		this.setOperationLabel(true,"");
					        		
					        		this.cleanEditPanelField();
					        		
					        	}catch (Exception ex) {
					        	   log.fatal("Error updating Job, configuration is ok?, cause: "+ex.getMessage());
				        		   this.setOperationLabel(false,"Error updating job, database configuration is ok ? See log for details");
								}
						    }
		        		 }else
		        			 JOptionPane.showMessageDialog(null, "Please, fill required fields");
		        		
		        	 }else	        	
		        	     JOptionPane.showMessageDialog(null, "Please, select a job and press 'Edit' button");
		        }	
		        
		        /* Export the job list to PDF file */
		        if(e.getActionCommand().equals("Export to PDF"))
		        {
		        	if(jobsTable.getSelectedRow() !=-1)
		        	{
		               JobTableModel tableModel = (JobTableModel) jobsTable.getModel();              
		               ArrayList<JobBean> jobBeanList = tableModel.getJobTableBeanList(); //.get(jobsTable.convertRowIndexToModel(selectedRow));
 
	    			   // Open a pop-up window where the user must insert some options (filename and destination folder)
	        		   JobPdfExportPopUp jobPdfExportPopUp = new JobPdfExportPopUp(this.mainFrame,jobBeanList);
		        	}else	        	
		        	   JOptionPane.showMessageDialog(null, "Please, select a job");
		         }
		        
		        /* 
		         * Browse file system to choose the Script interpreter (eg: Python) 
		         */
		        if (e.getActionCommand().equals("Browse Interpreter")) 
		        {
		        	scriptAndInterpreterFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        	scriptAndInterpreterFileChooser.setDialogTitle("Choose the script interpreter");		        	
		              
		            int value = scriptAndInterpreterFileChooser.showOpenDialog(this);
		             
		            //return the chosen value if approved (ie yes, ok) is chosen.
		            if (value==JFileChooser.APPROVE_OPTION)
		            {
		               File scriptInterpreterFile = scriptAndInterpreterFileChooser.getSelectedFile();		              
		               String pathScriptInterpreter = scriptInterpreterFile.getAbsolutePath();
		               this.jobEditingPanel.getJobInterpreterTextField().setText(pathScriptInterpreter);		            
		            }		              
		        }
		        
		        /* 
		         * Browse file system to choose the Script to execute (eg: myscript.py) 
		         */
		        if (e.getActionCommand().equals("Browse Script")) 
		        {
		        	scriptAndInterpreterFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        	scriptAndInterpreterFileChooser.setDialogTitle("Choose the script to execute");		        	
		              
		            int value = scriptAndInterpreterFileChooser.showOpenDialog(this);
		             
		            //return value if approved (ie yes, ok) is chosen.
		            if (value==JFileChooser.APPROVE_OPTION)
		            {
		               File scriptInterpreterFile = scriptAndInterpreterFileChooser.getSelectedFile();		              
		               String pathScript = scriptInterpreterFile.getAbsolutePath();
		               this.jobEditingPanel.getJobScriptTextField().setText(pathScript);		            
		            }
		        }
		        
		        /* 
		         * Close the application 
		         */
		        if (e.getActionCommand().equals("Close")) 
		        {
		        	if(mainFrame.isDisplayable()) {                     
	      			   mainFrame.dispose();
	                } 
		        }
		    }    
	 }
	
	/**
	 * Utility method that set right label about operation result
	 * @param  isSuccessResult true to set the operation success label
	 * @param  customMsg a custom message to append
	 */
	private void setOperationLabel(boolean isSuccessResult,String customMsg){
		
		if(isSuccessResult){			
			jobCommandPanel.getOperationResultLabel().setFont(new Font("Serif", Font.BOLD, 15));
			jobCommandPanel.getOperationResultLabel().setForeground(Color.GREEN);	
			jobCommandPanel.getOperationResultLabel().setText("Operation executed successfully "+customMsg);
			
		}else{
			jobCommandPanel.getOperationResultLabel().setText("");
			jobCommandPanel.getOperationResultLabel().setText(customMsg);
			jobCommandPanel.getOperationResultLabel().setFont(new Font("Serif", Font.BOLD, 15));
			jobCommandPanel.getOperationResultLabel().setForeground(Color.RED);	
		}		
	}
	
	
	/**
	 * Validate the input in the job editing/inserting from
	 * (Note: 'Interpreter' leave blank if you want execute .bat file on windows )
	 * @return true if the input is valid (ie: all the required fields are filled)
	 */
	private boolean validateUserInput(){
			
	    if(!StringUtils.isEmpty(this.jobEditingPanel.getJobNameTextField().getText()) 					   
			   && !StringUtils.isEmpty(this.jobEditingPanel.getJobScriptTextField().getText()))		
		   return true;
		else 
		   return false;
	}	
	
	
	/**
	 * Utility method that return the currently selected Job in the job table list 
	 * @return
	 */
	private JobBean getSelectedJobBean(){
		
		ListSelectionModel model = jobsTable.getSelectionModel();
        int selectedRow = model.getLeadSelectionIndex(); //if '0' mean first row
        log.info("job table, selected row index: "+selectedRow);
     
        JobTableModel a = (JobTableModel) jobsTable.getModel();              
        JobBean selectedJobBean =  a.getJobTableBeanList().get(jobsTable.convertRowIndexToModel(selectedRow));
        
        return selectedJobBean;
	}
	
	/**
	 * Utility method that build a JobBean object using the currently inserted values in the JobEditingPanel
	 * @return A new JobBean
	 */
	private JobBean buildJobBean(){
		
		JobBean jobBean = new JobBean();
    	
		jobBean.setName(this.jobEditingPanel.getJobNameTextField().getText());
		jobBean.setInterpreter(this.jobEditingPanel.getJobInterpreterTextField().getText());
		jobBean.setInterpreterArgument(this.jobEditingPanel.getJobInterpreterArguemtTextField().getText());
		jobBean.setScript(this.jobEditingPanel.getJobScriptTextField().getText());
		jobBean.setScriptArgument(this.jobEditingPanel.getJobScriptArgumentTextField().getText());
		jobBean.setDescription(this.jobEditingPanel.getJobDescriptionTextField().getText());
    	
		//other fields: executor 'msisdn' and 'last execution date' are updated in auto by the DAO at each job execution
		
    	return jobBean;
	}

	/* Called each time the user select another job in the table */
	public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting()) {
            return;
        }
       this.cleanEditPanelField(); 		
	}
	
	public JScrollPane getJobsTableScrollPane() {
		return jobsTableScrollPane;
	}

	public void setJobsTableScrollPane(JScrollPane jobsTableScrollPane) {
		this.jobsTableScrollPane = jobsTableScrollPane;
	}


}
