package andoffline.integration.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;


/**
 *  DAO object that offer the methods to manage the call database storing/loading
 *
 */
public class CallDao extends BaseDao{
	
     private static final Logger log = Logger.getLogger(CallDao.class);	
    
     private static String INSERT_CALL_QUERY = "INSERT INTO calls (phone,duration,call_date,call_type) VALUES (?,?,?,?)";
	 
	 /* the tree with the Call to store in the configured DBMS */
	 private JTree tree;

	/**
	 * Constructor
	 */
	public CallDao() {
		
	}
	
	  /**
	   * @param dbType The db type (eg mysql, sqlite)
	   * @param treeToSave the JTree with the content to store
	   *
	   * @return true if the store operation was executed successfully
	   * @throws Exception 
	   */
	  public boolean saveData(String dbChosen,JTree treeToSave) throws Exception {
		  
		  Connection connection = null;		  
		  try{     
			   connection = this.getConnection(dbChosen);	
		       if(connection != null){
		    	  this.storeData(treeToSave,connection);
		        }
		       return true;
		    
		  }catch (Exception e) {
			 log.fatal("Error saving Contacts in the "+dbChosen+" cause: ",e);
			 throw e;
			 
		  }finally{
			  if(connection != null){
				 connection.close();  
			  }
		  }

	 }
		 
   /**
	* Utility method that scan the JTree containing the Call to store. 
	* Note: the exported content are the one currently contained in the JTree (that can be filtered or not)
	*  
	* @param tree
	* @throws SQLException 
	*/
	private void storeData(JTree tree,Connection connection) throws SQLException{	
				
		log.info("Calls database saving started....");
				 
		PreparedStatement ps = connection.prepareStatement(INSERT_CALL_QUERY);
				 
		TreeNode treeRoot = (TreeNode)tree.getModel().getRoot();  			
		DefaultMutableTreeNode currentNode = null;				
		Enumeration<?> child = treeRoot.children();	    
					
		while (child.hasMoreElements())
		{			 
			currentNode = (DefaultMutableTreeNode) child.nextElement();
						
			//get only the nodes with at least one child
		    if (currentNode.getChildCount() > 0) {
						 
			    Enumeration<?> childEnum = currentNode.breadthFirstEnumeration(); 
							 
				//iterate through the enumeration
				while(childEnum.hasMoreElements())
				{			           
				   currentNode = (DefaultMutableTreeNode)childEnum.nextElement();
				            		
				   //true if the current node represents a call object in the tree    
				   if(currentNode.toString().contains("["))
				   {   			
				    	DefaultMutableTreeNode smsElement = null;	       	   
				       	Enumeration<?> smsNodeChildsEnum = currentNode.breadthFirstEnumeration();
				            	 
				        int index = 1;
				            	  
				        while(smsNodeChildsEnum.hasMoreElements()) 
				    	  {			            		 
				           	  smsElement = (DefaultMutableTreeNode)smsNodeChildsEnum.nextElement();
					          String element = smsElement.toString();			            	 	
				            		  
				           	  if(!element.contains("[") && !element.contains("]")) {   //skip first node eg: call [ x ]
				            			  
				           		 String[] tokens = element.split("=");
				            	 log.trace("Position: "+index+" Element to insert: "+tokens[1].trim());
				            	 ps.setString(index,tokens[1].trim()); 	 
				            			 
				            	 index ++;
				               }	 		  
				           	}				              
				              ps.executeUpdate();		 
				          }
					   }      
				    }		 
				}		
		 ps.close();
		 log.info("Calls database saving finished.");	
	}
	

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

}
