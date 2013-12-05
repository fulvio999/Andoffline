package andoffline.test.twitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TestOuth {

	/**
	 * With OAuth authorization scheme, an application can access the user account without userid/password combination given
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception{
		
		
		//INTO OAUTH: http://hueniverse.com/oauth/
		/**
		 * OAuth provides a method for users to grant third-party access to their resources without sharing their passwords
		 * Rispetto ad architettura Client-Server OAuth introduces a third role to this model: the resource owner
		 * Il client non è detto che sia il proprietario della risorsa, può essere qualunque agente a cui il proprietario della risorsa 
		 * ha concesso l'uso della sua risorsa privata.
		 * La risorsa da usare è ospitata dal server (quindi il server è colui che ospita la risorsa).
		 * 
		 * In order for the client to access resources, it first has to obtain permission from the resource owner. 
		 * This permission is expressed in the form of a token and matching shared-secret: tali token non sono specifici del proprietario della risorsa
		 * 
		 * Usando tale token si evita che il proprietario della risorsa debba condividere le SUE credenziali con il client che deve usare
		 * la risorsa 
		 * 
		 * 
		 * SEE: http://hueniverse.com/oauth/guide/workflow/
		 */
		
		//OAUTH: It provides a much needed solution for security web APIs without requiring users to share their usernames and passwords
		
	    // The factory instance is re-usable and thread safe.
		
		//Basic authentication is not supported !!!!
		//See: https://dev.twitter.com/docs/auth/moving-from-basic-auth-to-oauth
		
		//step 1: acquire user authorization
		
		
		//Con Oauth non servono pù user e pw per accesso al mio account
		//Quindi per ogni request devo poter identificare l'utente che la esegue e l'applicazione a cui appartiene
		//Ad ogni request invio anche un token di accesso che identifica l'utente 
		//A tal fine devo:
		// 1) registrare la mia applicazione presso http://twitter.com/oauth_clients/new per ottenere 
		//una due key "consumer key" e una "consumer secret" che dovrò poi fornire a Twitter4j
		// 2) ottenere un "Access token" il quale una volta ottenuto posso salvarlo da qualche parte perchè non è soggetto a scadenza
		
		
		
		// Obtain a RequestToken form Twitter		
	    Twitter twitter = new TwitterFactory().getInstance();
	    twitter.setOAuthConsumer("[consumer key]", "[consumer secret]");
	    RequestToken requestToken = twitter.getOAuthRequestToken();
	    AccessToken accessToken = null;
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    
	    while (null == accessToken) {
	    	
	      System.out.println("Open the following URL and grant access to your account:");
	      System.out.println(requestToken.getAuthorizationURL());
	      System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
	      String pin = br.readLine();
	      
	      try{
	         if(pin.length() > 0){
	           accessToken = twitter.getOAuthAccessToken(requestToken, pin);
	         }else{
	           accessToken = twitter.getOAuthAccessToken();
	         }
	      } catch (TwitterException te) {
	    	  
	        if(401 == te.getStatusCode()){
	          System.out.println("Unable to get the access token.");
	        }else{
	          te.printStackTrace();
	        }
	      }
	    }
	    //persist to the accessToken for future reference.
	    storeAccessToken(twitter.verifyCredentials().getId() , accessToken);
	    Status status = twitter.updateStatus(args[0]);
	    System.out.println("Successfully updated the status to [" + status.getText() + "].");
	    System.exit(0);
	    
	  }
	  private static void storeAccessToken(long useId, AccessToken accessToken){
	    //store accessToken.getToken()
	    //store accessToken.getTokenSecret()
	  }

	
}
