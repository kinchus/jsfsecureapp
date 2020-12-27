/**
 * 
 */
package es.qworks.jsfsecureapp.dao.entity;

import java.util.ArrayList;
import java.util.List;

import es.qworks.jsfsecureapp.dao.mongo.id.StringIdEntity;

import dev.morphia.annotations.Entity;



/**
* @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
@Entity("Role")
public class RoleEntity extends StringIdEntity {
	
	private static final long serialVersionUID = 7987038545534614221L;

	/** */
	public static RoleEntity ADMINISTRATOR = new RoleEntity("ADMINISTRATOR_ROLE");
	/** */
	public static RoleEntity CUSTOMER = new RoleEntity("CUSTOMER_ROLE");
	
	private String name;
	private List<String> permissions;

	
	/**
	 * 
	 */
	public RoleEntity() {
		
	}
	/**
	 * @param name 
	 * 
	 */
	public RoleEntity(String name) {
		setName(name);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the permissions
	 */
	public List<String> getPermissions() {
		if (permissions == null) {
			permissions = new ArrayList<String>();
		}
		return permissions;
	}

	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RoleEntity) {
			return equals(obj);
		}
		else if (obj instanceof String) {
			return equals(obj);
		}
		return false;
	}

}
