/**
 * 
 */
package es.qworks.jsfsecureapp.service.impl;

import java.util.List;

import javax.enterprise.context.SessionScoped;

import es.qworks.jsfsecureapp.model.Role;
import es.qworks.jsfsecureapp.service.RoleService;
import es.qworks.jsfsecureapp.service.exception.ServiceError;

/**
 * @author jmgarcia
 *
 */
@SessionScoped
public class RoleServiceImpl implements RoleService {

	private static final long serialVersionUID = 9061025421686225466L;

	/**
	 * @param object
	 * @throws ServiceError
	 * @see es.qworks.jsfsecureapp.service.Service#validate(java.lang.Object)
	 */
	@Override
	public void validate(Role object) throws ServiceError {
		
		
	}

	/**
	 * @param id
	 * @return
	 * @throws ServiceError
	 * @see es.qworks.jsfsecureapp.service.Service#getById(java.io.Serializable)
	 */
	@Override
	public Role getById(Long id) throws ServiceError {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @param name
	 * @return
	 * @throws ServiceError
	 * @see es.qworks.jsfsecureapp.service.RoleService#getByName(java.lang.String)
	 */
	@Override
	public Role getByName(String name) throws ServiceError {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 * @throws ServiceError
	 * @see es.qworks.jsfsecureapp.service.Service#getAll()
	 */
	@Override
	public List<Role> getAll() throws ServiceError {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param data
	 * @return
	 * @throws ServiceError
	 * @see es.qworks.jsfsecureapp.service.Service#create(java.lang.Object)
	 */
	@Override
	public Role create(Role data) throws ServiceError {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param data
	 * @throws ServiceError
	 * @see es.qworks.jsfsecureapp.service.Service#update(java.lang.Object)
	 */
	@Override
	public void update(Role data) throws ServiceError {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param id
	 * @throws ServiceError
	 * @see es.qworks.jsfsecureapp.service.Service#delete(java.io.Serializable)
	 */
	@Override
	public void delete(Long id) throws ServiceError {
		// TODO Auto-generated method stub
		
	}

}
