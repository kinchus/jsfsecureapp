/**
 * 
 */
package es.qworks.jsfsecureapp.dao;

import es.qworks.jsfsecureapp.dao.entity.UserEntity;

/**
 * @author Admin2
 *
 */
public interface UserDao extends Dao<UserEntity, String> {

	
	/**
	 * Gets the user with the given username (or null if not found)
	 * @param username
	 * @return User entity or NULL if none found
	 */
	UserEntity findByUsername(String username);

	/**
	 * Gets the user with the given email (or null if not found)
	 * @param email
	 * @return User entity or NULL if none found
	 */
	UserEntity findByEmail(String email);


	
}
