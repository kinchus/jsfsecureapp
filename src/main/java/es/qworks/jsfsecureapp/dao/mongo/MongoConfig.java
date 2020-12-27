/**
 * 
 */
package es.qworks.jsfsecureapp.dao.mongo;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import es.qworks.jsfsecureapp.util.BaseConfig;
import es.qworks.jsfsecureapp.util.StringUtil;




/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class MongoConfig extends BaseConfig {

	private static final Logger LOG = LoggerFactory.getLogger(MongoConfig.class);
	
	/** */
	public static final String 	DBCONFIG_FILE 	= "mongo.properties";
	/** */
	public static final String 	BASE_DOMAIN 	= "com.northstar.dao.entity";
	

	/**
	 * Configuration keys
	 */
	public enum ConfigKey implements BaseConfig.ConfigKey {
		
		/** */
		JNDI_PROPERTY("mongo.jndi", String.class),
		/** */
		DBHOST_PROPERTY("mongo.host", String.class),
		/** */
		DBPORT_PROPERTY("mongo.port", Integer.class),
		/** */
		DBNAME_PROPERTY("mongo.dbname", String.class),
		/** */
		DBAUTH_PROPERTY("mongo.dbauth", String.class),
		/** */
		DBUSER_PROPERTY("mongo.username", String.class),
		/** */
		DBPASS_PROPERTY("mongo.password", String.class);
		
		private String key;
		private Class<?> type;
		
		private ConfigKey(String key, Class<?> type) {
			this.key = key;
			this.type = type;
		}

		/**
		 * @return the key
		 */
		@Override
		public String getKey() {
			return key;
		}
		
		/**
		 * @return the key
		 */
		@Override
		public Class<?> getType() {
			return type;
		}
	}

	
	/** */
	public static String 	DBHOST_DEFAULT = "localhost";
	/** */
	public static Integer 	DBPORT_DEFAULT = 27017;
	/** */
	public static String 	DBNAME_DEFAULT = "admin";
	
	private static MongoConfig instance = null;
	
	private String jndiRes	= null;
	private String dbAuth 	= DBNAME_DEFAULT;
	private String dbName 	= DBNAME_DEFAULT;
	private String dbHost 	= DBHOST_DEFAULT;
	private String dbPortStr = DBPORT_DEFAULT.toString();
	private Integer dbPort 	= DBPORT_DEFAULT;
	private String username = null;
	private String password = null;
	
	private MongoCredential 	credential 	= null;
	private ServerAddress 		srvAddress 	= null;
	private MongoClientOptions 	options 	= null;
	    
    protected boolean fileParsed = false;
    
	
	

	/**
	 * @return the instance
	 */
	public static MongoConfig getInstance() {
		if (instance == null) {
			instance = new MongoConfig(DBCONFIG_FILE);
		}
		return instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public static void setInstance(MongoConfig instance) {
		MongoConfig.instance = instance;
	}

	/**
	 * 
	 */
	public MongoConfig() {
		super();
		parseConfiguration();
		options = MongoClientOptions.builder().build();
	}

	/**
	 * @param configFile
	 */
	public MongoConfig(String configFile) {
		super(configFile);
		parseConfiguration();
		options = MongoClientOptions.builder().build();
	}


	/**
	 * Parse the configuration file and set the connection properties
	 * 
	 */
	public void parseConfiguration() {

		jndiRes =  getProperty(ConfigKey.JNDI_PROPERTY);
		username = getProperty(ConfigKey.DBUSER_PROPERTY, username);
		password = getProperty(ConfigKey.DBPASS_PROPERTY, password);
		dbHost =  getProperty(ConfigKey.DBHOST_PROPERTY, dbHost);
		dbPortStr = getProperty(ConfigKey.DBPORT_PROPERTY, dbPortStr);
		dbAuth =  getProperty(ConfigKey.DBAUTH_PROPERTY, dbAuth);
		dbName =  getProperty(ConfigKey.DBNAME_PROPERTY, dbName);
		
		if (dbPortStr != null) {
			try {
				dbPort = Integer.parseInt(dbPortStr);
			}
			catch (NumberFormatException e) {
				dbPort = 27017;
			}
		}
		
		if (LOG.isTraceEnabled()) {
			StringBuffer msg = new StringBuffer("MongoDB Client configuration:\n");
			msg.append("\tJNDI resource:       " + jndiRes + "\n");
			msg.append("\tConfigured host:     " + dbHost + "\n");
			msg.append("\tConfigured port:     " + dbPortStr + "\n");
			msg.append("\tConfigured db name:  " + dbName + "\n");
			msg.append("\tConfigured username: " + username + "\n");
			LOG.trace(msg.toString());
		}
		
	}


	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @return the dbHost
	 */
	public String getDbHost() {
		return dbHost;
	}

	/**
	 * @param dbHost the dbHost to set
	 */
	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	/**
	 * @return the dbPort
	 */
	public Integer getDbPort() {
		return dbPort;
	}

	/**
	 * @param dbPort the dbPort to set
	 */
	public void setDbPort(Integer dbPort) {
		this.dbPort = dbPort;
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
	 * @return the credential
	 */
	public MongoCredential getCredential() {
		return credential;
	}

	/**
	 * @param credential the credential to set
	 */	
	public void setCredential(MongoCredential credential) {
		this.credential = credential;
	}

	/**
	 * Instantiates a new MongoDB Client
	 * @return the client
	 * @throws Exception
	 */
	public MongoClient buildClient() throws Exception {
		MongoClient ret = null;
		if (!StringUtil.isBlank(username)) { 
			credential = MongoCredential.createCredential(username, dbAuth, password.toCharArray());
		}
		
		try {
			dbPort = Integer.parseInt(dbPortStr);
		}
		catch (NumberFormatException e) {
			LOG.error("Couldn't parse mongo db port {}: {}", dbPortStr, e.getMessage());
		}
		
		if (dbPort == null) {
			srvAddress = new ServerAddress(dbHost);
		}
		else {
			srvAddress = new ServerAddress(dbHost, dbPort);
		}
		
		if (!StringUtil.isBlank(jndiRes)) {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			ret = (MongoClient) envContext.lookup(jndiRes);
			LOG.debug("Got MongoDB client from JNDI java:/comp/env/{} (authenticated:{})", 
					jndiRes, 
					ret.getCredential());
			
		}
		else if (credential != null) {
			ret = new MongoClient(srvAddress, (credential), options);
			LOG.debug("Started MongoDB client for {}:{}/{}", dbHost, dbPort, dbName);
		}
		else {
			ret = new MongoClient(srvAddress, options);
			LOG.debug("Started MongoDB client for {}:{}/{}", dbHost, dbPort, dbName);
		}

		return ret;
	}


	/**
	 * @param dbName
	 * @param dbHost
	 * @param dbPort 
	 * @param userName
	 * @param password
	 * @return 
	 */
	public MongoClient buildClient(String dbName, String dbHost, Integer dbPort, String userName, String password) {
		MongoClient ret = null;
		MongoCredential credential = null;
		ServerAddress sAddress = null;
		if (userName != null) { 
			credential = MongoCredential.createCredential(userName, dbName, password.toCharArray());
		}
		
		sAddress = new ServerAddress(dbHost, dbPort);
		
		LOG.debug("Building MongoDB client for {}:{}/{}", dbHost, dbPort, dbName);
		
		if (credential != null) {
			ret = new MongoClient(sAddress, credential, options);
		}
		else {
			ret = new MongoClient(sAddress);
		}

		return ret;
	}

}
