package andoffline.gui.panel.call;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import andoffline.gui.panel.sms.SmsPanel;

public class CallPanelCustomIconRenderer extends DefaultTreeCellRenderer{
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(CallPanelCustomIconRenderer.class);
 	
    public CallPanelCustomIconRenderer() {
  
    }

    
    /**
     * The core method of the renderer that create custom panel to display for EACH node of the JTree 
     * NOTE: the value read from the JTree are not the original ones coming from the sms, because the parser has customized the labels showed
     *
     */
	public Component getTreeCellRendererComponent(JTree tree, Object value,	boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		
		  // Start with default behavior
	      super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
	        
	      // Necessary for Windows
	      tree.setRowHeight(32);		
		
		  if ((value != null) && (value instanceof DefaultMutableTreeNode)) {

			  DefaultMutableTreeNode obj = (DefaultMutableTreeNode)value;		  
			  
			  TreeNode n = obj.getParent();
			  //The pair call parameter and his value (eg body="hello boy")
			  String smsParamAndValue = null;
			  
			  if(n == null){ //true for the root of the tree
				  setIcon(createImageIcon("root.png","All Calls"));
				  
			  }else if(!obj.isLeaf()){ //set a custom icon for each SMS node
				  
				  smsParamAndValue = ((String)obj.getUserObject()).trim();	
				  setIcon(createImageIcon("phone_call.png",smsParamAndValue));				  		
			  } 
			  else{ //The node is a leaf
				  
				  smsParamAndValue = ((String)obj.getUserObject()).trim();
				  
				  /* 
				     Make some modification at the nodes (ie use custom icons for some nodes)
				     NOTE: the name found is the one converted one by the parser 
				  */
				  if(smsParamAndValue.contains("Total")){  //true if the node is <smses>
					  
					  //Split the value of the tag to obtain only the total sms number
					  String count = smsParamAndValue.split("=")[1];
					  setIcon(createImageIcon("call_count.png",smsParamAndValue.split("=")[0]+": "+count));						  
				  }				 
			  }
		
		  }
		  return this;
		  
   }
		  
	/**
	 * Create the ImageIcon to be shown in the Tree node	 
	 * @param path The image file to use for ImageIcon creation
	 * @return
	 */	 
	protected static ImageIcon createImageIcon(String path, String desc) {
		
        java.net.URL imgURL = SmsPanel.class.getResource("/"+path);
        if (imgURL != null) {        	
            return new ImageIcon(imgURL,desc);
        } else {            
        	log.error("Couldn't find file: " + path);
            return null;
        }
    }


}
