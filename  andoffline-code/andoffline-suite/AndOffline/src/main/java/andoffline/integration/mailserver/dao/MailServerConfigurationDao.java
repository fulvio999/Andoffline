
package andoffline.integration.mailserver.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Dedicated object to read/write the configuration to use connect with an EXTERNAL mail server
 * for email sending
 *
 */
public class MailServerConfigurationDao {
	
	private static final Logger log = Logger.getLogger(MailServerConfigurationDao.class);
	
	private static String MAIL_SERVER_CONFIG_FILE_NAME = "conf/mail.properties";
	 
	// The header to insert at the beginning on the conf/twitter4j.properties
	private static String HEADER = "# # Configuration parameters to connect with an exixting smtp server (eg: Gmail) for mail sending " + "\n"+" # ******** DON'T EDIT. USE THE DEDICATED MENU FUNCTION !!!! *********"+ "\n";
	 
	//the required param to connect with the mail server
	private String mailServerAddress;
	private String mailServerPort;
	private String mailServerPassword;
	private String mailServerUser;	
	private String mailSenderAddress; //the email address associated at the server account (ie: who send the email)

	/**
	 * Constructor
	 */
	public MailServerConfigurationDao() {
		
	}
	
	/**
	 * Load the configuration stored in conf/twitter4j.properties file
	 * @throws IOException 
	 */
	public void readConfiguration(){		
		
		try{		
			log.info("Reading mail server configuration from: "+MAIL_SERVER_CONFIG_FILE_NAME);
			
			File mailServerConfigFile = new File(MAIL_SERVER_CONFIG_FILE_NAME);		
			FileReader fr = new FileReader(mailServerConfigFile.getAbsolutePath());
			Properties databaseConfig = new Properties();
			databaseConfig.load(fr);
			
			this.mailServerAddress = databaseConfig.getProperty("mailServerAddress");
			this.mailServerPort = databaseConfig.getProperty("mailServerPort");	
			this.mailServerPassword = databaseConfig.getProperty("mailServerPassword");	
			this.mailServerUser = databaseConfig.getProperty("mailServerUser");	
			this.mailSenderAddress = databaseConfig.getProperty("mailSenderAddress");	
		
		}catch (Exception e) {
			log.fatal("Error reading the Mail server configuration, cause: ",e);
		}
	}
	
	/**
	 * Utility method used to check if the loaded values are valid (ie are empty)
	 * @return true if ALL the mandatory configuration values are valid (ie are not empty)
	 */
	public boolean configurationIsValid(){
		
		if(StringUtils.isEmpty(this.mailServerAddress) || StringUtils.isEmpty(this.mailServerPort) || StringUtils.isEmpty(this.mailServerPassword) || StringUtils.isEmpty(this.mailServerUser) || StringUtils.isEmpty(this.mailSenderAddress))
			return false;
		else
			return true;
		
	}
	
	/**
	 * Store the configuration in conf/twitter4j.properties file
	 * 
	 * @param mailServerAddress
	 * @param mailServerPort
	 * @param mailServerPassword
	 * @param mailServerUser
	 * @return true is the save operation was executed successfully
	 */
	public boolean storeConfiguration(String mailServerAddress, String mailServerPort, String mailServerPassword, String mailServerUser, String mailSenderAddress){		
	
		try{		
			File databaseConfigFile = new File(MAIL_SERVER_CONFIG_FILE_NAME);
			FileReader fr = new FileReader(databaseConfigFile.getAbsolutePath());
			Properties databaseConfig = new Properties();
			// load ALL the properties (the ones to be updated and not)
			databaseConfig.load(fr);
			fr.close();
			
			FileWriter fw = new FileWriter(databaseConfigFile.getAbsolutePath());	
			fw.write("# ----------");
			
			// overwrite the old properties with the new edited one 
			databaseConfig.setProperty("mailServerAddress", mailServerAddress);
			databaseConfig.setProperty("mailServerPort", mailServerPort);	
			databaseConfig.setProperty("mailServerPassword", mailServerPassword);
			databaseConfig.setProperty("mailServerUser", mailServerUser);
			databaseConfig.setProperty("mailSenderAddress", mailSenderAddress);
			
			databaseConfig.store(fw, HEADER);
			
			fw.close();
			
			return true;
			
		}catch (Exception e) {	
			
			log.fatal("Error saving Mail server configuration, cause: ",e);
			return false;
		}		
	}

	public String getMailServerAddress() {
		return mailServerAddress;
	}

	public void setMailServerAddress(String mailServerAddress) {
		this.mailServerAddress = mailServerAddress;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public String getMailServerPassword() {
		return mailServerPassword;
	}

	public void setMailServerPassword(String mailServerPassword) {
		this.mailServerPassword = mailServerPassword;
	}

	public String getMailServerUser() {
		return mailServerUser;
	}

	public void setMailServerUser(String mailServerUser) {
		this.mailServerUser = mailServerUser;
	}

	public String getMailSenderAddress() {
		return mailSenderAddress;
	}

	public void setMailSenderAddress(String mailSenderAddress) {
		this.mailSenderAddress = mailSenderAddress;
	}

}
