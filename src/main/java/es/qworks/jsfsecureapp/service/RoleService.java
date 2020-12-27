/**
 * 
 */
package es.qworks.jsfsecureapp.service;

import es.qworks.jsfsecureapp.model.Role;
import es.qworks.jsfsecureapp.service.exception.ServiceError;

/**
 * @author jmgarcia
 *
 */
public interface RoleService extends Service<Role, Long> {

	/**
	 * @param name
	 * @return
	 * @throws ServiceError 
	 */
	Role getByName(String name) throws ServiceError;
}
