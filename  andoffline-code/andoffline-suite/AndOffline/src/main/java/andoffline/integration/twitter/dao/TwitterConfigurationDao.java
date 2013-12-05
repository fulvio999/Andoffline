package andoffline.integration.twitter.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



/**
 * Utility class that read/write from a properties file the informations necessary to connect with Twitter
 * The properties file must be placed in the folder "conf" and named "twitter4j.properties"
 *
 */
public class TwitterConfigurationDao {
	
	 private static final Logger log = Logger.getLogger(TwitterConfigurationDao.class);
	
	 private static String TWITTER_CONFIG_FILE_NAME = "conf/twitter4j.properties";
	 
	 // The header to insert at the beginning on the conf/twitter4j.properties
	 private static String HEADER = "# Configuration file for Twitter4j framework " + "\n"+" # ******** DON'T EDIT. USED THE DEDICATED MEU FUNTION !!!! *********"+ "\n";
	 
	 //the two required param to use the OAUTH authentication (obtained after the registration on Twitterr site)
	 private String oauthConsumerKey;
	 private String oauthConsumerSecret;
	 
	 private String oauthAccessToken;
	 private String oauthAccessTokenSecret;

	/**
	 * Constructor
	 */
	public TwitterConfigurationDao() {
		
	}
	
	/**
	 * Load the configuration stored in conf/twitter4j.properties file
	 * @throws IOException 
	 */
	public void readConfiguration() {
		
		try{		
			log.info("Reading Tweetter configuration from: "+TWITTER_CONFIG_FILE_NAME);
			
		    File databaseConfigFile = new File(TWITTER_CONFIG_FILE_NAME);
			FileReader fr = new FileReader(databaseConfigFile.getAbsolutePath());
			Properties databaseConfig = new Properties();
			databaseConfig.load(fr);
			
			this.oauthConsumerKey = databaseConfig.getProperty("oauthConsumerKey");
			this.oauthConsumerSecret = databaseConfig.getProperty("oauthConsumerSecret");
			
			this.oauthAccessToken = databaseConfig.getProperty("oauthAccessToken");
			this.oauthAccessTokenSecret = databaseConfig.getProperty("oauthAccessTokenSecret");
		
		}catch (Exception e) {
		    log.fatal("Error reading the Twitter configuration, cause: ",e);
		}
	}
	
	/**
	 * Store the configuration in conf/twitter4j.properties file
	 * @param oauthConsumerKey
	 * @param oauthConsumerSecret
	 * @return true is the save operation was executed successfully
	 */
	public boolean storeConfiguration(String oauthConsumerKey, String oauthConsumerSecret, String oauthAccessToken, String oauthAccessTokenSecret){		
		
		try{		
			File databaseConfigFile = new File(TWITTER_CONFIG_FILE_NAME);
			FileReader fr = new FileReader(databaseConfigFile.getAbsolutePath());
			Properties databaseConfig = new Properties();
			// load ALL the properties (the ones to be updated and not)
			databaseConfig.load(fr);
			fr.close();
			
			FileWriter fw = new FileWriter(databaseConfigFile.getAbsolutePath());	
			fw.write("# ----------");
			
			// overwrite the old properties with the new edited one 
			databaseConfig.setProperty("oauthConsumerKey", oauthConsumerKey);
			databaseConfig.setProperty("oauthConsumerSecret", oauthConsumerSecret);	
			
			databaseConfig.setProperty("oauthAccessToken", oauthAccessToken);
			databaseConfig.setProperty("oauthAaccessTokenSecret", oauthAccessTokenSecret);
			
			databaseConfig.store(fw, HEADER);
			
			fw.close();
			
			return true;
			
		}catch (Exception e) {	
			log.fatal("Error saving twitter configuration file: "+e.getMessage());
			return false;
		}		
	}


	/**
	 * Utility method used to check if the loaded values are valid (ie are empty)
	 * @return true if ALL the mandatory configuration values are valid (ie are not empty)
	 */
	public boolean configurationIsValid(){
		
		if(StringUtils.isEmpty(this.oauthConsumerKey) || StringUtils.isEmpty(this.oauthConsumerSecret) || StringUtils.isEmpty(this.oauthAccessToken) || StringUtils.isEmpty(this.oauthAccessTokenSecret))
			return false;
		else
			return true;
		
	}
	
	
	public String getOauthConsumerKey() {
		return oauthConsumerKey;
	}

	public void setOauthConsumerKey(String oauthConsumerKey) {
		this.oauthConsumerKey = oauthConsumerKey;
	}

	public String getOauthConsumerSecret() {
		return oauthConsumerSecret;
	}

	public void setOauthConsumerSecret(String oauthConsumerSecret) {
		this.oauthConsumerSecret = oauthConsumerSecret;
	}

	public String getOauthAccessToken() {
		return oauthAccessToken;
	}

	public void setOauthAccessToken(String oauthAccessToken) {
		this.oauthAccessToken = oauthAccessToken;
	}

	public String getOauthAccessTokenSecret() {
		return oauthAccessTokenSecret;
	}

	public void setOauthAccessTokenSecret(String oauthAccessTokenSecret) {
		this.oauthAccessTokenSecret = oauthAccessTokenSecret;
	}

}
