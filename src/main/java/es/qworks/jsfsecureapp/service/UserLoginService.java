/**
 * 
 */
package es.qworks.jsfsecureapp.service;

import java.io.Serializable;

import es.qworks.jsfsecureapp.model.User;



/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public interface UserLoginService extends Serializable {
	
	/** PASSWD_MIN_LENGTH */
	public static final int PASSWD_MIN_LENGTH = 8;
	/** PASSWORD_REGEX */
	public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{" + PASSWD_MIN_LENGTH + ",}$";

	
	/**
	 * 
	 */
	public enum LoginStatus {
		/** LOGIN_OK */
		LOGIN_OK,
		/** LOGIN_ERR */
		LOGIN_ERR,
		/** USER_MUST_CHANGE_PASSWORD */
		USER_MUST_CHANGE_PASSWORD,
		/** USER_NOT_FOUND */
		USER_NOT_FOUND,
		/** SUBMITTED_INVALID_PASSWORD */
		SUBMITTED_INVALID_PASSWORD,
		/** NOT_AUTHORIZED */
		NOT_AUTHORIZED;
		
		private String session;

		/**
		 * @return the session
		 */
		public String getSession() {
			return session;
		}

		/**
		 * @param session the session to set
		 */
		public void setSession(String session) {
			this.session = session;
		}
		
	}
	
	/**
	 * @param id
	 * @param password
	 * @return
	 */
	LoginStatus login(String id, char []password);
		
	/**
	 * @param authSession 
	 * @param user
	 * @param password
	 * @param newPassword
	 * @return
	 */
	public LoginStatus changeUserPassword(String authSession, String user, String password, String newPassword);
	

	/**
	 * @return
	 */
	User getLoggedUser();
	
	
	/**
	 * @param id
	 */
	void logout();


}
