package es.qworks.jsfsecureapp.model;

import java.util.List;

/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class User extends Dto
{
    private static final long serialVersionUID = -6122152881769945213L;

    /** */
    public static final String ROLE_ADMINISTRATOR 	= "Administrator";
    /** */
    public static final String ROLE_CUSTOMER	 	= "Customer";
    /** */
    public static final String ROLE_ENDUSER 		= "EndUser";
	
    private String username;         
    private String firstName;
    private String lastName;
    private String email;
    private String accountId;
    private List<Role> roles;
    private List<String> groups;
    
    /**
     * 
     */
    public User() {
    	
    }
    
    /**
     * @param username
     */
    public User(String username) {
    	setUsername(username);
    }
        
    /**
     * @param role
     * @return
     */
    public Boolean hasRole(String role) {
    	if (roles != null) {
    		for (Role r:roles) {
	    		if (r.equals(role)) {
	    			return true;
	    		}
    		}
    	}	
    	
    	return false;
    }
   
   	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param customerId the accountId to set
	 */
	public void setAccountId(String customerId) {
		this.accountId = customerId;
	}

	/**
	 * @return the role
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the role to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the groups
	 */
	public List<String> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

}