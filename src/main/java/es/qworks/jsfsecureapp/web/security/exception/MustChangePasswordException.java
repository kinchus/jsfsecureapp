/**
 * 
 */
package es.qworks.jsfsecureapp.web.security.exception;

import org.apache.shiro.authc.ExpiredCredentialsException;

/**
* @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class MustChangePasswordException extends ExpiredCredentialsException {

	private static final long serialVersionUID = -633481973871719880L;

	private String authSession = null;
	
	/**
	 * @param userName
	 * @param authSession 
	 */
	public MustChangePasswordException(String userName, String authSession) {
		super("Password for user " + userName + "must be changed ");
		setAuthSession(authSession);
	}

	/**
	 * @return the authSession
	 */
	public String getAuthSession() {
		return authSession;
	}

	/**
	 * @param authSession the authSession to set
	 */
	public void setAuthSession(String authSession) {
		this.authSession = authSession;
	}


}
