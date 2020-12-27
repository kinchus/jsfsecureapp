package es.qworks.jsfsecureapp.web.security;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.apache.shiro.authc.Account;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.qworks.jsfsecureapp.model.User;
import es.qworks.jsfsecureapp.service.UserLoginService;
import es.qworks.jsfsecureapp.service.UserLoginService.LoginStatus;
import es.qworks.jsfsecureapp.service.exception.ServiceError;
import es.qworks.jsfsecureapp.web.security.exception.MustChangePasswordException;


/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
@SessionScoped
public class FormRealm extends AuthenticatingRealm implements Serializable {

	private static final long serialVersionUID = 4276133837693362209L;
	private static final Logger LOG = LoggerFactory.getLogger(FormRealm.class);
	private static final String REALM_NAME = "FormRealm";
	
	@Inject
    private UserLoginService loginService;

    
	/**
	 * 
	 */
	public FormRealm() {
		setName(REALM_NAME);
		setCredentialsMatcher(new SimpleCredentialsMatcher());
	}

    /**
     *
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof UsernamePasswordToken;
    }

    /**
     *
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        
    	UsernamePasswordToken upToken = (UsernamePasswordToken) token;
    	
        try {
			LoginStatus res = getUserLoginService().login(upToken.getUsername(), upToken.getPassword());
			if (res == LoginStatus.LOGIN_OK) {
				LOG.debug("Login successful for user {}", upToken.getUsername());
				return getLoggedUserAccount(upToken.getCredentials());
			}
			else if (res == LoginStatus.USER_MUST_CHANGE_PASSWORD) {
				LOG.debug("User {} must change password", upToken.getUsername());
				throw new MustChangePasswordException(upToken.getUsername(), res.getSession());
			}
			else {
				LOG.debug("Login failed for user {}", upToken.getUsername());
				throw new AuthenticationException("Authentication failed for user " + upToken.getUsername());
			}
			
		} catch (ServiceError e) {
			LOG.debug("Login failed for user {}", upToken.getUsername());
			throw new AuthenticationException("Authentication failed for user " + upToken.getUsername(), e);
		}

        // AuthorizationInfo info = realm.getAuthorizationInfo(subject);
        // return new SimpleAccount(upToken.getPrincipal().toString(), upToken.getCredentials(), getName());
    }
    
    private Account getLoggedUserAccount(Object credentials) {
    	Account ret = null;
    	try {
    		User user = getUserLoginService().getLoggedUser();
			Set<String> roles = new HashSet<String>();
			user.getRoles().forEach(role -> roles.add(role.getName()));
			Set<Permission> permissions = new HashSet<Permission>();
			ret = new SimpleAccount(user, credentials, getName(), roles, permissions);
		} catch (ServiceError e) {
			e.printStackTrace();
		}
		
		return ret;
    }
    
    /**
	 * @return the userLoginService
	 */
	private UserLoginService getUserLoginService() throws ServiceError {
		if (loginService == null) {
			loginService = javax.enterprise.inject.spi.CDI.current().select(UserLoginService.class).get();
		}
		if (loginService == null) {
			throw new ServiceError("Couldn't get UserLoginService Bean!!");
		}
		return loginService;
	}

}