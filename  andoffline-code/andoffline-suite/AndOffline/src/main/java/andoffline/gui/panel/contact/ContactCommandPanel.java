package andoffline.gui.panel.contact;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import andoffline.integration.database.gui.DataBaseSaverPopUp;
import andoffline.parser.vcf.VcardRecord;


/**
 * Create a panel with some commands to manage the JTree with the sms (eg. filter the sms displayed, export them in pdf...)
 *
 */
public class ContactCommandPanel extends JPanel implements ActionListener{    
    
	private static final long serialVersionUID = 1L;
	
	private JLabel searchNodeContentLabel;
    private JTextField searchNodeContentTextField;
    private JButton searchConfirmButton;
   
    private JButton closeButton;   
    private JButton exportToPdfButton;
    private JButton saveContactsButton;
    
    //The parent JFrame that the close button must close
    private JFrame mainFrame;
   
    //The list of Vcard record built using the content of a vcf file
    private List<VcardRecord> vcardRecordList;
  	
	/**
	 * Constructor
	 * 
	 * @param mainFrame The root main frame to close when the use push the close button (ie the application is close)
	 * @param vcardRecordList The list of vcard record extracted from the vcf file
	 */
	public ContactCommandPanel(JFrame mainFrame) {
		
		this.setBorder(BorderFactory.createTitledBorder("Commands"));
		this.setLayout(new MigLayout("wrap 3")); // we want 3 columns
		
		this.mainFrame = mainFrame;		
		
		exportToPdfButton = new JButton("Export To PDF");
		exportToPdfButton.addActionListener(this);
		
		this.saveContactsButton = new JButton("Save Contact(s)");
		this.saveContactsButton.addActionListener(this);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);

        this.add(saveContactsButton);
        this.add(exportToPdfButton,"gapleft 30");
        this.add(closeButton,"width 105,gapleft 500");
	}
	
    /**
     * Handle the actions registered on the JButton
     */
	public void actionPerformed(ActionEvent e) {		
		 
	      if (e.getSource() instanceof JButton)
	      {	    	  
	          //True if the user has chosen a source VCF file containing Contact
	          if (e.getActionCommand().equals("Search")) 
	          {
	        	  //TODO: next release implements a search into the VCF parsed contacts	        	 
        		  
        	  }else if (e.getActionCommand().equals("Close")) {
        		  
        		  if (mainFrame.isDisplayable()) {                     
        			  mainFrame.dispose();
                  }

        	  }else if (e.getActionCommand().equals("Export To PDF") && vcardRecordList != null) {          		  
        		  /*
        		    Open a popup window where the user insert some options (filename and destination folder)
        		    to export the contact to a PDF file        		         		    
        		   */
        		  ContactPdfExportPopUp contactPdfExportPopUp = new ContactPdfExportPopUp(this.mainFrame,this.vcardRecordList);   
        	  }else if (e.getActionCommand().equals("Save Contact(s)") && vcardRecordList != null){ 
        		  
        		  // open a popup window where the user can choose the database where export the CONTACT(S)
        		  DataBaseSaverPopUp dataBaseChooserPopUp = new DataBaseSaverPopUp(this.mainFrame,this.vcardRecordList,"CONTACT");   
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

	public JButton getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(JButton closeButton) {
		this.closeButton = closeButton;
	}

	public JButton getExportToPdfButton() {
		return exportToPdfButton;
	}

	public void setExportToPdfButton(JButton exportToPdfButton) {
		this.exportToPdfButton = exportToPdfButton;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public List<VcardRecord> getVcardRecordList() {
		return vcardRecordList;
	}

	public void setVcardRecordList(List<VcardRecord> vcardRecordList) {
		this.vcardRecordList = vcardRecordList;
	}


}
