package andoffline.gui.panel.contact;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import andoffline.gui.common.utility.VcfTranslatorUtils;
import andoffline.parser.vcf.VCFparser;
import andoffline.parser.vcf.VcardEntry;
import andoffline.parser.vcf.VcardRecord;


/**
 * Create the panel where the user can chose a VCF file to be parsed and show the VCF record extracted 
 *
 */
public class ContactPanel extends JPanel implements ActionListener{
		
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(ContactPanel.class);
	
	private JLabel sourceVCFlabel;
    private JTextField sourceVCFfileTextField; //the VCF file to be processed
    private String pathSourceVCFfile; //the absolute path to the source VCF file
    
    private JButton browseButton;
    
    // The Container frame that the "close" jbutton of the command panel must close
    private JFrame mainFrame;
	
    // The panel where will be inserted the records extracted from the vcf file
    private JPanel vcfRecordPanel;
	private JScrollPane vcfRecordScrollPanel;
	
	// The vcf file with the contacts exported from the phone
	private File contactVCFfile;
	
	// The sub-panel with the the "Export To PDF" and "Close" buttons
	private ContactCommandPanel contactCommandPanel;
 	
	/**
	 * Create the Contacts tab panel
	 */
	public ContactPanel(JFrame mainFrame) {
		
			this.setBorder(BorderFactory.createTitledBorder("Contact(s) Found :"));
			this.setLayout(new MigLayout("wrap 3")); // we want 3 columns
			
			/* The root main frame to close when the user push the close button (ie the application is close) in the contactCommandPanel */
			this.mainFrame = mainFrame;
		    
			sourceVCFfileTextField = new JTextField();
			sourceVCFfileTextField.setToolTipText("The VCF file with the contact");
			sourceVCFlabel = new JLabel("Source VCF file:");
		 	
		 	browseButton = new JButton("Find");
	        browseButton.addActionListener(this);
	        
	        //--- The panel with the VcardRecord extracted from the chosen VCF file
	        vcfRecordPanel = new JPanel();
	        vcfRecordPanel.setLayout(new BoxLayout(vcfRecordPanel, BoxLayout.PAGE_AXIS));
	        
	        vcfRecordScrollPanel = new JScrollPane(vcfRecordPanel);
	        vcfRecordScrollPanel.setBorder(BorderFactory.createTitledBorder("Contact Viewer"));
			
	        contactCommandPanel = new ContactCommandPanel(mainFrame);
	       
	        //--- 1st row	           
	        this.add(sourceVCFlabel);	        
	        this.add(sourceVCFfileTextField,"width 720");	       
	        this.add(browseButton,"width 105");
		        
	        //--- 2nd row	        
	        this.add(vcfRecordScrollPanel,"span 3,height 1000,growx,wrap"); //the scroll panel with the VCF records
	       
	        //--- 3rd row	        
	        this.add(contactCommandPanel,"span 3,growx,width 1000");		    
	}

    // Event Handler attached with the Find button (ie the file chooser)
	public void actionPerformed(ActionEvent e) {
		
		  final JFileChooser sorceVcfFileChooser = new JFileChooser();		
		   
	      if (e.getSource() instanceof JButton)  
	      {	    	  
	          // the user has chosen a VCF file containing exported contact from the phone 
	          if (e.getActionCommand().equals("Find")) 
	          {
	        	  sorceVcfFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        	  sorceVcfFileChooser.setDialogTitle("Choose a VCF file");	        	          	  
	              
	        	  //TODO apply file filter to the filechooser
	        	  
	              int value = sorceVcfFileChooser.showOpenDialog(this);
	             
	              //return value if approved (ie yes, ok) is chosen.
	              if (value==JFileChooser.APPROVE_OPTION)
	              {
	            	  contactVCFfile = sorceVcfFileChooser.getSelectedFile();
	            	  
	            	  //get the absolute path of the chosen file
	            	  pathSourceVCFfile = contactVCFfile.getAbsolutePath(); 
	            	  
	                  //set the file path in the textField
	            	  sourceVCFfileTextField.setText(pathSourceVCFfile); 
	            	  
	            	  VCFparser vcfParser = new VCFparser(pathSourceVCFfile); 
	            	  
	            	  try {	            		  
	            		    List<VcardRecord> vcardRecordList = vcfParser.parseContact();
	            		    
	            		    this.getContactCommandPanel().setVcardRecordList(vcardRecordList);
	            		  
		            		  for(int i=0; i<vcardRecordList.size(); i++){
		            			  
		            			  //log.trace("---- Start VCF Record ------");
		            			 
		            			  VcardRecord rec = vcardRecordList.get(i);
		            			  List<VcardEntry> entryList = rec.getVcardEntry();
		            			  
		            			  for(int j=0; j<entryList.size(); j++){
		            				  
		            				  VcardEntry entry = entryList.get(j);
		            				  //log.trace(" Type: "+entry.getType()+ "  Value: "+entry.getValue());		            				  		            				  
		            				  
		            				  /* Convert the label in a human readable label (eg N ---> Name) */ 		            				   
		            				  String labelToDisplay = VcfTranslatorUtils.conversionNameTable.get(entry.getType().trim());
		            				  
		            				  //in case of there is no translation 
		            				  if (labelToDisplay == null)
		            					  labelToDisplay = entry.getType();
		            				  
		            				  this.getVcfRecordPanel().add(new JLabel("<html><b>"+labelToDisplay+"</b> : "+entry.getValue()+"</html>"));
		            				 
		            			  }		            			 
		            			  this.getVcfRecordScrollPanel().repaint();
		            			  this.getVcfRecordScrollPanel().validate();
		            			  
		            			  this.getVcfRecordPanel().repaint();		            			  
		            			  this.getVcfRecordPanel().validate();
		            			  
		            			  //log.trace("---- End VCF Record ------");
	            		
		            			  this.getVcfRecordPanel().add(new JSeparator(SwingConstants.HORIZONTAL));
	            		  }
					
						} catch (Exception e1) {
						  log.fatal("Error parsing VCF contact, cause:_",e1);							
						}
	              }	           
	          }	          
	      }		
	}

	public JLabel getSourceVCFlabel() {
		return sourceVCFlabel;
	}

	public void setSourceVCFlabel(JLabel sourceVCFlabel) {
		this.sourceVCFlabel = sourceVCFlabel;
	}

	public JTextField getSourceVCFfileTextField() {
		return sourceVCFfileTextField;
	}

	public void setSourceVCFfileTextField(JTextField sourceVCFfileTextField) {
		this.sourceVCFfileTextField = sourceVCFfileTextField;
	}

	public String getPathSourceVCFfile() {
		return pathSourceVCFfile;
	}

	public void setPathSourceVCFfile(String pathSourceVCFfile) {
		this.pathSourceVCFfile = pathSourceVCFfile;
	}

	public JButton getBrowseButton() {
		return browseButton;
	}

	public void setBrowseButton(JButton browseButton) {
		this.browseButton = browseButton;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public File getContactVCFfile() {
		return contactVCFfile;
	}

	public void setContactVCFfile(File contactVCFfile) {
		this.contactVCFfile = contactVCFfile;
	}

	public ContactCommandPanel getContactCommandPanel() {
		return contactCommandPanel;
	}

	public void setContactCommandPanel(ContactCommandPanel contactCommandPanel) {
		this.contactCommandPanel = contactCommandPanel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JPanel getVcfRecordPanel() {
		return vcfRecordPanel;
	}

	public void setVcfRecordPanel(JPanel vcfRecordPanel) {
		this.vcfRecordPanel = vcfRecordPanel;
	}

	public JScrollPane getVcfRecordScrollPanel() {
		return vcfRecordScrollPanel;
	}

	public void setVcfRecordScrollPanel(JScrollPane vcfRecordScrollPanel) {
		this.vcfRecordScrollPanel = vcfRecordScrollPanel;
	}

//	public List<VcardRecord> getVcardRecordList() {
//		return vcardRecordList;
//	}
//
//	public void setVcardRecordList(List<VcardRecord> vcardRecordList) {
//		this.vcardRecordList = vcardRecordList;
//	}
	

}
