/**
 * 
 */
package es.qworks.jsfsecureapp.aws.exception.cognito;

import es.qworks.jsfsecureapp.aws.exception.AwsException;

/**
* @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class MustChangePasswordException extends AwsException {

	private static final long serialVersionUID = -633481973871719880L;

	private String authSession = null;
	
	/**
	 * @param challengeName
	 * @param authSession 
	 */
	public MustChangePasswordException(String challengeName, String authSession) {
		super(challengeName);
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
