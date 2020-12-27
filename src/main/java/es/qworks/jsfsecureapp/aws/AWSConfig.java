/**
 * 
 */
package es.qworks.jsfsecureapp.aws;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import es.qworks.jsfsecureapp.util.StringUtil;



// TODO: Auto-generated Javadoc
/**
 * The Class AWSConfig.
 *
@author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 */
public class AWSConfig {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(AWSConfig.class);

	/** The Constant AWS_PROPS_FILE. */
	public static final String AWS_PROPS_FILE	= "aws.properties";
	
	/** The Constant AWS_REGION. */
	public static final String AWS_REGION 		= "aws.region";
	
	/** The Constant AWS_PROFILE. */
	public static final String AWS_PROFILE 		= "aws.profile";
	
	/** The Constant AWS_ACCOUNT_ID. */
	public static final String AWS_ACCOUNT_ID	= "aws.account_id";
	
	/** The Constant AWS_KEY_ID. */
	public static final String AWS_KEY_ID 		= "aws.key_id";
	
	/** The Constant AWS_KEY_SECRET. */
	public static final String AWS_KEY_SECRET 	= "aws.key_secret";
	
	/** The Constant AWS_IoT_ENDPOINT. */
	public static final String AWS_IoT_ENDPOINT = "aws.iot.mqtt_endpoint";
	
	/** The Constant AWS_COGNITO_USERPOOLID. */
	public static final String AWS_COGNITO_USERPOOLID 	= "aws.cognito.pool_id";
	
	/** The Constant AWS_COGNITO_POOLCLIENTID. */
	public static final String AWS_COGNITO_POOLCLIENTID 	= "aws.cognito.app_client_id";

	/** The Constant AWS_S3_BUCKET. */
	public static final String AWS_S3_BUCKET 	= "aws.s3.backend_bucket";
	
	
	/** The Constant LAMBDA_FUNCTION_VAR. */
	private static final String LAMBDA_FUNCTION_VAR = "AWS_LAMBDA_FUNCTION_NAME";
	
	/** The Constant LAMBDA_REGION_VAR. */
	private static final String LAMBDA_REGION_VAR = "AWS_REGION";
	
	/** The Constant instance. */
	private static final AWSConfig instance = new AWSConfig();
	
	/** The properties. */
	private Properties properties = new Properties(); 	
	
	/** The region. */
	private String region = null;
	
	/** The profile. */
	private String profile = null;
	
	/** The account id. */
	private String accountId = null;
	
	/** The is EC 2 instance. */
	private Boolean isEC2Instance = null;
	
	/** The is lambda function. */
	private Boolean isLambdaFunction = null;
	
	/** The lambda function name. */
	private String lambdaFunctionName = null;
	
	/** The credentials. */
	private AWSCredentials credentials = null;
	
	
	/**
	 * Instantiates a new AWS config.
	 */
	private AWSConfig() {
		loadProperties(AWS_PROPS_FILE);
		initRegion();
	}

	
	/**
	 * Gets the single instance of AWSConfig.
	 *
	 * @return single instance of AWSConfig
	 */
	public static AWSConfig getInstance() {
		return instance;
	}
	
	/**
	 * Gets the region.
	 *
	 * @return the region
	 */
	public String getRegion() {
		if (region == null) {
			region = getProperty(AWS_REGION);
		}
		return region;
	}

	/**
	 * Gets the profile.
	 *
	 * @return the profile
	 */
	public String getProfile() {
		if (profile == null) {
			profile = getProperty(AWS_PROFILE);
		}
		return profile;
	}

	/**
	 * Gets the account id.
	 *
	 * @return the account id
	 */
	public String getAccountId() {
		if (accountId == null) {
			accountId = getProperty(AWS_ACCOUNT_ID);
		}
		return accountId ;
	}


	/**
	 * Gets the lambda function name.
	 *
	 * @return the lambdaFunctionName
	 */
	public String getLambdaFunctionName() {
		return lambdaFunctionName;
	}


	/**
	 * Checks if is EC 2 instance.
	 *
	 * @return the isEC2Instance
	 */
	public Boolean isEC2Instance() {
		return isEC2Instance;
	}
	
	/**
	 * Checks if is lambda function.
	 *
	 * @return the isLambdaInstance
	 */
	public Boolean isLambdaFunction() {
		if (isLambdaFunction == null) {
			lambdaFunctionName = System.getenv(LAMBDA_FUNCTION_VAR);
			if (lambdaFunctionName != null) {
				LOG.trace("Running from Lambda function \"{}\"", lambdaFunctionName);
				isLambdaFunction = true;
			}
			else {
				isLambdaFunction = false;
			}
		}
		return isLambdaFunction;
	}
	
	
	/**
	 * Retrieve the AWS account credentials set in configuration.
	 *
	 * @return the credentials
	 */
	public  AWSCredentials getCredentials() {
		
		if (credentials == null) {
			String keyId = getInstance().getProperty(AWSConfig.AWS_KEY_ID);
			String secret = getInstance().getProperty(AWSConfig.AWS_KEY_SECRET);
			
			if (!StringUtil.isBlank(keyId)) {
				LOG.debug("Get credentials from specified key");
				credentials = new BasicAWSCredentials(keyId, secret);
			}
		}
		
		return credentials;
		
	}


    /**
     * Gets the property.
     *
     * @param property the property
     * @param defaultValue the default value
     * @return the property
     */
	public String getProperty(String property, String defaultValue) {
    	String val = getProperty(property);
    	if (StringUtil.isBlank(val)) {
    		val = defaultValue;
    	}
    	return val;
    }
 
    /**
     * Gets the property.
     *
     * @param property the property
     * @return the property
     */
	public String getProperty(String property) {
		
		String val = System.getenv(property);
		if (val != null) {
			return val;
		}
		
    	if (properties == null) {
    		return null;
    	}
    	
    	val = properties.getProperty(property); 
    	
    	/*
    	if (property.endsWith(SECRETKEY_SUFFIX)) {
    		int keyIndx = val.indexOf("#");
    		String secretName = val;
    		String secretKey = null;
    	    if (keyIndx > -1) {
    			secretName = val.substring(0, keyIndx);
    			secretKey = val.substring(keyIndx+1);
    			val = getSecretsService().getSecret(secretName, secretKey);
    		}
    	    else {
    	    	val = getSecretsService().getSecret(secretName);
    	    }
    		
    	}
    	*/
    	
    	return val;
    	
    }
    

    /**
     * Sets the property.
     *
     * @param property the property
     * @param value the value
     */
	public void setProperty(String property, Object value) {
		if (value != null) {
			properties.setProperty(property, value.toString());
		}
    }
	
	
	
	/**
	 * Load properties.
	 *
	 * @param filename the filename
	 */
	private void loadProperties(String filename) {
    	URL res = AWSConfig.class.getClassLoader().getResource(filename);
    	if (res  == null) {
    		LOG.error("Configuration file {} not found", filename);
        	return;
    	}
    	LOG.trace("Loading configuration from file {}", filename);
        try {
			InputStream stream = res.openStream();
			properties.load(stream);
		} catch (IOException e) {
			LOG.error("Error loading configuration: {}", filename);
		}
    	
	}    
	
	/**
	 * Inits the region.
	 */
	private void initRegion() {
		Region awsRegion = null;
		if (isLambdaFunction()) {
			region = System.getenv(LAMBDA_REGION_VAR);
		}
		else {
			region = getProperty(AWS_REGION);
		}
		
		if (region != null) {
			LOG.trace("AWS Region set to {}", region);
			return;
		}
		
		awsRegion = Regions.getCurrentRegion();
		if (awsRegion != null) {
			LOG.trace("AWS Region set to {}", region);
			region = awsRegion.getName();
			isEC2Instance = true;
		}
	}




}
