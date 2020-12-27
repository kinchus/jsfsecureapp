package es.qworks.jsfsecureapp.model;

import java.util.List;

/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class Role extends Dto {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 5111283802675185770L;
	
	private String name;
	private List<String> permissions;

	/**
	 * 
	 */
	public Role() {
		
	}
	
	/**
	 * @param name 
	 * 
	 */
	public Role(String name) {
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
		return permissions;
	}

	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * @param role
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object role) {
		if (role == null) {
			return false;
		}
		else if (role instanceof Role) {
			return ((Role) role).getName() == this.getName();
		}
		else if (role instanceof String) {
			return ((String) role) == this.getName();
		}
		else {
			return false;
		}
	}
	
	/**
	 * @param role
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(String role) {
		return (role) == this.getName();
	}



}
