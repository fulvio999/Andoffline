package andoffline.gui.panel.call;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.miginfocom.swing.MigLayout;
import andoffline.parser.call.AllCallParser;



/**
 * Create the panel with the features to manage the Call
 * 
 *
 */
public class CallPanel extends JPanel implements ActionListener, ChangeListener{
		
	private static final long serialVersionUID = 1L;
	
	private JLabel sourceXMLlabel;
    private JTextField sourceXMLfileTextField; //the XML file to be processed by xpath
    private String pathSourceXMLfile; //the absolute path to the source file
    
    private JButton browseButton;
    
    // The Container frame that the "close" JButton of the command panel must close
    private JFrame mainFrame;
	
    private JPanel xmlTextAreaPanel;
	private JScrollPane scrollPanel;
	
	private JTree xmlJTree;
	
	// The file with the sms exported from the phone
	private File smsXmlFile;
	
	private CallCommandPanel commandPanel;
 	
	/**
	 * Create the SMS tab panel
	 */
	public CallPanel(JFrame mainFrame) {
		
			this.setBorder(BorderFactory.createTitledBorder("Configuration"));
			this.setLayout(new MigLayout("wrap 3")); // we want 3 columns
			
			this.mainFrame = mainFrame;
		    
		 	sourceXMLfileTextField = new JTextField();
		 	sourceXMLfileTextField.setToolTipText("The XML file to process with XPATH");
		 	sourceXMLlabel = new JLabel("Source XML file:");
		 	
		 	browseButton = new JButton("Find");
	        browseButton.addActionListener(this);
	        
	        xmlTextAreaPanel = new JPanel();
	        xmlTextAreaPanel.setLayout( new GridLayout(1,1));
	        
	        scrollPanel = new JScrollPane(xmlTextAreaPanel);
	        scrollPanel.setBorder(BorderFactory.createTitledBorder("Log Call Tree View"));
			
	        commandPanel = new CallCommandPanel(mainFrame);
	        
		 	//--- 1st row	        
	        this.add(sourceXMLlabel);
	        this.add(sourceXMLfileTextField,"width 720");	       
	        this.add(browseButton,"width 105,wrap");
		        
	        //--- 2nd row	       
	        this.add(scrollPanel,"span 3,height 1000,growx,wrap"); //the scroll panel with the xml tree	        
	        
	        //-- 3rd row   
	        this.add(commandPanel,"span 3,growx,width 1000");		    
		  }

    //--- Event Handler attached with the Find button (ie the file chooser)
	public void actionPerformed(ActionEvent e) {
		
		  final JFileChooser sorceXmFileChooser = new JFileChooser();
				
	      if (e.getSource() instanceof JButton)  
	      {	    	  
	          // the user has chosen a source XML file containing SMS
	          if (e.getActionCommand().equals("Find")) 
	          {
  	        	  sorceXmFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        	  sorceXmFileChooser.setDialogTitle("Choose xml file source");
	        	  //sorceXmFileChooser.setFileFilter(new DirectoryFilter());
	              
	              int value = sorceXmFileChooser.showOpenDialog(this);
	             
	              //Return value if approved (ie yes, ok) is chosen.
	              if (value==JFileChooser.APPROVE_OPTION)
	              {
	            	  smsXmlFile = sorceXmFileChooser.getSelectedFile();
	            	  //get the absolute path of the chosen file
	            	  pathSourceXMLfile = smsXmlFile.getAbsolutePath(); 
	                  //set the file path in the textField
	            	  sourceXMLfileTextField.setText(pathSourceXMLfile); 
	            	  
	            	  try{
	            		  
	            		  //Remove the old content displayed in the text area
	            		  this.getXmlTextAreaPanel().removeAll(); 
	            		  
	            		  //The root node of the JTree. Is a special node that has no parent but can have childs 
	            		  DefaultMutableTreeNode base = new DefaultMutableTreeNode("Messages");
	            		 
	            		  //Create an empty tree	            		 
	            		  xmlJTree = new JTree(base);
	     				  xmlJTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	     				  xmlJTree.setAutoscrolls(true);
	            		 
	     				  //Enable tool tips.
	     			      //ToolTipManager.sharedInstance().registerComponent(xmlJTree);
	     				  
	            		  SAXParserFactory factory = SAXParserFactory.newInstance();
	     				  SAXParser parser =  factory.newSAXParser();	     				
	     				 
	     				  /*
	     				   * Fill the tree: Indicates at the parser the handler (ie a class that extends the default event handler: DefaultHandler)
	     				   *  and the sms type (All, Received, Sent) to show in the tree. By default show all sms are shown 
	     				   *  If sms type is 2 means sent, if 1 means received
	     				   */
	     				  parser.parse(smsXmlFile,new AllCallParser(base,xmlJTree,"All"));
	     				 
	     				  //NOTE: ONLY when the tree is created when can use a custom renderer to use custom icons 
	     				  TreeCellRenderer renderer = new CallPanelCustomIconRenderer();	     		            
     		              xmlJTree.setCellRenderer(renderer);     		              
     		             
     		              this.commandPanel.setTree(xmlJTree);
     		              this.commandPanel.setCallXmlFile(smsXmlFile);
     		              this.commandPanel.setBase(base);
	     				
	     				 /* Currently not used Attach a listener to intercept the selection event of a CALL node */
//	     				 xmlJTree.addTreeSelectionListener(new TreeSelectionListener() {
//							
//							public void valueChanged(TreeSelectionEvent e) {
//								
//								TreePath tp = e.getPath(); 
//								
//								TreeNode node = (TreeNode) tp.getLastPathComponent();
//								if (node.getChildCount() >= 0) {
//									
//								      for (Enumeration en= node.children(); en.hasMoreElements();) {
//								        TreeNode n = (TreeNode) en.nextElement();
//								        System.out.println("--** "+n);
//								       
//								      }
//								}
//
//							}
//						});
	     			    		  
	     			    this.getXmlTextAreaPanel().add(xmlJTree);
	     			    
	     			    //To refresh (and redraw) the panel and show the tree view of the chosen XML file
	     			    this.revalidate();		            		  
			        		  
		                }catch (Exception ex) {
		                	  this.getXmlTextAreaPanel().add(new JLabel("Error: "+ex.toString()));
						} 
	          
	              }
	           
	          }
	          
	      }    
		
	}

	public JPanel getXmlTextAreaPanel() {
		return xmlTextAreaPanel;
	}

	public void setXmlTextAreaPanel(JPanel xmlTextAreaPanel) {
		this.xmlTextAreaPanel = xmlTextAreaPanel;
	}

	public JTextField getSourceXMLfileTextField() {
		return sourceXMLfileTextField;
	}

	public void setSourceXMLfileTextField(JTextField sourceXMLfileTextField) {
		this.sourceXMLfileTextField = sourceXMLfileTextField;
	}

	public JScrollPane getScrollPanel() {
		return scrollPanel;
	}

	public void setScrollPanel(JScrollPane scrollPanel) {
		this.scrollPanel = scrollPanel;
	}

	
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() instanceof JButton)
			System.out.println("CIAO");
		
	}


	public JTree getXmlJTree() {
		return xmlJTree;
	}


	public void setXmlJTree(JTree xmlJTree) {
		this.xmlJTree = xmlJTree;
	}


	public JButton getBrowseButton() {
		return browseButton;
	}


	public void setBrowseButton(JButton browseButton) {
		this.browseButton = browseButton;
	}


	public JLabel getSourceXMLlabel() {
		return sourceXMLlabel;
	}


	public void setSourceXMLlabel(JLabel sourceXMLlabel) {
		this.sourceXMLlabel = sourceXMLlabel;
	}


	public String getPathSourceXMLfile() {
		return pathSourceXMLfile;
	}


	public void setPathSourceXMLfile(String pathSourceXMLfile) {
		this.pathSourceXMLfile = pathSourceXMLfile;
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
	

}
