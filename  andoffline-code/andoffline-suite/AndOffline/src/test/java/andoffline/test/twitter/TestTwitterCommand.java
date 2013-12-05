package andoffline.test.twitter;

import java.io.IOException;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Twitter integration class that uses Twitter4j framework sending a test message
 *
 */
public class TestTwitterCommand {	
    
    /**
     * Send the sms text in input to Twitter
     * 
     * @param smsToSend
     * @throws TwitterException
     */
	public static void main(String[] args) throws IOException {	
		
		  String smsToSend = "Hello World !!!";
		  
		  try {	    	
			  
//			  ConfigurationBuilder cb = new ConfigurationBuilder();
//			  cb.setUser("fulvius999");
//			  cb.setPassword("Qelpdt01");
//			  
//			  TwitterFactory tf = new TwitterFactory(cb.build());
			  
	    	  // The factory instance is re-usable and thread safe.
//	          Twitter twitter = tf.getInstance();
	          
			  
			  // step 1: acquire access token
			  
	          Twitter twitter = new TwitterFactory().getInstance();
	          
	          RequestToken requestToken = twitter.getOAuthRequestToken("callbackURL.toString()");
	          
			  //DirectMessage message = twitter.sendDirectMessage("fulvius999", smsToSend);			
					  
			  //System.out.println("Direct message successfully sent to " + message.getRecipientScreenName());
			  
		} catch (TwitterException e) {
			
			System.out.println("Error sending the test message, cause: " + e.toString());
		}    	
    }
 
}
