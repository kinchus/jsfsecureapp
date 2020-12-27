/**
 * 
 */
package es.qworks.jsfsecureapp.dao.mongo;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import es.qworks.jsfsecureapp.dao.GenericDao;
import es.qworks.jsfsecureapp.dao.UserDao;
import es.qworks.jsfsecureapp.dao.entity.UserEntity;

import dev.morphia.Datastore;

/**
* @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
@Named
@SessionScoped
public class UserDaoImpl extends GenericDao<UserEntity, String> implements UserDao {

	private static final long serialVersionUID = -7004856610947020380L;

	/**
	 * 
	 */
	public UserDaoImpl() {
		super(UserEntity.class);
	}
	
	/**
	 * @param datastore
	 */
	public UserDaoImpl(Datastore datastore) {
		super(UserEntity.class, datastore);
	}

	/**
	 * @param username
	 * @return
	 */
	@Override
	public UserEntity findByUsername(String username) {
		return getQuery().field("username").equal(username).first();
	}

	/**
	 * @param email
	 * @return
	 */
	@Override
	public UserEntity findByEmail(String email) {
		return getQuery().field("email").equal(email).first();
	}
	



}
