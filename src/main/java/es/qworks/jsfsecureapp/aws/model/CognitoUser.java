/**
 * 
 */
package es.qworks.jsfsecureapp.aws.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;


/**
 * The Class CognitoUser.
 */
public class CognitoUser implements Serializable  {
	
	private static final long serialVersionUID = -8125486807360655888L;
	
	private String username = null;
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;
	private String email = null;
	private String address = null;
	private String city = null;
	private String phoneNumber = null;
	private String zoneinfo = null;
	private String password = null;
	private String accessToken = null;
	private Boolean enabled = null;
	private String status = null;
	private String ownerId = null;
	private String profileId = null;
	
    private List<String> roles;
	private List<String> groups;
		
	/**
	 * Instantiates a new cognito user.
	 */
	public CognitoUser() {
		super();
		this.groups = new ArrayList<String>();
		this.roles = new ArrayList<String>();
		this.enabled = false;
	}

	/**
	 * Instantiates a new cognito user.
	 *
	 * @param userName the firstName
	 */
	public CognitoUser(String userName) {
		super();
		this.username = userName;
		this.groups = new ArrayList<String>();
		this.roles = new ArrayList<String>();
		this.enabled = false;
	}
	
	/**
	 * Checks for group.
	 *
	 * @param group the group
	 * @return true, if successful
	 */
	public boolean hasGroup(String group) {
		for (String userGrp:getGroups()) {
			if (group.equals(userGrp)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks for role.
	 *
	 * @param roles the roles
	 * @return true, if successful
	 */
	public boolean hasRole(String ... roles) {
		for (String usrRole:this.roles) {
			for (String role:roles) {
				if (usrRole.equals(role)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Gets the user username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the user firstName.
	 *
	 * @param userName the new user firstName
	 */
	public void setUsername(String userName) {
		this.username = userName;
	}

	/**
	 * Gets the firstName.
	 *
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the firstName.
	 *
	 * @param firstName the new firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the middle firstName.
	 *
	 * @return the middle firstName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * Sets the middle firstName.
	 *
	 * @param middleName the new middle firstName
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Gets the last firstName.
	 *
	 * @return the last firstName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last firstName.
	 *
	 * @param lastName the new last firstName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the phone number.
	 *
	 * @return the phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets the phone number.
	 *
	 * @param phoneNumber the new phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Gets the zoneinfo.
	 *
	 * @return the zoneinfo
	 */
	public String getZoneinfo() {
		return zoneinfo;
	}

	/**
	 * Sets the zoneinfo.
	 *
	 * @param zoneinfo the new zoneinfo
	 */
	public void setZoneinfo(String zoneinfo) {
		this.zoneinfo = zoneinfo;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the enabled.
	 *
	 * @return the enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled.
	 *
	 * @param enabled the new enabled
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	/**
	 * Checks for role.
	 *
	 * @param role the role
	 * @return true, if successful
	 */
	public boolean hasRole(String role) {
		if (this.roles != null) {
			for (String r:roles) {
				if (r.equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the access token.
	 *
	 * @return the access token
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * Sets the access token.
	 *
	 * @param accessToken the new access token
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * Gets the groups.
	 *
	 * @return the groups
	 */
	public List<String> getGroups() {
		return groups;
	}

	/**
	 * Sets the groups.
	 *
	 * @param groups the new groups
	 */
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	/**
	 * Gets the owner id.
	 *
	 * @return the owner id
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * Gets the profile id.
	 *
	 * @return the profile id
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * Sets the profile id.
	 *
	 * @param profileId the new profile id
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * Sets the owner id.
	 *
	 * @param ownerId the new owner id
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * Gets the roles.
	 *
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * Sets the roles.
	 *
	 * @param roles the new roles
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
}
