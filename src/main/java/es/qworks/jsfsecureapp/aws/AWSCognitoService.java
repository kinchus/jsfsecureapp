/**
 * 
 */
package es.qworks.jsfsecureapp.aws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupResult;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDisableUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminEnableUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminListGroupsForUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminListGroupsForUserResult;
import com.amazonaws.services.cognitoidp.model.AdminRemoveUserFromGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.cognitoidp.model.CodeMismatchException;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.CreateGroupRequest;
import com.amazonaws.services.cognitoidp.model.CreateGroupResult;
import com.amazonaws.services.cognitoidp.model.DeleteGroupRequest;
import com.amazonaws.services.cognitoidp.model.ExpiredCodeException;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.GlobalSignOutRequest;
import com.amazonaws.services.cognitoidp.model.GroupExistsException;
import com.amazonaws.services.cognitoidp.model.GroupType;
import com.amazonaws.services.cognitoidp.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidp.model.ListGroupsRequest;
import com.amazonaws.services.cognitoidp.model.ListGroupsResult;
import com.amazonaws.services.cognitoidp.model.ListUsersRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersResult;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;

import es.qworks.jsfsecureapp.aws.exception.AwsException;
import es.qworks.jsfsecureapp.aws.exception.AwsResourceAlreadyExistsException;
import es.qworks.jsfsecureapp.aws.exception.AwsResourceNotFoundException;
import es.qworks.jsfsecureapp.aws.exception.cognito.MustChangePasswordException;
import es.qworks.jsfsecureapp.aws.exception.cognito.UnsupportedChallengeException;
import es.qworks.jsfsecureapp.aws.model.CognitoUser;
import es.qworks.jsfsecureapp.util.StringUtil;




/**
@author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
@Named
@SessionScoped
public class AWSCognitoService implements Serializable {

	private static final long serialVersionUID = 246165019306087069L;

	private static final Logger LOG = LoggerFactory.getLogger(AWSCognitoService.class);
	
	/** The Constant AWS_REGION. */
	public static final String AWS_REGION = "aws.region";
	
	/** The Constant AWS_PROFILE. */
	public static final String AWS_PROFILE = "aws.profile";
	
	/** The Constant AWS_ACCOUNT_ID. */
	public static final String AWS_ACCOUNT_ID = "aws.account_id";
	
	/** The Constant AWS_COGNITO_USERPOOLID. */
	public static final String AWS_COGNITO_USERPOOLID = "aws.cognito.pool_id";
	
	/** The Constant AWS_COGNITO_POOLCLIENTID. */
	public static final String AWS_COGNITO_POOLCLIENTID = "aws.cognito.app_client_id";



	private static final String ATTR_EMAIL 			= "email";
	private static final String ATTR_NAME 			= "name";
	private static final String ATTR_MIDDLE_NAME 	= "middle_name";
	private static final String ATTR_ADDRESS		= "address";
	private static final String ATTR_CITY			= "city";
	private static final String ATTR_PHONE_NUMBER 	= "phone_number";
	private static final String ATTR_ZONEINFO		= "zoneinfo";
	private static final String ATTR_PROFILE 		= "profile";
	private static final String ATTR_ROLE			= "custom:role";
	private static final String ATTR_OWNERID 		= "custom:owner_id";
	
	private static final String CHANGE_PASSWD			= "NEW_PASSWORD_REQUIRED";
	private static final int MAX_GROUP_PRECEDENCE = 5;

	private static AWSConfig config = AWSConfig.getInstance();
	private static String region = config.getProperty(AWS_REGION);
	private static String identityPoolId = config.getProperty(AWS_COGNITO_USERPOOLID);
	private static String applicationClientId = config.getProperty(AWS_COGNITO_POOLCLIENTID);

	private static AWSCognitoIdentityProviderClient cognitoClient = null;
	private static AWSCredentialsProvider credentialsProvider = null;
	

	/**
	 * Initialize the AWS Cognito client with configured parameters.
	 *  
	 */
	public static void init() {
		
		LOG.debug("Building Cognito Identity Provider Client with configured parameters");
		cognitoClient = (AWSCognitoIdentityProviderClient) AWSCognitoIdentityProviderClientBuilder
				.standard()
				.withCredentials(getCredentialsProvider())
				.withRegion(region)
				.build();
		
	}
	
	/**
	 * Initialize the AWS Cognito client for the given Pool ID and Application client ID, using
	 * the default credentials.
	 *  
	 * @param poolId 
	 * @param clientId 
	 */
	public static void init(String poolId, String clientId) {
		identityPoolId = poolId;
		applicationClientId = clientId;
		LOG.debug("Building Cognito Identity Provider Client (Pool ID: {}, App Client ID: {})", poolId, clientId);
		cognitoClient = (AWSCognitoIdentityProviderClient) AWSCognitoIdentityProviderClientBuilder.standard()
				.withCredentials(getCredentialsProvider())
				.build();
	}
	

	
	/**
	 * Gets the internal AWS sdk cognito client
	 * @return 
	 */
	private static AWSCognitoIdentityProviderClient getCognitoClient() {
		

		if ((cognitoClient == null)) {
			init();
		}
		return cognitoClient;
	}

	/**
	 * 
	 *
	 * @return 
	 */
	public static AWSCredentialsProvider getCredentialsProvider() {
		
		if (credentialsProvider == null) {
			
			AWSCredentials creds = config.getCredentials();
			String profile = config.getProfile();
			
			if (creds != null) {
				LOG.trace("Get credentials for ACCESS_KEY_ID {}", creds.getAWSAccessKeyId());
				credentialsProvider = new AWSStaticCredentialsProvider(creds);
			}
			else if (!StringUtil.isBlank(profile)) {
				LOG.trace("Get credentials from profile {}", profile);
				credentialsProvider =  new ProfileCredentialsProvider(profile);
			}
			else {
				LOG.trace("Get credentials from default AWS chain");
				credentialsProvider = DefaultAWSCredentialsProviderChain.getInstance();
			}
			
		}
		return credentialsProvider;

	}
	
	
	/**
	 * @return the applicationClientId
	 */
	private static String getApplicationClientId() {
		return applicationClientId;
	}
	
	/**
	 * 
	 *
	 * @return 
	 */
	private static String getIdentityPoolId() {
		return identityPoolId;
	}
	

	
	/**
	 * Default constructor
	 */
	public AWSCognitoService() {
		super();
	}

	
	/**
	 * 
	 *
	 * @param username 
	 * @param password 
	 * @return 
	 * @throws MustChangePasswordException 
	 * @throws AwsException 
	 */
	public String validate(String username, char[] password) throws MustChangePasswordException, AwsException {

		LOG.debug("Starting login for user {}", username);
		
		Map<String, String> authParams = new HashMap<String, String>();
		authParams.put("USERNAME", username);
		authParams.put("PASSWORD", new String(password));

		AdminInitiateAuthResult authResponse = null;
		AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
				.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).withAuthParameters(authParams)
				.withClientId(getApplicationClientId()).withUserPoolId(getIdentityPoolId());
		try {
			authResponse = getCognitoClient().adminInitiateAuth(authRequest);
		}
		catch (AmazonServiceException e) {
			throw new AwsException("AdminInitiateAuth", e);
		}
		
		if ((authResponse.getChallengeName() != null) && authResponse.getChallengeName().equals(CHANGE_PASSWD)) {
			throw new MustChangePasswordException(authResponse.getChallengeName(), authResponse.getSession());
		}

		LOG.debug("User {} login OK.", username);
		
		AuthenticationResultType res = authResponse.getAuthenticationResult();
		return res.getAccessToken();
	}

	
	/**
	 * 
	 *
	 * @param accessToken 
	 * @throws AwsException 
	 */
	public void logout(String accessToken) throws AwsException {
		GlobalSignOutRequest request = new GlobalSignOutRequest()
				.withAccessToken(accessToken);
		try {
			getCognitoClient().globalSignOut(request);
		}
		catch (AmazonServiceException e) {
			throw new AwsException("GlobalSignOut", e);
		}
	}

	
	/**
	 * 
	 *
	 * @param authSession 
	 * @param username 
	 * @param password 
	 * @param newPassword 
	 * @return String with the access token
	 * @throws UnsupportedChallengeException 
	 * @throws AwsException 
	 */
	public String changeUserPassword(String authSession, String username, String password, String newPassword)  throws UnsupportedChallengeException, AwsException {
		
		Map<String,String> challengeResponses = new HashMap<String,String>();
        challengeResponses.put("USERNAME", username);
        challengeResponses.put("PASSWORD", password);
        challengeResponses.put("NEW_PASSWORD", newPassword);
		
        AdminRespondToAuthChallengeResult challengeResponse = null;
		AdminRespondToAuthChallengeRequest request = new AdminRespondToAuthChallengeRequest()
                .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                .withChallengeResponses(challengeResponses)
                .withClientId(getApplicationClientId())
                .withUserPoolId(getIdentityPoolId())
                .withSession(authSession);;

        try {
			LOG.debug("Requesting user password change");
			challengeResponse = getCognitoClient().adminRespondToAuthChallenge(request);
		}
		catch (AmazonServiceException e) {
			throw new AwsException("AdminRespondToAuthChallenge", e);
		}
		if (challengeResponse.getChallengeName() == null) {
			AuthenticationResultType res = challengeResponse.getAuthenticationResult();
			LOG.debug("Password changed successfully. User {} is now logged in", username);
			return res.getAccessToken();
		}
		else {
			throw new UnsupportedChallengeException(challengeResponse.getChallengeName());
		}
	}
	
	
	/**
	 * 
	 *
	 * @param username 
	 * @throws AwsException 
	 */
	public void sendVerificationCode(String username)  throws AwsException {
		
		LOG.debug("Sending verification code for {} ", username);
		ForgotPasswordRequest request = new ForgotPasswordRequest()
				.withUsername(username)
				.withClientId(getApplicationClientId());
		try {
			getCognitoClient().forgotPassword(request);
		}
		catch (UserNotFoundException e) {
			throw new AwsResourceNotFoundException(username + " not found in user pool");
		}
		catch (UserNotConfirmedException e) {
			throw new AwsException("ForgotPassword", e);
		}
		catch (AmazonServiceException e) {
			throw new AwsException(e);
		}
		
	}
	
	/**
	 * 
	 *
	 * @param username 
	 * @param code 
	 * @param newPassword 
	 * @throws AwsException 
	 */
	public void confirmVerificationCode(String username, String code, String newPassword) throws AwsException {
		
		LOG.debug("Confirm verification code for {} ", username);
		
		ConfirmForgotPasswordRequest  request = new ConfirmForgotPasswordRequest ()
				.withUsername(username)
				.withPassword(newPassword)
				.withConfirmationCode(code)
				.withClientId(getApplicationClientId());
		try {
			getCognitoClient().confirmForgotPassword(request);
		}
		catch (InvalidPasswordException e) {
			throw new AwsException("ConfirmForgotPassword", "Password is not valid", e);
		}
		catch (CodeMismatchException e) {
			throw new AwsException("ConfirmForgotPassword", "Code mismatch", e);
		}
		catch (ExpiredCodeException e) {
			throw new AwsException("ConfirmForgotPassword", "Code has expired", e);
		}
		catch (AmazonServiceException e) {
			throw new AwsException(e);
		}
	}


	/**
	 * 
	 *
	 * @param userName 
	 * @return 
	 */
	public CognitoUser findUser(String userName)
	{
		LOG.debug("Retrieve user data for {}", userName);
		CognitoUser aux = null;
		
		AdminGetUserRequest getUserReq = new AdminGetUserRequest()
				.withUsername(userName)
				.withUserPoolId(getIdentityPoolId());
		AdminGetUserResult gres = null;
		try {
			gres = getCognitoClient().adminGetUser(getUserReq);
		}
		catch (UserNotFoundException nfe) {
			return null;
		}
		
		if (gres.getSdkHttpMetadata().getHttpStatusCode() != 200) {
			LOG.error("ERROR invoking the request");
			return null;
		}
		
		aux = new CognitoUser(userName);
		aux.setEnabled(gres.getEnabled());
		aux.setStatus(gres.getUserStatus());
		List<AttributeType> attributes = gres.getUserAttributes();
		setUserAttributes(aux, attributes);
		
		AdminListGroupsForUserRequest grReq = new AdminListGroupsForUserRequest()
				.withUserPoolId(getIdentityPoolId())
				.withUsername(aux.getUsername());
		
		AdminListGroupsForUserResult grRes = getCognitoClient().adminListGroupsForUser(grReq);
		
		List<String> groups = new ArrayList<String>(grRes.getGroups().size());
		
		int actualPrec = 0;
		while (++actualPrec < AWSCognitoService.MAX_GROUP_PRECEDENCE) {
			for (GroupType group:grRes.getGroups()) {
				if (group.getPrecedence() == actualPrec) {
					groups.add(group.getGroupName());
				}
			}
		}
		
		if (groups.size() > 0) {
			aux.setGroups(groups);
		}
		
		return aux;
	}


	
	/**
	 * 
	 *
	 * @param maxResults 
	 * @return 
	 */
	public List<CognitoUser> findAllUsers(Integer maxResults) {
		List<CognitoUser> ret = new ArrayList<CognitoUser>();
		ListUsersResult result = null;
		ListUsersRequest luReq = null;
		AdminListGroupsForUserRequest lgReq = null;
		AdminListGroupsForUserResult lgRes = null;
		String pToken = null;
		int count = 0;
		
		do {
			luReq = new ListUsersRequest()
					.withUserPoolId(getIdentityPoolId());
			
			if (maxResults != null) {
				maxResults -= count;
				luReq.setLimit(maxResults);
			}
			
			result = getCognitoClient().listUsers(luReq);
			pToken = result.getPaginationToken();
			
			if ( result.getUsers() != null )  {
				count += result.getUsers().size();
				for (UserType usr:result.getUsers()) {
					
					// String username = usr.getUsername();
					// Boolean enabled = usr.getEnabled();
					// String status = usr.getUserStatus();
					// Collection<AttributeType> attributes = usr.getAttributes();
					CognitoUser oarmUser = null;
					
					if ((oarmUser =  convertFromUserType(usr)) == null) {
						continue;
					}
						
					// Add the AWS user groups
					// if (FEAT_GET_GROUPS_FOR_USER) {
					lgReq = new AdminListGroupsForUserRequest()
							.withUserPoolId(getIdentityPoolId())
							.withUsername(oarmUser.getUsername());
					
					lgRes = getCognitoClient().adminListGroupsForUser(lgReq);
					List<String> groups = new ArrayList<String>(lgRes.getGroups().size());
					int actualPrec = 0;
					while (++actualPrec < AWSCognitoService.MAX_GROUP_PRECEDENCE) {
						for (GroupType group:lgRes.getGroups()) {
							if (group.getPrecedence() == actualPrec) {
								groups.add(group.getGroupName());
							}
						}
					}
					
					if (groups.size() > 0) {
						oarmUser.setGroups(groups);
					}
					
					ret.add( oarmUser );
					
					
				}
			}
		} while ((pToken != null) && (count < maxResults));
		
		return ret;
	}
	
	/**
	 * 
	 *
	 * @param user 
	 * @return 
	 * @throws AwsResourceAlreadyExistsException 
	 * @throws AwsException 
	 */
	public CognitoUser createUser(CognitoUser user) throws AwsResourceAlreadyExistsException, AwsException {
		
		Objects.requireNonNull(user);
		
		List<AttributeType> attributes = getUserAttributes(user);
		
		// CreateUser request
		AdminCreateUserResult res = null;
		AdminCreateUserRequest request = new AdminCreateUserRequest()
						.withUserPoolId(getIdentityPoolId())
						.withUsername(user.getUsername())
						.withUserAttributes(attributes);
				
		// Password provided?
		if ((user.getPassword() != null) && (!user.getPassword().isEmpty())) {
			request.setTemporaryPassword(user.getPassword());
		}
		
		try {
			LOG.debug("Request for user {} creation.", user.getUsername());
			res = getCognitoClient().adminCreateUser(request);
		}
		catch (UsernameExistsException e) {
			throw new AwsResourceAlreadyExistsException(user.getUsername());
		}
		catch (AmazonServiceException e) {
			throw new AwsException("AdminCreateUser", e);
		}
		
		attributes.clear();
		
		CognitoUser ret = null;
		
		// Añade el usuario a los grupos a los que pertenece
		if (res.getUser() != null) {
			ret = convertFromUserType(res.getUser());
			
			List<String> groups = getGroups();
			
			// Añade el usuario a los grupos
			for (String groupName:user.getGroups()) {
				
				if (!groups.contains(groupName)) {
					createGroup(groupName, null, 4);
				}
				
				AdminAddUserToGroupRequest addGrpReq = new AdminAddUserToGroupRequest()
						.withUserPoolId(getIdentityPoolId())
						.withUsername(user.getUsername())
						.withGroupName(groupName);
				try {
					AdminAddUserToGroupResult addGrpRes = getCognitoClient().adminAddUserToGroup(addGrpReq);
					if (addGrpRes.getSdkHttpMetadata().getHttpStatusCode() != 200) {
						LOG.trace("Couldn't add user {} to group {}. HTTP Status = {} ", 
								user.getUsername(), 
								groupName,
								addGrpRes.getSdkHttpMetadata().getHttpStatusCode());
					}
					else {
						LOG.trace("Added user {} to group {}.", user.getUsername(), groupName);
					}
				}
				catch (AmazonServiceException e) {
					throw new AwsException("AdminAddUserToGroup", e);
				}
			}
		}
		
		// Disable Cognito user if the actual is not enabled
		if (!user.getEnabled()) {
			disableUser(user.getUsername());
		}
		
		return ret;
	}
	
	
	/**
	 * 
	 *
	 * @param user 
	 * @return 
	 * @throws AwsResourceNotFoundException
	 * @throws AwsException 
	 */
	public CognitoUser updateUser(final CognitoUser user) throws AwsResourceNotFoundException, AwsException {
		
		Objects.requireNonNull(user);
		
		String userName = user.getUsername();
		
		// Build user ATTRIBUTES spec
		List<AttributeType> attributes = getUserAttributes(user);
		
		// Request for user attribute update
		try {
			LOG.trace("Updating attributes");
			AdminUpdateUserAttributesRequest updUserAttrreq = new AdminUpdateUserAttributesRequest()
					.withUsername(userName)
					.withUserAttributes(attributes)
					.withUserPoolId(getIdentityPoolId());
			getCognitoClient().adminUpdateUserAttributes(updUserAttrreq);
		}
		catch (UserNotFoundException e) {
			throw new AwsResourceNotFoundException(userName);
		}
		catch (AmazonServiceException e) {
			LOG.error("{} error while updating user attributes: {}", e.getErrorType().toString(), e.getErrorMessage());
			throw new AwsException("adminUpdateUserAttributes", e);
		}

	
		LOG.debug("Updating groups for user {}", userName);
		// Load stored USER GROUPS
		List<String> oldGroups = new ArrayList<String>();
		String token = null;
		do {
			AdminListGroupsForUserResult resp = null;
			AdminListGroupsForUserRequest adminListGroupsForUserRequest = new AdminListGroupsForUserRequest()
				.withUserPoolId(getIdentityPoolId())
				.withUsername(userName);
			try {
				resp = getCognitoClient().adminListGroupsForUser(adminListGroupsForUserRequest);
				for (GroupType grpT:resp.getGroups()) {
					oldGroups.add(grpT.getGroupName());
				}
				token = resp.getNextToken();
			}
			catch (AmazonServiceException e) {
				LOG.warn("An error occured while requesting user groups from {}: {}", userName, e.getErrorMessage());
			}
		} while (token != null);
		
		// Check the stored user groups against the actual user groups 
		// First add the new groups not included in the old ones
		for (String newGrp:user.getGroups()) {
			if (!oldGroups.contains(newGrp)) {
				AdminAddUserToGroupRequest addGrpReq = new AdminAddUserToGroupRequest()
						.withUserPoolId(getIdentityPoolId())
						.withUsername(userName)
						.withGroupName(newGrp);
				try {
					getCognitoClient().adminAddUserToGroup(addGrpReq);
				}
				catch (AmazonServiceException e) {
					LOG.warn("An error occured while adding the user {} to the group {}: {}", userName, newGrp, e.getErrorMessage());
				}
			}
		}
		
		// Then remove the old groups not present in the new ones
		for (String oldGrp:oldGroups) {
			if (!user.getGroups().contains(oldGrp)) {
				AdminRemoveUserFromGroupRequest addGrpReq = new AdminRemoveUserFromGroupRequest()
						.withUserPoolId(getIdentityPoolId())
						.withUsername(userName)
						.withGroupName(oldGrp);
				try {
					getCognitoClient().adminRemoveUserFromGroup(addGrpReq);
				}
				catch (AmazonServiceException e) {
					LOG.warn("An error occured while removing the user {} from the group {}.\n{}: {}", userName, oldGrp, e, e.getMessage());
				}
			}
		}
		
		return user;
	}
	

	
	/**
	 * 
	 *
	 * @param userName 
	 * @param enabled 
	 * @throws AwsResourceNotFoundException
	 * @throws AwsException 
	 */
	@Deprecated
	public void setEnabled(String userName, boolean enabled) throws AwsResourceNotFoundException, AwsException
	{	
		// ENABLED/DISABLED 
		if (enabled) {
			LOG.debug("Enabling user");
			AdminEnableUserRequest enableUserRequest = new AdminEnableUserRequest()
					.withUserPoolId(getIdentityPoolId())
					.withUsername(userName); 
			try {
				getCognitoClient().adminEnableUser(enableUserRequest);
			}
			catch (UserNotFoundException e) {
				throw new AwsResourceNotFoundException(userName);
			}
			catch (AmazonServiceException e) {
				throw new AwsException("AdminEnableUser", e);
			}
		}
		else  {
			AdminDisableUserRequest disableUserRequest = new AdminDisableUserRequest()
					.withUserPoolId(getIdentityPoolId())
					.withUsername(userName);
			try {
				LOG.debug("Disabling user {}", userName);
				getCognitoClient().adminDisableUser(disableUserRequest);
			}
			catch (UserNotFoundException e) {
				throw new AwsResourceNotFoundException(userName);
			}
			catch (AmazonServiceException e) {
				throw new AwsException("AdminDisableUser", e);
			}
		}
	}

	/**
	 * 
	 *
	 * @param userName 
	 * @throws AwsResourceNotFoundException
	 * @throws AwsException 
	 */
	public void enableUser(String userName) throws AwsResourceNotFoundException, AwsException {	
		Objects.requireNonNull(userName);
		
		LOG.debug("Enabling user {}", userName);
		AdminEnableUserRequest enableUserRequest = new AdminEnableUserRequest()
					.withUserPoolId(getIdentityPoolId())
					.withUsername(userName); 
		try {
			getCognitoClient().adminEnableUser(enableUserRequest);
		}
		catch (UserNotFoundException e) {
			throw new AwsResourceNotFoundException(userName);
		}
		catch (AmazonServiceException e) {
			throw new AwsException("AdminEnableUser", e);
		}
	}
	
	/**
	 * 
	 *
	 * @param userName 
	 * @throws AwsResourceNotFoundException
	 * @throws AwsException 
	 */
	public void disableUser(String userName) throws AwsResourceNotFoundException, AwsException
	{	
		Objects.requireNonNull(userName);
		LOG.debug("Disabling user {}", userName);
		AdminEnableUserRequest enableUserRequest = new AdminEnableUserRequest()
					.withUserPoolId(getIdentityPoolId())
					.withUsername(userName); 
		try {
			getCognitoClient().adminEnableUser(enableUserRequest);
		}
		catch (UserNotFoundException e) {
			throw new AwsResourceNotFoundException(userName);
		}
		catch (AmazonServiceException e) {
			throw new AwsException("AdminDisableUser", e);
		}
	}



	/**
	 * 
	 *
	 * @param ids 
	 * @throws AwsResourceNotFoundException 
	 * @throws AwsException 
	 */
	public void deleteUsers(Collection<String> ids) throws AwsResourceNotFoundException, AwsException {
		Objects.requireNonNull(ids);
		for (String id:ids) {
			deleteUser(id);
		}
	}


	/**
	 * 
	 *
	 * @param userName 
	 * @throws AwsResourceNotFoundException 
	 * @throws AwsException 
	 */
	public void deleteUser(String userName) throws AwsResourceNotFoundException, AwsException  {
		Objects.requireNonNull(userName);
		AdminDeleteUserRequest request = new AdminDeleteUserRequest()
				.withUsername(userName)
				.withUserPoolId(getIdentityPoolId());
		try {
			getCognitoClient().adminDeleteUser(request);
		}
		catch (UserNotFoundException e) {
			throw new AwsResourceNotFoundException(userName);
		}
		catch (AmazonServiceException e) {
			throw new AwsException(e);
		}
	}
	
	
	/**
	 * 
	 *
	 * @param groupName 
	 * @param description 
	 * @param precedence 
	 * @return 
	 * @throws AwsResourceAlreadyExistsException 
	 * @throws AwsException 
	 */
	public String createGroup(String groupName, String description, Integer precedence)  throws AwsResourceAlreadyExistsException, AwsException {
		
		Objects.requireNonNull(groupName);
		
		CreateGroupRequest request = new CreateGroupRequest()
					.withUserPoolId(getIdentityPoolId())
					.withGroupName(groupName);
		
		if (description != null) {
			request.withDescription(description);
		}
		if (precedence != null) {
			request.withPrecedence(precedence);
		}
		
		try {
			CreateGroupResult result = getCognitoClient().createGroup(request);
			if (result.getSdkHttpMetadata().getHttpStatusCode() != 200) {
				return null;
			}
		}
		catch (GroupExistsException e) {
			throw new AwsResourceAlreadyExistsException(groupName);
		}
		catch (AmazonServiceException e) {
			throw new AwsException(e);
		}
	
		return groupName;
	}
	
	/**
	 * 
	 *
	 * @return 
	 */
	public List<String> getGroups() {
		List<String> ret = new ArrayList<String>();
		
		ListGroupsRequest req = new ListGroupsRequest()
				.withUserPoolId(getIdentityPoolId());
		ListGroupsResult res = getCognitoClient().listGroups(req);
		for (GroupType grp:res.getGroups()) {
			ret.add(grp.getGroupName());
		}
		return ret;
	}
	
	
	/**
	 * 
	 *
	 * @param groupName 
	 * @throws AwsResourceNotFoundException, 
	 * @throws AwsException 
	 */
	public void deleteGroup(String groupName) throws AwsResourceNotFoundException, AwsException {
		DeleteGroupRequest request = new DeleteGroupRequest()
				.withUserPoolId(getIdentityPoolId())
				.withGroupName(groupName);
		try {
			getCognitoClient().deleteGroup(request );
		}
		catch (ResourceNotFoundException e) {
			throw new AwsResourceNotFoundException(groupName);
		}
		catch (AmazonServiceException e) {
			throw new AwsException(e);
		}
	}

	//
	//
	//
	
	/**
	 * 
	 *
	 * @return 
	 */

	
	/**
	 * 
	 *
	 * @param user 
	 * @return 
	 */
	private static List<AttributeType> getUserAttributes(CognitoUser user) {
		List<AttributeType> attributes = new ArrayList<AttributeType>();
		AttributeType attr = null;
		if (user.getEmail() != null) {
			attr = new AttributeType();
			attr.setName(ATTR_EMAIL);
			attr.setValue(user.getEmail());
			attributes.add(attr);
		}
		
		if (user.getFirstName() != null) {
			attr = new AttributeType();
			attr.setName(ATTR_NAME);
			attr.setValue(user.getFirstName());
			attributes.add(attr);
		}
		
		if (user.getMiddleName() != null) {
			attr = new AttributeType();
			attr.setName(ATTR_MIDDLE_NAME);
			attr.setValue(user.getMiddleName());
			attributes.add(attr);
		}
		
		if (user.getAddress() != null) {
			attr = new AttributeType();
			attr.setName(ATTR_ADDRESS);
			attr.setValue(user.getAddress());
			attributes.add(attr);
		}
		
		if (user.getPhoneNumber() != null) {
			attr = new AttributeType();
			attr.setName(ATTR_PHONE_NUMBER);
			attr.setValue(user.getPhoneNumber());
			attributes.add(attr);
		}
		
		if (user.getProfileId() != null) {
			attr = new AttributeType();
			attr.setName(ATTR_PROFILE);
			attr.setValue(user.getProfileId());
			attributes.add(attr);
		}
		
		if (user.getOwnerId() != null) {
			attr = new AttributeType();
			attr.setName(ATTR_OWNERID);
			attr.setValue(user.getOwnerId());
			attributes.add(attr);
		}
		
		if (user.getRoles() != null) {
			attr = new AttributeType();
			attr.setName(ATTR_ROLE);
			String term = "";
			StringBuffer rBuffStr = new StringBuffer();
			for (String role:user.getRoles()) {
				rBuffStr.append(term);
				rBuffStr.append(role);
				term = ",";
			}
			attr.setValue(rBuffStr.toString());
			attributes.add(attr);
		}
		
		/*
		*/
		
		return attributes;
	}

	/**
	 * 
	 *
	 * @param user 
	 * @param attributes 
	 */
	private void setUserAttributes(CognitoUser user, Collection<AttributeType> attributes)  {
		
		for (AttributeType attr:attributes) {
			if (attr.getName().equals(ATTR_EMAIL)) {
				user.setEmail(attr.getValue());
			}
			else if (attr.getName().equals(ATTR_NAME)) {
				user.setFirstName(attr.getValue());
			} 
			else if (attr.getName().equals(ATTR_MIDDLE_NAME)) {
				user.setMiddleName(attr.getValue());
			} 
			else if (attr.getName().equals(ATTR_PHONE_NUMBER)) {
				user.setPhoneNumber(attr.getValue());
			} 
			else if (attr.getName().equals(ATTR_ADDRESS)) {
				user.setAddress(attr.getValue());
			} 
			else if (attr.getName().equals(ATTR_CITY)) {
				user.setCity(attr.getValue());
			} 
			else if (attr.getName().equals(ATTR_ZONEINFO)) {
				user.setZoneinfo(attr.getValue());
			}
			else if (attr.getName().equals(ATTR_OWNERID)) {
				user.setOwnerId(attr.getValue());
			}
			else if (attr.getName().equals(ATTR_PROFILE)) {
				user.setProfileId(attr.getValue());
			}
			else if (attr.getName().equals(ATTR_ROLE)) {
				String []roleStr = attr.getValue().split(",");
				List<String> roleList = Arrays.asList(roleStr);
				user.setRoles(roleList);
			}
		}
		
	}


	
	/**
	 * 
	 *
	 * @param user 
	 * @return 
	 */
	private CognitoUser convertFromUserType(UserType user) {
		CognitoUser ret = new CognitoUser(user.getUsername());
		setUserAttributes(ret, user.getAttributes());
		ret.setEnabled(user.getEnabled());
		ret.setStatus((user.getUserStatus()));
		return ret;
	}
	
}
