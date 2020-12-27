package es.qworks.jsfsecureapp.dao;

import es.qworks.jsfsecureapp.dao.entity.RoleEntity;

/**
 * DAO for Role entities 
* @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public interface RoleDao extends Dao<RoleEntity, String> {
	
	/**
	 * @param roleName
	 * @return Matching Role entity or NULL if none found
	 */
	RoleEntity findByName(String roleName);

}
