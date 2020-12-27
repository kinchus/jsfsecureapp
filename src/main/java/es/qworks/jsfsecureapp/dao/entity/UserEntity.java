/**
 * 
 */
package es.qworks.jsfsecureapp.dao.entity;

import java.util.ArrayList;
import java.util.List;

import es.qworks.jsfsecureapp.dao.mongo.id.StringIdEntity;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.Indexes;
import dev.morphia.annotations.Reference;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;


/**
 * 
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 */
@Entity("User")
@Indexes({
	@Index(fields = @Field("username")),
	@Index(fields = {@Field("username"), @Field("email")})
})
public class UserEntity extends StringIdEntity   {
	
	private static final long serialVersionUID = -8125486807360655888L;
	
	private String username = null;
	private String email = null;
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;
	private String address = null;
	private String city = null;
	private String phoneNumber = null;
	private String zoneinfo = null;
	private String password = null;
	private Boolean enabled = null;
	private String status = null;
	private String accountId = null;
	@Reference
    private List<RoleEntity> roles;
	@Embedded
	private List<String> groups;
	
	
	/**
	 * 
	 */
	public UserEntity() {
		super();
		this.groups = null;
		this.roles = null;
		this.enabled = false;
	}

	/**
	 * @param usernname
	 */
	public UserEntity(String usernname) {
		super();
		this.username = usernname;
		this.groups = new ArrayList<String>();
		this.roles = new ArrayList<RoleEntity>();
		this.enabled = false;
	}
	
	/**
	 * @return the firstName
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the firstName to set
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
	 * @param fName the firstName to set
	 */
	public void setFirstName(String fName) {
		this.firstName = fName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the zoneinfo
	 */
	public String getZoneinfo() {
		return zoneinfo;
	}

	/**
	 * @param zoneinfo the zoneinfo to set
	 */
	public void setZoneinfo(String zoneinfo) {
		this.zoneinfo = zoneinfo;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
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

	/**
	 * @return the roles
	 */
	public List<RoleEntity> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}
	

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UserEntity) {
			UserEntity other = (UserEntity)obj;
			return this.username.equals(other.getUsername());
		}
		return false;
	}
	
	/**
	 * @param other 
	 * @return 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(UserEntity other) {
		return username.equals(other.getUsername());
	}


	
}
