
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
 * DAO object that offer the methods to manage the sms database storing/loading
 *
 */
public class SmsDao extends BaseDao{
	
	 private static final Logger log = Logger.getLogger(SmsDao.class);	
	 
	 private static String INSERT_SMS_QUERY = "INSERT INTO sms (protocol,phone,date,type,subject,body,toa,sc_toa,service_center,smsRead,status,locked) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	 /**
	  * Constructor
	 */
	 public SmsDao() {
		
	 }
	
	  /**
	   *  Insert the data contained in the JTree in argument in the Database type chosen in argument.
	   *  The database structure (if necessary) was previously created.
	   *  
	   * @param dbType The db type (eg mysql, sqlite) chosen
	   * @param treeToSave the JTree with the SMS to store
	   * @param dbConfManager the object containing the configuration for the chosen database
	   * 
	   * @return true if the save operation was executed successfully	   
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
			 log.fatal("Error saving SMS in the "+dbChosen+" cause: ",e);
			 throw e;
			 
		  }finally{
			  if(connection != null){
				 connection.close();  
			  }
		  }
    }

	/**
	 * Utility method that scan the JTree containing the SMS to store. 
	 * Note: the exported content is the one CURRENTLY contained in the JTree in argument 
	 * (ie: if the JTree is filtered will be stored the filtered content)
	 *  
	 * @param tree
	 * @throws SQLException 
	 */
	private void storeData(JTree tree, Connection connection) throws SQLException{	
		
		 log.info("Sms database saving started....");
		 
		 PreparedStatement ps = connection.prepareStatement(INSERT_SMS_QUERY);
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
		            		
		           //true if the current node represents an sms object in the tree    
		           if(currentNode.toString().contains("["))
		           {   			
		          	  DefaultMutableTreeNode smsElement = null;	          	   
		          	  Enumeration<?> smsNodeChildsEnum = currentNode.breadthFirstEnumeration();
		            	 
		          	  int index = 1;
		            	  
		           	  while(smsNodeChildsEnum.hasMoreElements()) 
		    		  {			            		 
		           		  smsElement = (DefaultMutableTreeNode)smsNodeChildsEnum.nextElement();
			           	  String element = smsElement.toString();			            	 	
		            		  
		           		  if(!element.contains("[") && !element.contains("]")) {   //skip first node eg: sms [ x ]
		            			  
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
	   log.info("Sms database saving finished.");	
	}

}
