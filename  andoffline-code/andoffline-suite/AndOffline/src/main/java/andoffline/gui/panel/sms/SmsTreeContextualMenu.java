
package andoffline.gui.panel.sms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import andoffline.integration.mailserver.gui.MailComposerPopUp;
import andoffline.integration.twitter.gui.DirectMessageComposerPopUp;
import andoffline.integration.twitter.gui.TwitterComposerPopUp;

/**
 * Custom mouse listener to listen for mouse event on the JTree node with the SMS 
 * It's show a little PopUp window containing a contextual menu, where the user can:
 * - send sms text to Twitter
 * - send sms text a "Twitter Direct Message"
 * - send sms text as email
 *
 */
public class SmsTreeContextualMenu extends MouseAdapter implements ActionListener{
	
	private static final Logger log = Logger.getLogger(SmsTreeContextualMenu.class);

	private JPopupMenu popup;	
	private String selectedSmsText;
	
	/**
	 * Constructor
	 */
	public SmsTreeContextualMenu() {
		
	}
	
	
	/**
	 *  Handle the mouse events on the SMS nodes of the JTree and show a contextual menu
	 */
	public void mousePressed(MouseEvent e) {         	
		
		  /*
		   *  Get the position where user has clicked to know the selected tree node:
		   *  it is the position to show the popUp
		   */
	      int x = e.getX();
	      int y = e.getY();
	      
	      JTree tree = (JTree)e.getSource();
	      TreePath path = tree.getPathForLocation(x,y);
	         
	      if(path == null)
	         return; 
	
	      tree.setSelectionPath(path);	         
	         
	      // The menu entry shown on SMS tree node selection  
	      JMenuItem sendMsgToTwitter = new JMenuItem("Send sms text to Twitter");
	      JMenuItem sendMsgAsTwitterdirectMessage = new JMenuItem("Send sms as Twitter Direct Message");
	      JMenuItem sendAsMail = new JMenuItem("Send as Email");
	         
	      sendMsgToTwitter.addActionListener(this);
	      sendAsMail.addActionListener(this);
	      sendMsgAsTwitterdirectMessage.addActionListener(this);
	         
	      popup = new JPopupMenu();
	      popup.add(sendMsgToTwitter);	
	      popup.add(sendMsgAsTwitterdirectMessage);	
	      popup.add(sendAsMail);	        
	      popup.setBorder(BorderFactory.createLineBorder(Color.red));
				
	      TreeNode node = (TreeNode) tree.getLastSelectedPathComponent();	       
	      selectedSmsText = node.getChildAt(5).toString().split("=")[1];	         
			
		  popup.show((JComponent)e.getSource(), x, y);			          
	}  
   
    public void mouseReleased(MouseEvent e) {      	
           
    }
      
      
    /**
  	 * Manage the click event on the popUp item: open the right popUp according with the chosen entry in the contextual menu
  	 */
  	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equalsIgnoreCase("Send as Email")){	
			log.debug("SMS Text to send by mail: "+  selectedSmsText);
			MailComposerPopUp mailComposerPopUp = new MailComposerPopUp(selectedSmsText);	 
		}
		
		if(e.getActionCommand().equalsIgnoreCase("Send sms text to Twitter")){
			log.debug("SMS Text to send to Tweetter: "+  selectedSmsText);
			TwitterComposerPopUp tweetComposerPopUp = new TwitterComposerPopUp(selectedSmsText);
		}
		
		if(e.getActionCommand().equalsIgnoreCase("Send sms as Twitter Direct Message")){
			log.debug("SMS Text to send as Twitter Direct Message: "+  selectedSmsText);
			DirectMessageComposerPopUp directMessageComposerPopUp = new DirectMessageComposerPopUp(selectedSmsText);
		}
  	}


  	
	public JPopupMenu getPopup() {
		return popup;
	}


	public void setPopup(JPopupMenu popup) {
		this.popup = popup;
	}

}
