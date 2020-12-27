package es.qworks.jsfsecureapp.service.impl;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.qworks.jsfsecureapp.aws.AWSCognitoService;
import es.qworks.jsfsecureapp.aws.exception.AwsException;
import es.qworks.jsfsecureapp.aws.exception.AwsResourceAlreadyExistsException;
import es.qworks.jsfsecureapp.aws.exception.AwsResourceNotFoundException;
import es.qworks.jsfsecureapp.aws.model.CognitoUser;
import es.qworks.jsfsecureapp.dao.UserDao;
import es.qworks.jsfsecureapp.dao.entity.UserEntity;
import es.qworks.jsfsecureapp.model.Role;
import es.qworks.jsfsecureapp.model.User;
import es.qworks.jsfsecureapp.service.UserService;
import es.qworks.jsfsecureapp.service.exception.ObjectExistsException;
import es.qworks.jsfsecureapp.service.exception.ObjectNotFoundException;
import es.qworks.jsfsecureapp.service.exception.ObjectValidationException;
import es.qworks.jsfsecureapp.service.exception.ServiceError;



/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
@SessionScoped
public class UserServiceImpl implements UserService {

	private static final long serialVersionUID = -1236903867609589752L;
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	
	
	@Inject 
	private AWSCognitoService awsCognito;
	@Inject 
	private UserDao	userDao;
	
	/**
	 * 
	 */
	public UserServiceImpl() {}

	/**
	 * @param user
	 * @throws ObjectValidationException
	 * @see es.qworks.jsfsecureapp.service.Service#validate(java.lang.Object)
	 */
	@Override
	public void validate(User user) throws ObjectValidationException {
		if (user.getUsername() == null) {
			throw new ObjectValidationException("field \"username\" cannot be null");
		}
		if (user.getEmail() == null) {
			throw new ObjectValidationException("field \"email\" cannot be null");
		}

	}
	
	/**
	 * @param userId
	 * @return
	 * @see es.qworks.jsfsecureapp.service.Service#getById(java.io.Serializable)
	 */
	@Override
	public User getById(String userId) {
		return getMapper().mapUser(userDao.findByUsername(userId));
	}

	/**
	 * @return
	 * @throws ServiceError
	 * @see es.qworks.jsfsecureapp.service.Service#getAll()
	 */
	@Override
	public List<User> getAll() throws ServiceError {
		return getMapper().mapUserEntities(userDao.findAll());
	}

	/**
	 * @param data
	 * @return
	 * @throws ObjectExistsException
	 * @throws ServiceError
	 * @see es.qworks.jsfsecureapp.service.Service#create(java.lang.Object)
	 */
	@Override
	public User create(User data) throws ObjectExistsException, ServiceError {
		
		CognitoUser cogUser = new CognitoUser();
		cogUser.setUsername(data.getUsername());
		cogUser.setEmail(data.getEmail());
		cogUser.setGroups(data.getGroups());
		// Copy roles
		for (Role r : data.getRoles()) {
			String roleName = r.getName();
			cogUser.getRoles().add(roleName);
		}
		
		LOG.debug("Registering user \"{}\" in AWS Cognito", cogUser.getUsername());
		try {
			awsCognito.createUser(cogUser);
		} catch (AwsResourceAlreadyExistsException e1) {
			LOG.error("An user with name \"{}\" already exists in the Cognito user pool", cogUser.getUsername());
			throw new ObjectExistsException("Cognito User " + cogUser.getUsername());
		} catch (AwsException e) {
			LOG.error("Exception registering AWS Cognito user: {}", e.getMessage());
			throw new ServiceError("Unable to register user", e);
		}
		
		LOG.debug("Store user data", data.getUsername());
		UserEntity ret = userDao.save(getMapper().mapUser(data));
		return getMapper().mapUser(ret);
	}

	
	/**
	 * @param data
	 * @throws ServiceError
	 * @see es.qworks.jsfsecureapp.service.Service#update(java.lang.Object)
	 */
	@Override
	public void update(User data) throws ServiceError {
		userDao.save(getMapper().mapUser(data));
	}

	/**
	 * @param username
	 * @throws ObjectNotFoundException
	 * @throws ServiceError
	 * @see es.qworks.jsfsecureapp.service.Service#delete(java.io.Serializable)
	 */
	@Override
	public void delete(String username) throws ObjectNotFoundException, ServiceError {
		UserEntity ent = userDao.findByUsername(username);
		if (ent == null) {
			throw new ObjectNotFoundException("DB User entity: " + username);
		}
		userDao.delete(ent);
		
		try {
			awsCognito.deleteUser(username);
		} catch (AwsResourceNotFoundException e) {
			throw new ObjectNotFoundException("AWS Cognito User: " + username);
		} catch (AwsException e) {
			LOG.error("{}", e.getMessage());
			throw new ServiceError("Unable to delete user", e);
		}
	}

}
