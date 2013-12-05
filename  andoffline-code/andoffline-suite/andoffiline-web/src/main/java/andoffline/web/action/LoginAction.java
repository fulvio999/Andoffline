package andoffline.web.action;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Action called when the user execute a successful login
 *
 */
public class LoginAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(LoginAction.class);
	
	/**
	 * Constructor
	 */
	public LoginAction() {
	
	}

	/**
	 * Core method that forward to an Apache Tiles definition
	 */
	public String execute(){
		
		final Authentication authority = SecurityContextHolder.getContext().getAuthentication();  

		String loggedUser = authority.getName();
		
		Map<String, Object> session = ActionContext.getContext().getSession();
    	session.put("loggedUser",loggedUser);
 		
		return SUCCESS;
	}
	
}
