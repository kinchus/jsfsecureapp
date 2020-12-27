/**
 * 
 */
package es.qworks.jsfsecureapp.dao.mongo;

import javax.enterprise.context.SessionScoped;

import es.qworks.jsfsecureapp.dao.GenericDao;
import es.qworks.jsfsecureapp.dao.RoleDao;
import es.qworks.jsfsecureapp.dao.entity.RoleEntity;

import dev.morphia.Datastore;


/**
* @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
@SessionScoped
public class RoleDaoImpl extends GenericDao<RoleEntity, String> implements RoleDao {

	private static final long serialVersionUID = -54126115278005502L;

	
	/**
	 * Constructor 
	 * @param datastore
	 */
	public RoleDaoImpl(Datastore datastore) {
		super(RoleEntity.class, datastore);
	}

	
	/**
	 * @param roleName
	 * @return
	 * @see es.qworks.jsfsecureapp.dao.RoleDao#findByName(java.lang.String)
	 */
	@Override
	public RoleEntity findByName(String roleName) {
		return getQuery().field("name").equal(roleName).first();
	}
	
}
