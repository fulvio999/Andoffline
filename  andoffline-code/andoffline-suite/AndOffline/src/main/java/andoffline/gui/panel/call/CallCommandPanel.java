package andoffline.gui.panel.call;

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
import andoffline.parser.call.AllCallParser;
import andoffline.parser.call.CustomCallFilterParser;
import andoffline.parser.call.LostCallParser;
import andoffline.parser.call.MadeCallParser;
import andoffline.parser.call.ReceivedCallParser;
import andoffline.parser.sms.SmsAllParser;



/**
 * Create a panel with some commands to manage the JTree with the sms (eg. filter the sms displayed, export them in pdf...)
 *
 */
public class CallCommandPanel extends JPanel implements ActionListener{    
    
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(CallCommandPanel.class);
	
	private JLabel searchNodeContentLabel;
    private JTextField searchNodeContentTextField;
    private JButton searchConfirmButton;
    
    //Utility button to compact/expand all the nodes of the JTree
    private JButton expandAllNodeButton;
    private JButton compactAllNodeButton;
    
    private JButton closeButton;   
    private JButton filterButton;
    
    private JButton exportToPdfButton;
    private JButton saveCallsButton;
    
    // a sub-panel with an animation to indicates that a processing is in action
    private BusyLabelPanel busyLabelPanel;
    
    // the parent JFrame that the close button must close
    private JFrame mainFrame;
    
    private JComboBox filterOptionCombo;
    
    // the tree to manage and filter
    private JTree tree;
    
    // the xml File with the calls
    private File callXmlFile;
    
    // the root of the tree
    private DefaultMutableTreeNode base;
    
    private static String[] filterOption = { "Received", "Made", "Lost" , "All"};
    
	/**
	 * Constructor
	 */
	public CallCommandPanel(JFrame mainFrame) {
		
		this.setBorder(BorderFactory.createTitledBorder("Commands"));
		this.setLayout(new MigLayout("wrap 6")); // we want 6 columns
		
		this.mainFrame = mainFrame;
		
		searchNodeContentLabel = new JLabel("Search:");
		searchNodeContentTextField = new JTextField();
		searchConfirmButton = new JButton("Search");
		searchConfirmButton.addActionListener(this);
		
		exportToPdfButton = new JButton("Export To PDF");
		exportToPdfButton.addActionListener(this);
		
		this.saveCallsButton = new JButton("Save Call(s)");
		this.saveCallsButton.addActionListener(this);
		
		compactAllNodeButton = new JButton("Compact Tree");
		compactAllNodeButton.addActionListener(this);
		
		expandAllNodeButton = new JButton("Expand Tree");
		expandAllNodeButton.addActionListener(this);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		
		//Create the combo box to filter the tree node    
        filterOptionCombo = new JComboBox(filterOption);
        filterOptionCombo.setSelectedIndex(3);	//set All by default
        
        filterButton = new JButton("Filter");
		filterButton.addActionListener(this);
		
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
        this.add(new JLabel(""),"width 105"); //placeholder
        this.add(exportToPdfButton,"width 105");
        this.add(saveCallsButton,"width 105");  
        this.add(closeButton,"width 105");
	}
	
    /**
     * Manage the events on the Buttons 
     */
	public void actionPerformed(ActionEvent e) {		
		  
	      if (e.getSource() instanceof JButton)  
	      {	    	  
	          // True if the user has choosen a source XML file containing a Log Call
	          if (e.getActionCommand().equals("Search") && tree !=null) 
	          {
	        	  String customStringToSearch = searchNodeContentTextField.getText();	        	  
	        	  SAXParser parser = null;
        		  SAXParserFactory factory = SAXParserFactory.newInstance();
        		  
        		  try {
	        		  if(customStringToSearch != null && !customStringToSearch.equalsIgnoreCase("")) {
	 	        		 
		        			 //prepare the parser to find the inserted String
		        			 parser = factory.newSAXParser();
		        			 parser.parse(callXmlFile,new CustomCallFilterParser(base,tree, customStringToSearch));
	       		  
	        		  }else{ //The search string is empty: no filter is applied
	        			  	 parser = factory.newSAXParser();	
							 parser.parse(callXmlFile,new SmsAllParser(base,tree));
							 searchNodeContentTextField.setText(""); //clean the filter combo
	        		  }
 						
        		  } catch (Exception e1) {
        			  log.fatal("Error parsing the call tree, cause: "+e1.getMessage());   
        		  }
	        	
	          }else if (e.getActionCommand().equals("Expand Tree") && tree !=null){
	        	  
	        	  int row = tree.getRowCount() - 1;
	        	  while (row >= 0) {
	        	      tree.expandRow(row);	        	     
	        	      row--;
	        	  }	        	  

        	  }else if (e.getActionCommand().equals("Compact Tree") && tree !=null) {
         		  
        		  int row = tree.getRowCount() - 1;
	        	  //Note: use 2 to compact only the <sms> child and not the root
	        	  while (row >= 2) {
	        	      tree.collapseRow(row);	        	      
	        	      row--;
	        	  }
        		  
        	  }else if (e.getActionCommand().equals("Close")){
        		  if (mainFrame.isDisplayable()) {                     
        			  mainFrame.dispose();
                  }

        	  } else if (e.getActionCommand().equals("Filter") && tree !=null) {
        		  
        		  String callTypeToShow = (String) filterOptionCombo.getSelectedItem();        		 
        		  
        		  SAXParser parser = null;
        		  SAXParserFactory factory = SAXParserFactory.newInstance();
      				
     				try {      					 
    					  //Depending on the type of filter chosen {"All", "Received", "Made", "Lost"} create the right instance of the parse
    					  if(callTypeToShow.equalsIgnoreCase("All")){
    						  
    						  parser = factory.newSAXParser();	
    						  parser.parse(callXmlFile,new AllCallParser(base,tree,callTypeToShow));
    						  searchNodeContentTextField.setText(""); //clean the filter combo
    					  }	  
    					  
    					  if(callTypeToShow.equalsIgnoreCase("Received")){
    						  
    						 parser = factory.newSAXParser();	
    						 parser.parse(callXmlFile,new ReceivedCallParser(base,tree));
    						 searchNodeContentTextField.setText(""); //clean the filter combo
    					  }
    							 
    					  if(callTypeToShow.equalsIgnoreCase("Made")){
    						  
    						 parser = factory.newSAXParser();	
    						 parser.parse(callXmlFile,new MadeCallParser(base,tree));
    						 searchNodeContentTextField.setText(""); //clean the filter combo
    					  }
    					  
    					  if(callTypeToShow.equalsIgnoreCase("Lost")){
    						  
     						 parser = factory.newSAXParser();	
     						 parser.parse(callXmlFile,new LostCallParser(base,tree));
     						 searchNodeContentTextField.setText(""); //clean the filter combo
     					  }
						
    				  } catch (Exception e1) {						
    					  log.fatal("Error parsing the call tree for filter, cause: "+e1.getMessage());
    				  }
     				 
        	  } else if (e.getActionCommand().equals("Export To PDF") && tree !=null){
        		  
        		  // Open a pop-up window where the user must insert some options (filename and destination folder)
        		  CallPdfExportPopUp callPdfExportPopUp = new CallPdfExportPopUp(this.mainFrame,tree);     
        		  
        	  }	else if (e.getActionCommand().equals("Save Call(s)") && tree !=null){ 
        		  
        		  //open a popup window where the user can choose the database where export the SMS
        		  DataBaseSaverPopUp dataBaseChooserPopUp = new DataBaseSaverPopUp(this.mainFrame,tree,"CALL");   
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


	public DefaultMutableTreeNode getBase() {
		return base;
	}


	public void setBase(DefaultMutableTreeNode base) {
		this.base = base;
	}


	public File getCallXmlFile() {
		return callXmlFile;
	}


	public void setCallXmlFile(File callXmlFile) {
		this.callXmlFile = callXmlFile;
	}


	public JButton getSaveCallsButton() {
		return saveCallsButton;
	}


	public void setSaveCallsButton(JButton saveCallsButton) {
		this.saveCallsButton = saveCallsButton;
	}


	public BusyLabelPanel getBusyLabelPanel() {
		return busyLabelPanel;
	}


	public void setBusyLabelPanel(BusyLabelPanel busyLabelPanel) {
		this.busyLabelPanel = busyLabelPanel;
	}



}
