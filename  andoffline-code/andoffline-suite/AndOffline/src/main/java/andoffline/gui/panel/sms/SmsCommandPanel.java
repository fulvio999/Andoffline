package andoffline.gui.panel.sms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import andoffline.gui.common.utility.BusyLabelPanel;
import andoffline.integration.database.gui.DataBaseSaverPopUp;
import andoffline.parser.sms.CustomSmsFilterParser;
import andoffline.parser.sms.SmsAllParser;
import andoffline.parser.sms.SmsReceivedParser;
import andoffline.parser.sms.SmsSentParser;


/**
 * Create a panel with the commands to manage the JTree with the sms (eg. filter the sms displayed, export them in pdf...)
 *
 */
public class SmsCommandPanel extends JPanel implements ActionListener{    
    
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(SmsCommandPanel.class);
	
	private JLabel searchNodeContentLabel;
    private JTextField searchNodeContentTextField;
    private JButton searchConfirmButton;
    
    // Utility button to compact/expand all the nodes of the JTree
    private JButton expandAllNodeButton;
    private JButton compactAllNodeButton;
    
    private JButton closeButton;   
    private JButton filterButton;
    
    private JButton exportToPdfButton;
    private JButton saveMessagesButton;
    
    // The parent JFrame that the close button must close
    private JFrame mainFrame;
    
    private JComboBox filterOptionCombo;
    
    // A sub-panel with an animation to indicates that a processing is in action
    private BusyLabelPanel busyLabelPanel;
    
    // The tree to manage and filter
    private JTree tree;
    
    // The xml File with the sms
    private File smsXmlFile;
    
    // The root of the tree
    private DefaultMutableTreeNode base;
    
    private static String[] filterOption = { "Received", "Sent", "All"};
    
	/**
	 * Constructor
	 */
	public SmsCommandPanel(JFrame mainFrame) {
		
		this.setBorder(BorderFactory.createTitledBorder("Commands"));
		this.setLayout(new MigLayout("wrap 6")); // we want 6 columns
		
		this.mainFrame = mainFrame;
		
		this.searchNodeContentLabel = new JLabel("Search:");
		this.searchNodeContentTextField = new JTextField();
		this.searchConfirmButton = new JButton("Search");
		this.searchConfirmButton.addActionListener(this);
		
		this.exportToPdfButton = new JButton("Export To PDF");
		this.exportToPdfButton.addActionListener(this);
		
		this.saveMessagesButton = new JButton("Save Message(s)");
		this.saveMessagesButton.addActionListener(this);
		
		this.compactAllNodeButton = new JButton("Compact Tree");
		this.compactAllNodeButton.addActionListener(this);
		
		this.expandAllNodeButton = new JButton("Expand Tree");
		this.expandAllNodeButton.addActionListener(this);
		
		this.closeButton = new JButton("Close");
		this.closeButton.addActionListener(this);
		
		// Create the combo box to filter the tree nodes    
		this.filterOptionCombo = new JComboBox(filterOption);
        this.filterOptionCombo.setSelectedIndex(2);	
        
        this.filterButton = new JButton("Filter");
        this.filterButton.addActionListener(this);
		
		this.busyLabelPanel = new BusyLabelPanel();

		//--- 1st row
        this.add(filterOptionCombo,"width 105"); 
        this.add(filterButton,"width 125,align left");  
        this.add(busyLabelPanel,"width 180");
        this.add(searchNodeContentLabel,"align right");
        this.add(searchNodeContentTextField,"width 305");
        this.add(searchConfirmButton,"width 105,wrap");     
        
        //--- 2nd row 
        this.add(compactAllNodeButton,"width 105"); 
        this.add(expandAllNodeButton,"width 105");
        this.add(new JLabel("")); //placeholder
        this.add(exportToPdfButton,"width 105");
        this.add(saveMessagesButton,"width 105"); 
        this.add(closeButton,"width 105");
	}
	

  /*
   * Manage the actions on the buttons
   * 
   */
   public void actionPerformed(ActionEvent e) {		
			
	      if (e.getSource() instanceof JButton)  
	      {	    	  
	          // the user has pressed the Search button to find a custom String
	          if (e.getActionCommand().equals("Search") && tree !=null) 
	          {	        	  
	        	  String customStringToSearch = searchNodeContentTextField.getText();
	        	  
	        	  SAXParser parser = null;
        		  SAXParserFactory factory = SAXParserFactory.newInstance();
        		        		  
	        	  try {
		        	   if(customStringToSearch != null && !customStringToSearch.equalsIgnoreCase(""))
		        	   {		 	        		 
			        	  //prepare the parser to find the filter String
			        	  parser = factory.newSAXParser();
			        	  parser.parse(smsXmlFile,new CustomSmsFilterParser(base,tree, customStringToSearch));
		       		  
		        		}else{ //The search string is empty: no filter is applied
		        		   parser = factory.newSAXParser();	
						   parser.parse(smsXmlFile,new SmsAllParser(base,tree));
						   searchNodeContentTextField.setText(""); //clean the filter combo
		        		}
	 						
	        		  } catch (Exception e1) {
	        			  log.fatal("Error parsing the sms tree, cause: "+e1.getMessage());        			 
	        		  }        		  
        		 
	        	
	          }else if (e.getActionCommand().equals("Expand Tree") && tree !=null) {  //expand all the JTree
	        	  
	        	  int row = tree.getRowCount() - 1;
	        	  while (row >= 0) {
	        	      tree.expandRow(row);	        	     
	        	      row--;
	        	  }	        	  

        	  }else if (e.getActionCommand().equals("Compact Tree") && tree !=null){  //compact all the JTree
         		  
        		  int row = tree.getRowCount() - 1;
	        	  //Note: here we use '2' as fix value to compact only the <sms> child and not the root
	        	  while (row >= 2) {
	        	      tree.collapseRow(row);	        	      
	        	      row--;
	        	  }
        		  
        	  }else if (e.getActionCommand().equals("Close")){
        		  if (mainFrame.isDisplayable()) {                     
        			  mainFrame.dispose();
                  }

        	  }else if (e.getActionCommand().equals("Filter") && tree !=null) {
        		  
        		  String smsTypeToShow = (String) filterOptionCombo.getSelectedItem();
        		  
        		  SAXParser parser = null;
        		  SAXParserFactory factory = SAXParserFactory.newInstance();
        			 
     				  try {      					 
     					  //Depending on the type of filter chosen (Received, Sent, or custom filter) create the right instance of the parse
     					  if(smsTypeToShow.equalsIgnoreCase("All")){
     						  
     						  parser = factory.newSAXParser();	
     						  parser.parse(smsXmlFile,new SmsAllParser(base,tree));
     						  searchNodeContentTextField.setText(""); //clean the filter combo
     					  }	  
     					  
     					  if(smsTypeToShow.equalsIgnoreCase("Sent")){
     						  
     						 parser = factory.newSAXParser();	
    						 parser.parse(smsXmlFile,new SmsSentParser(base,tree));
    						 searchNodeContentTextField.setText(""); //clean the filter combo
     					  }
     							 
     					  if(smsTypeToShow.equalsIgnoreCase("Received")){
     						  
     						 parser = factory.newSAXParser();	
    						 parser.parse(smsXmlFile,new SmsReceivedParser(base,tree));
    						 searchNodeContentTextField.setText(""); //clean the filter combo
     					  }
						
     				  } catch (Exception e1) {	
     					  log.fatal("Error: ",e1);     					 
     				  }        			  
        		  
        	  } else if (e.getActionCommand().equals("Export To PDF") && tree !=null){        		  
        		  
        		  // Open a popup window where insert some options for the pdf file to produce (filename and destination folder)
        		  SmsPdfExportPopUp smsPdfExportPopUp = new SmsPdfExportPopUp(this.mainFrame,tree);   
        		  
        	  } else if (e.getActionCommand().equals("Save Message(s)") && tree !=null){ 
        		  
        		  //open a popup window where the user can choose the database where export the SMS 
        		  DataBaseSaverPopUp dataBaseSaverPopUp = new DataBaseSaverPopUp(this.mainFrame,tree,"SMS");   
        	  }	        	  
	      }
	}	


	public JLabel getSearchNodeContentLabel() {
		return searchNodeContentLabel;
	}

	public void setSearchNodeContentLabel(JLabel searchNodeContentLabel) {
		this.searchNodeContentLabel = searchNodeContentLabel;
	}

	public JTextField getSearchNodeContentTextField() {
		return searchNodeContentTextField;
	}

	public void setSearchNodeContentTextField(JTextField searchNodeContentTextField) {
		this.searchNodeContentTextField = searchNodeContentTextField;
	}

	public JButton getSearchConfirmButton() {
		return searchConfirmButton;
	}

	public void setSearchConfirmButton(JButton searchConfirmButton) {
		this.searchConfirmButton = searchConfirmButton;
	}

	public JButton getExpandAllNodeButton() {
		return expandAllNodeButton;
	}

	public void setExpandAllNodeButton(JButton expandAllNodeButton) {
		this.expandAllNodeButton = expandAllNodeButton;
	}

	public JButton getCompactAllNodeButton() {
		return compactAllNodeButton;
	}

	public void setCompactAllNodeButton(JButton compactAllNodeButton) {
		this.compactAllNodeButton = compactAllNodeButton;
	}

	public JButton getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(JButton closeButton) {
		this.closeButton = closeButton;
	}

	public JButton getFilterButton() {
		return filterButton;
	}

	public void setFilterButton(JButton filterButton) {
		this.filterButton = filterButton;
	}

	public JButton getExportToPdfButton() {
		return exportToPdfButton;
	}

	public void setExportToPdfButton(JButton exportToPdfButton) {
		this.exportToPdfButton = exportToPdfButton;
	}

	public JComboBox getFilterOptionCombo() {
		return filterOptionCombo;
	}

	public void setFilterOptionCombo(JComboBox filterOptionCombo) {
		this.filterOptionCombo = filterOptionCombo;
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;		
	}


	public JFrame getMainFrame() {
		return mainFrame;
	}


	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}


	public File getSmsXmlFile() {
		return smsXmlFile;
	}


	public void setSmsXmlFile(File smsXmlFile) {
		this.smsXmlFile = smsXmlFile;
	}


	public DefaultMutableTreeNode getBase() {
		return base;
	}


	public void setBase(DefaultMutableTreeNode base) {
		this.base = base;
	}


	public BusyLabelPanel getBusyLabelPanel() {
		return busyLabelPanel;
	}


	public void setBusyLabelPanel(BusyLabelPanel busyLabelPanel) {
		this.busyLabelPanel = busyLabelPanel;
	}


	public JButton getSaveMessagesButton() {
		return saveMessagesButton;
	}


	public void setSaveMessagesButton(JButton saveMessagesButton) {
		this.saveMessagesButton = saveMessagesButton;
	}


}
