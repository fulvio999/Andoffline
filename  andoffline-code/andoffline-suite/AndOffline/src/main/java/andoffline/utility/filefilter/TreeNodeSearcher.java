
package andoffline.utility.filefilter;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import andoffline.gui.panel.sms.SmsPanelCustomIconRenderer;


/** 
 * Utility class used to search a String between the nodes of a java JTree passed in argument
 *
 */
public class TreeNodeSearcher {
	
	private JTree treeTosearch;

	/**
	 * Cosntructor
	 * @param tree The JTree where search
	 */
	public TreeNodeSearcher(JTree tree) {
		this.treeTosearch = tree;		
	}
	
	/**
	 * Return a list of nodes number containing the search String 
	 * @param stringToSearch The String to search in the JTree
	 * @return
	 */
	public DefaultMutableTreeNode getNodesMatching(String stringToSearch){
		
//		System.out.println("Looking for : "+stringToSearch);
//		
//		//The nodes number containing the searched String
//		ArrayList<String> nodeMatching = new ArrayList<String>();
//		
//		return nodeMatching;
		
		
		TreeNode root = (TreeNode)treeTosearch.getModel().getRoot();  
		
		DefaultMutableTreeNode node = null;
		
		//------------
		 Enumeration child = root.children();
		 
		 while (child.hasMoreElements()) {
			 
			 node = (DefaultMutableTreeNode) child.nextElement();
			 
			 System.out.println("CHILD: "+node.getChildCount()); //34
			 
			 
			 if (node.getChildCount() >= 0) {
			 
			 Enumeration en = node.breadthFirstEnumeration(); 
			 
	        //iterate through the enumeration
	        while(en.hasMoreElements())
	        {
	            //get the node
	            node = (DefaultMutableTreeNode)en.nextElement();
	            SmsPanelCustomIconRenderer cell = (SmsPanelCustomIconRenderer)node.getUserObject();
	            
	           
	            
	           // JPanel p = cell.getCustomNode();
	          
	            
	            //match the string with the user-object of the node
//	            if(nodeStr.equals(node.getUserObject().toString()))
//	            {
//	                //tree node with string found
//	                return node;                         
//	            }
	        }
		
		}
		//------------
		
//	   TreeNode root = (TreeNode)treeTosearch.getModel().getRoot();    
//	        
//		DefaultMutableTreeNode node = null;
//		
//		 Enumeration child = root.children();
//		 while (child.hasMoreElements()) {
//			
//			 node = (DefaultMutableTreeNode) child.nextElement();		 
//			
//			 System.out.println("CHILD: "+node.getChildCount()); //34
//			 
//			 if (node.getChildCount() >= 0) {
			 
//			 for (Enumeration e=node.children(); e.hasMoreElements(); ) {
//				 
//	                TreeNode n = (TreeNode)e.nextElement();
//	                n.get
	                
	                
//	                TreePath path = parent.pathByAddingChild(n);
//	                TreePath result = find2(tree, path, nodes, depth+1, byName);
	                // Found a match
//	                if (result != null) {
//	                    return result;
//	                }
//	            }
//			 } 
//			 

		 }
		
//		DefaultMutableTreeNode node = null;
//	    Enumeration e = treeTosearch.getT
//	    while (e.hasMoreElements()) {
//	      node = (DefaultMutableTreeNode) e.nextElement();
//	      if (nodeStr.equals(node.getUserObject().toString())) {
//	        return node;
//	      }
//	    }
	    return null;
		
	}

}
