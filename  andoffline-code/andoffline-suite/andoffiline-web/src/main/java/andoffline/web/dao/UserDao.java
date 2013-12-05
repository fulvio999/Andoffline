
package andoffline.web.dao;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * DAO used by Spring Security as "authentication-provider".
 * 
 * It read the user informations from a file named "users.properties" placed in the 
 * "conf" folder of the application installation dir.
 *
 */
public class UserDao implements UserDetailsService {
	
	private static final Logger log = Logger.getLogger(UserDao.class);	
	
	/**
	 * Constructor
	 */
	public UserDao() {
		
	}
	
    /**
     * dummy solution to load the user account from  an external properties file:
     * Spring 'AuthenticationProvider' authenticates the 'username' simply by
     * comparing the password submitted in a UsernamePasswordAuthenticationToken against the one loaded by
     * the UserDetailsService (ie the UserDetails object returned by this method).
     */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		
		log.info("Getting access details from UserDao, for user: "+username);
			
		String password = "";
		String role = "";
		
		try{			
			//eg: /home/fulvio/AndOffline/web-app/apache-tomcat
			String path = System.getProperty("catalina.base");
					
			String fullPath = path+File.separator+"conf"+File.separator+"andoffline"+File.separator+"users.properties";
			log.info("users.properties file full path: "+fullPath);
			
			FileReader fr = new FileReader(fullPath);
			  
			Properties usersProperties = new Properties();
			usersProperties.load(fr);
			
			String loadedUser = usersProperties.getProperty(username);
			
			if(StringUtils.isEmpty(loadedUser)){
				log.fatal("Username "+username+" not found in users.properties file");				
				
				//return a "fake" user without no role
				UserDetails unknownUser = new User(username, "",true,true,true,true, new ArrayList<GrantedAuthority>());
				return unknownUser;
				
			}else{
				log.info("User found in users.properties file");
				String[] passwordAndRole = loadedUser.split(",");
				/* AuthenticationProvider will simply compare the provided password with this one read form file */
				password = passwordAndRole[0];
				role = passwordAndRole[1];
			}
			
		}catch (Exception e) {
		   log.fatal("Error reading from users.properties or locating it, cause: ",e);
		}
		
		// Ideally it should be fetched from database and populated instance of 'User' class		
		ArrayList<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();
		ga.add(new GrantedAuthorityImpl(role));
		
		// (username, password,  enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, Collection<? extends GrantedAuthority> authorities) 
		UserDetails user = new User(username, password,true,true,true,true, ga);
		
		return user;		
	}


}
