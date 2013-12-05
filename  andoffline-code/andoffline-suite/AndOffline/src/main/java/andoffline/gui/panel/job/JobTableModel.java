package andoffline.gui.panel.job;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import andoffline.integration.database.dto.JobBean;

/**
 * Define a custom TableModel for the JTable that show the jobs saved in the database 
 *
 */
public class JobTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	/* NOTE: not all the fields of a job are shown in the table. Others ones are shown on demand using a popup window */
    private String[] columnNames = {"id","Name","Interpreter","Interpreter Argument","Script","Script Argument"};
	
	/* The list of beans (fetched from Database) to display in the jtable: each table row represents a bean 
	 * (ie each cell is a bean field value) */
	private ArrayList<JobBean> jobTableBeanList = new ArrayList<JobBean>();

	
	/* 
	 * Constructor
	 */
	public JobTableModel(){
		
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public int getRowCount() {
		return jobTableBeanList.size();
	}
	
	/* Core method that return the name to place in the column table */
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	/**
	 * Return the value to insert in a jtable cell: the returned value depends on the column index in argument 
	 */
	public Object getValueAt(int row, int col) {
		
		JobBean tb = jobTableBeanList.get(row);
		
		switch (col) {
	      case 0:
	         return tb.getId();
	      case 1:
	         return tb.getName();
	      case 2:
	         return tb.getInterpreter();
	      case 3:
		     return tb.getInterpreterArgument();
	      case 4:
	         return tb.getScript();
	      case 5:
	    	 return tb.getScriptArgument();	      
	      default:
	         return null;
	   }		
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public ArrayList<JobBean> getJobTableBeanList() {
		return jobTableBeanList;
	}

	public void setJobTableBeanList(ArrayList<JobBean> jobTableBeanList) {
		this.jobTableBeanList = jobTableBeanList;
	}


}
