package es.qworks.jsfsecureapp.service.impl;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.cognitoidp.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;

import es.qworks.jsfsecureapp.aws.AWSCognitoService;
import es.qworks.jsfsecureapp.aws.exception.AwsException;
import es.qworks.jsfsecureapp.aws.exception.cognito.MustChangePasswordException;
import es.qworks.jsfsecureapp.aws.exception.cognito.UnsupportedChallengeException;
import es.qworks.jsfsecureapp.dao.UserDao;
import es.qworks.jsfsecureapp.model.User;
import es.qworks.jsfsecureapp.service.UserLoginService;
import es.qworks.jsfsecureapp.service.mapper.ServiceMapper;
import es.qworks.jsfsecureapp.web.beans.annotations.LoggedUser;


/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
@SessionScoped
public class UserLoginServiceImpl implements UserLoginService {

	private static final long serialVersionUID = -6746131196709358679L;
	private static final Logger LOG = LoggerFactory.getLogger(UserLoginServiceImpl.class);

	@Inject 
	private AWSCognitoService awsCognito;
	@Inject 
	private UserDao	userDao;
	private User currentUser = null;

	
	/**
	 * @return
	 * @see es.qworks.jsfsecureapp.service.UserLoginService#getLoggedUser()
	 */
	@Produces
	@LoggedUser
	@SessionScoped
	@Override
	public User getLoggedUser() {
		return currentUser;
	}
	
	/**
	 * 
	 */
	public UserLoginServiceImpl() {
		
	}


	/**
	 * @param username
	 * @param password
	 * @return
	 * @see es.qworks.jsfsecureapp.service.UserLoginService#login(java.lang.String, char[])
	 */
	@Override
	public LoginStatus login(String username, char[] password)  {
		
		LoginStatus ret = LoginStatus.LOGIN_ERR;
		
		try {
			String sessId = awsCognito.validate(username, password);
			LOG.info("{} credentials are valid", username);
			ret = LoginStatus.LOGIN_OK;
			ret.setSession(sessId);
			currentUser = getMapper().mapUser(userDao.findByUsername(username));
		} catch (MustChangePasswordException e) {
			LOG.warn("User {} must change password");
			ret = LoginStatus.USER_MUST_CHANGE_PASSWORD;
			ret.setSession(e.getAuthSession());
		} catch (AwsException e) {
			LOG.error("Error validating user {} credentials: {}", username, e.getMessage());
		} 
		
        return ret;
	}

	/**
	 * @param authSession
	 * @param username
	 * @param password
	 * @param newPassword
	 * @return
	 * @see es.qworks.jsfsecureapp.service.UserLoginService#changeUserPassword(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public LoginStatus changeUserPassword(String authSession, String username, String password, String newPassword) {
		
		LOG.debug("Changing password for {} ", username);
		
		LoginStatus ret = LoginStatus.LOGIN_ERR;

		try {
			String sessId = awsCognito.changeUserPassword(authSession, username, password, newPassword);
			ret = LoginStatus.LOGIN_OK;
			ret.setSession(sessId);
		}
		catch (UserNotFoundException | NotAuthorizedException | InvalidPasswordException |UnsupportedChallengeException e) {
			LOG.error("Login error: {}", e.getMessage());
		}
		catch (Exception e) {
			LOG.error("Login error: {}", e.getMessage());
		} 
		catch (AwsException e) {
			LOG.error("Login error: {}", e.getMessage());
		} 
		
		return ret;
	}

	/**
	 * 
	 * @see es.qworks.jsfsecureapp.service.UserLoginService#logout()
	 */
	@Override
	public void logout() {
		
		if (currentUser == null) {
			return;
		}
		
		try {
			awsCognito.logout(currentUser.getUsername());
		} catch (AwsException e) {
			LOG.error("An exception was thrown: {}", e.getMessage());
		}
		
	}
	
	private ServiceMapper getMapper()  {
		return ServiceMapper.INSTANCE;
	}
	

}
