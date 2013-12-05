
package andoffline.integration.database.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import andoffline.gui.common.utility.VcfTranslatorUtils;
import andoffline.parser.vcf.VcardEntry;
import andoffline.parser.vcf.VcardRecord;


/**
 *  DAO object that offer the methods to manage the Contact database storing/loading
 *
 */
public class ContactDao extends BaseDao{
	
     private static final Logger log = Logger.getLogger(ContactDao.class);	
     
     private static String INSERT_CONTACT_QUERY = "INSERT INTO contact (contactRecord,contactKey,contactValue) VALUES (?,?,?)";
	 
	 /* the contacts to store in the configured DBMS */
	 private  List<VcardRecord> vcardRecordList;

	/**
	 * Constructor
	 */
	public ContactDao() {
		
	}
	
	
	/**
	   * @param dbType The db type (eg mysql, sqlite)
	   * @param vcardRecordList A list of contact built from a vcf file  
	   * 
	   * @return true if the store operation was executed successfully
	   * @throws Exception 
	   */
	  public boolean saveData(String dbChosen, List<VcardRecord> vcardRecordList) throws Exception {
		  
		  Connection connection = null;		  
		  try{     
			  connection = this.getConnection(dbChosen);	
		       if(connection != null){
		    	  this.storeData(vcardRecordList,connection);
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
	* Utility method that scan the JTree containing the SMS to store. 
	* Note: the exported content are the one currently contained in the JTree (that can be filtered or not)
	*  
	* @param tree
	* @throws SQLException 
	*/
	private void storeData(List<VcardRecord> vcardRecordList,Connection connection) throws SQLException{	
			
		log.info("Saving total: "+vcardRecordList.size()+" contacts to database started....");
				 
		PreparedStatement ps = connection.prepareStatement(INSERT_CONTACT_QUERY);
			 
		for(int i=0;i<vcardRecordList.size();i++)
		{
			ps.setInt(1,i+1); 
			
			List<VcardEntry> recordList = (vcardRecordList.get(i)).getVcardEntry();
			for (int j=0; j< recordList.size();j++)
			{	
				//convert to Human label (eg N ---> Name)
				String labelToDisplay = VcfTranslatorUtils.conversionNameTable.get(recordList.get(j).getType());
				
				//in case of there is no translation 
				if (labelToDisplay == null)
					  labelToDisplay = recordList.get(j).getType();
				 
				log.trace("TYPE: "+labelToDisplay.trim());
				log.trace("VALUE: "+recordList.get(j).getValue().trim());
				
				ps.setString(2,labelToDisplay); 
				ps.setString(3,recordList.get(j).getValue()); 
				 
				ps.executeUpdate();					
			}			
		 }
		 
		log.info("Saving contacts to database finished.");		    
	}


	public List<VcardRecord> getVcardRecordList() {
		return vcardRecordList;
	}


	public void setVcardRecordList(List<VcardRecord> vcardRecordList) {
		this.vcardRecordList = vcardRecordList;
	}

}
