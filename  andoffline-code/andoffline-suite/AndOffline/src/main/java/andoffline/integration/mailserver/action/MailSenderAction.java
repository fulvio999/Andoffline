
package andoffline.integration.mailserver.action;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import andoffline.integration.mailserver.dao.MailServerConfigurationDao;

/**
 * Action called from the GUI to send an email using the configured mail account
 * It uses the dedicated DAO object to load the necessary configuration to connect with a mail server  
 */
public class MailSenderAction {
	
	private static final Logger log = Logger.getLogger(MailSenderAction.class);

	/**
	 * Constructor
	 */
	public MailSenderAction() {
		
	}
	
	/**
	 * Sends the obtained statistics as email attachment using Apache Commons Mail Library (http://commons.apache.org/email/)
	 * 
	 * @param mailSubject
	 * @param mailTextMsg
	 * @param mailFrom
	 * @param mailTo
	 * @throws Exception 
	 *  
	 * @throws EmailException 
	 * @throws ParseException 
	 */
	public void send(String mailSubject, String mailTextMsg, String mailFrom, String mailTo) throws Exception {
		
		try{			
			log.info("--- Sending the email: ---");
			log.info("- subject: :"+mailSubject);
			log.info("- text: "+mailTextMsg);
			log.info("- from: "+mailFrom);
			log.info("- to: "+mailTo);
			log.info("--------------------------");
			
			MailServerConfigurationDao mailServerConfigurationDao = new MailServerConfigurationDao();
			mailServerConfigurationDao.readConfiguration();
		
		    /**
		     * TODO Send an email with an attachment
		     */			
			
		   /**
		    * Send a mail without attachment (note: to send an email with attachments is also necessary include javax.mail jar)
		    * Load from the configuration file placed in the "conf" folder the necessary parameters to connect with a mail server
		    */
		   Email simpleEmail = new SimpleEmail();
		  
		   simpleEmail.setHostName(mailServerConfigurationDao.getMailServerAddress());
		   simpleEmail.setSmtpPort(Integer.parseInt(mailServerConfigurationDao.getMailServerPort()));
		   simpleEmail.setAuthenticator(new DefaultAuthenticator(mailServerConfigurationDao.getMailServerUser(), mailServerConfigurationDao.getMailServerPassword()));
		   simpleEmail.setSSL(true);
		   simpleEmail.setFrom(mailFrom);
		   simpleEmail.setSubject(mailSubject);
		   simpleEmail.setMsg(mailTextMsg);
		   simpleEmail.addTo(mailTo);
		   simpleEmail.send();
		  
		  log.info("Email sent successfully !");
		  
		}catch (Exception e) {		
			
			log.fatal("Error sending the email: ",e);	
			throw e;
		}
		
	}

}
