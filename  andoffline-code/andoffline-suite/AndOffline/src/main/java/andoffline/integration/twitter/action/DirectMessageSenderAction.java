
package andoffline.integration.twitter.action;

import org.apache.log4j.Logger;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import andoffline.integration.twitter.dao.TwitterConfigurationDao;

/**
 * Instead of a Tweet is possible send a "direct message" to a my follower (or to myself).
 * A "direct message" is an email sent to the email address of the chosen follower. 
 * 
 * NOTE: If the destination is not a follower of the user account used to connect to Twitter
 * is not possible sent a "direct message"
 *
 */
public class DirectMessageSenderAction {
	
	private static final Logger log = Logger.getLogger(DirectMessageSenderAction.class);

	/**
	 * constructor
	 */
	public DirectMessageSenderAction() {
		
	}	
	
	/**
	 * Send a Direct message (ie a message sent directly to mail address of a follower)
	 * @param twitterMessage The message to send at the follower as "direct message" 
	 * @param followerName   The name of the destination follower 
	 * 
	 * @return
	 * @throws Exception 
	 */
	public boolean send(String twitterMessage, String followerName) throws Exception{
		
		try{			
			log.info("Sending Direct message to my follower name: "+followerName);
			
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
			
			DirectMessage message = twitter.sendDirectMessage(followerName, twitterMessage);
			log.info("Direct message successfully sent to " + message.getRecipientScreenName());
						
		}catch (Exception e) {
			
			log.fatal("Error sending Twitter direct message, cause: ",e);
			throw e;
		}
		
		return true;
		
	}

}
