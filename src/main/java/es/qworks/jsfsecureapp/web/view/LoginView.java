package es.qworks.jsfsecureapp.web.view;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletRequest;
import javax.validation.constraints.Pattern;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.qworks.jsfsecureapp.service.UserLoginService;
import es.qworks.jsfsecureapp.service.UserLoginService.LoginStatus;
import es.qworks.jsfsecureapp.web.security.exception.MustChangePasswordException;

/**
 * @author jmgd6647
 *
 */
/**
 * 
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 */
@Named
@ViewScoped
public class LoginView extends AbstractBackingBean {
	
	private static final long serialVersionUID = 5962774595677594374L;
	
	private static Logger LOG = LoggerFactory.getLogger(LoginView.class);
	
	private Boolean rememberMe;
	private String username;
	private String password;
	@Pattern(regexp=UserLoginService.PASSWORD_REGEX)
	private String newPassword;
	private String newPasswordVerification;
	private Boolean changePassword = false;
	private String authSession;

	@Inject
	private UserLoginService userLoginService;
	
	/**
	 * 
	 */
	public LoginView() {
		super();
	}
	
	/**
	 * 
	 */
	public void onLoad() {
		LOG.debug("Login bean loaded!!");
	}
		
	/**
	 * @return 
	 */
	public String actionLogin() {
		
		String targetUrl = Views.INDEX_VIEW;
		
		try {
			LOG.debug("Start login of {}", username);
			SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, rememberMe));
            SavedRequest savedRequest = WebUtils.getAndClearSavedRequest( (ServletRequest) getExternalContext().getRequest() );
            if (savedRequest != null) {
            	targetUrl = savedRequest.getRequestUrl();
            }
            sendMessage(FacesMessage.SEVERITY_INFO,  "Welcome, " + username);
            redirectTo( targetUrl );
        }
		catch (MustChangePasswordException e) {
			changePassword = true;
			authSession = e.getAuthSession();
			LOG.debug("User {} must change password", username);
			sendMessage(FacesMessage.SEVERITY_WARN, "Current password must be changed");
			return "";
		}
        catch (AuthenticationException e) {
        	LOG.error(e.getMessage());
    		sendMessage(FacesMessage.SEVERITY_ERROR, "Login failed for user " + username);
            return null;
        }
	    
	    // Old non-Shiro programmatic login 
//	    LoginStatus res  = userLoginService.login(username, password.toCharArray());
//        if (res == LoginStatus.LOGIN_OK) {
//    		msg = "Successful login for user " + username;
//    		
//    		SavedRequest savedRequest = WebUtils.getAndClearSavedRequest( (ServletRequest) getExternalContext().getRequest() );
//          redirectTo( savedRequest != null ? savedRequest.getRequestUrl() : Views.INDEX_VIEW );
//    	}
//    	else if ((res == LoginStatus.USER_MUST_CHANGE_PASSWORD )) {
//    		msg = "User " + username + " must change password";
//    		changePassword = true;
//    		authSession = res.getSession();
//    		LOG.debug("User must change password");
//    		sendMessage(FacesMessage.SEVERITY_WARN, msg);
//    	}
//    	else {
//    		msg = "User " + username + " couldn't log in";
//    		LOG.debug(msg);
//    		sendMessage(FacesMessage.SEVERITY_ERROR, msg);
//    	}
         
        return "";
    }

	
	/**
	 * @return 
	 */
	public String actionChangePassword() {
		boolean loggedIn = false;
		LoginStatus ret  = null;
	    String msg = null;
	    
        loggedIn = false;
        try {
	        ret = userLoginService.changeUserPassword(authSession, username, password, newPassword);
	        if (ret == LoginStatus.LOGIN_OK) {
	        	loggedIn = true;
	        	authSession = ret.getSession();
	        	msg = "User " + username + " is logged in";
	    		sendMessage(FacesMessage.SEVERITY_INFO, msg);
	    		redirectTo(Views.INDEX_VIEW);
	    	}
	    	else {
	    		loggedIn = false;
	    		msg = "User " + username + " couldn't login in";
	    		sendMessage(FacesMessage.SEVERITY_ERROR, msg);
	    	}
        }
        catch (Exception e) {
        	LOG.debug("{}: {}", e, e.getMessage());
			return null;
        }
        finally {
        	sendMessage(FacesMessage.SEVERITY_INFO, msg);
        	PrimeFaces.current().ajax().addCallbackParam("loggedIn", loggedIn);
        }
        
        return Views.INDEX_VIEW;
    }
	
	
	/**
	 * 
	 */
	public void actionLogout() {
		LOG.info("Logging out user {}", username);
		SecurityUtils.getSubject().logout();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		userLoginService.logout();
		redirectTo(Views.INDEX_VIEW + "?faces-redirect=true");
	}

	
	/**
	 * @param fc
	 * @param component
	 * @param value
	 * @throws ValidatorException
	 */
	public void validatePassword(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
		if (value == null) {
            return;
        }
		String password = value.toString();
		
		if (!password.matches(UserLoginService.PASSWORD_REGEX)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password too weak", "It musts be at least " + UserLoginService.PASSWD_MIN_LENGTH + " characters long and\n must contain number(s) and capital letter(s)");
			throw new ValidatorException(message);
		}
	}
	
	/**
	 * @param fc
	 * @param component
	 * @param value
	 * @throws ValidatorException
	 */
	public void validateRepeatPassword(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
		if (value == null) {
            return;
        }
		String password = value.toString();
		
		if (!password.equals(newPasswordVerification)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match",  null);
			throw new ValidatorException(message);
		}
		
	}
		
	/**
	 * @return the rememberMe
	 */
	public Boolean getRememberMe() {
		return rememberMe;
	}

	/**
	 * @param rememberMe the rememberMe to set
	 */
	public void setRememberMe(Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the newPasswordVerification
	 */
	public String getNewPasswordVerification() {
		return newPasswordVerification;
	}

	/**
	 * @param newPasswordVerification the newPasswordVerification to set
	 */
	public void setNewPasswordVerification(String newPasswordVerification) {
		this.newPasswordVerification = newPasswordVerification;
	}

	/**
	 * @return the changePassword
	 */
	public Boolean getChangePassword() {
		return changePassword;
	}

	/**
	 * @param changePassword the changePassword to set
	 */
	public void setChangePassword(Boolean changePassword) {
		this.changePassword = changePassword;
	}


}

