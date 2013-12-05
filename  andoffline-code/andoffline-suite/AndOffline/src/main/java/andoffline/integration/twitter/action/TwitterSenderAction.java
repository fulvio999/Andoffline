package andoffline.integration.twitter.action;

import org.apache.log4j.Logger;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import andoffline.integration.twitter.dao.TwitterConfigurationDao;

/**
 * Class with methods to interacts  with Twitter.
 * It uses the dedicated DAO object to load the necessary configuration to connect with Twitter  
 * 
 * See: http://twitter4j.org/en/code-examples.html#directMessage    (Section: "OAuth support")
 * 
 * 1) Register your application at https://twitter.com/oauth_clients/new
 * to acquire consumer key, and consumer secret in advance
 *  
 * 2) follow the steps provided by twitter. At the end you'll get the "OAuth settings":
 * Consumer key
 * Consumer secret
 * 
 * and the access token to sign requests with your own Twitter account
 * 
 * NOTE; This key should never be human-readable in your application
 * NOTE: Remember that the registered application MUSTN'T be read-only
 *
 *--------------------------------------------------------------------
 * To use OAuth, an application must:
 *
 *   Obtain access tokens to act on behalf of a user account.
 *   Authorize all HTTP requests it sends to Twitter's APIs.
 *
 * Use the access token string as your "oauth_token" and the access 
 * token secret as your "oauth_token_secret" to sign requests with your own Twitter account. 
 * Do not share your oauth_token_secret with anyone. 
 * --------------------------------------------------------------------
 */
public class TwitterSenderAction {
	
	 private static final Logger log = Logger.getLogger(TwitterSenderAction.class);

	/**
	 * constructor
	 */
	public TwitterSenderAction() {
		
	}
	
	/**
	 * Send a tweet
	 * 
	 * @return
	 * @throws Exception 
	 */
	public boolean send(String twitMessage) throws Exception{
		
		try{			
			log.info("Sending message to Twitter...");
			
			//read the Twitter configuration from the configuration file
			TwitterConfigurationDao twitterConfigurationDao = new TwitterConfigurationDao();
			twitterConfigurationDao.readConfiguration();		
			
			ConfigurationBuilder cb = new ConfigurationBuilder();			
			cb.setDebugEnabled(true);
			
			cb.setOAuthConsumerKey(twitterConfigurationDao.getOauthConsumerKey());
			cb.setOAuthConsumerSecret(twitterConfigurationDao.getOauthConsumerSecret());
			cb.setOAuthAccessToken(twitterConfigurationDao.getOauthAccessToken());
			cb.setOAuthAccessTokenSecret(twitterConfigurationDao.getOauthAccessTokenSecret());			
			
			TwitterFactory tf = new TwitterFactory(cb.build());	
			Twitter twitter = tf.getInstance();
			
			/* Send a Twit on the home page of the account that own the OAUTH authentication token */
			Status status = twitter.updateStatus(twitMessage);			
			log.info("Tweet successfully sent");		
			
		}catch (Exception e) {
			
			log.fatal("Error sending message to Twitter, cause: ",e);
			throw e;
		}
		
		return true;
		
	}

}
