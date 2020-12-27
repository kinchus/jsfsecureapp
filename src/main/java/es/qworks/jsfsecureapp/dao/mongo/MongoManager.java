/**
 * 
 */
package es.qworks.jsfsecureapp.dao.mongo;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import dev.morphia.Datastore;
import dev.morphia.Morphia;

/**
@author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class MongoManager {

	
	private static final Logger LOG = LoggerFactory.getLogger(MongoManager.class);
	
	private static MongoConfig	config = null;
	private static MongoManager instance = null;
	
	private MongoClient mongo = null;
	private Morphia 	morphia	= null;
	private Datastore 	datastore = null;
	
	/**
	 * @return the instance
	 */
	public static MongoManager getInstance() {
		if (instance == null) {
			instance = new MongoManager();
		}
		return instance;
	}
	
	/**
	 * @param configFile 
	 * @return the instance
	 */
	public static MongoManager getInstance(String configFile) {
		config = new MongoConfig(configFile);
		instance = new MongoManager();
		return instance;
	}

	/**
	 * @return the config
	 */
	public static MongoConfig getConfig() {
		if (config == null) {
			config = MongoConfig.getInstance();
		}
		return config;
	}

	/**
	 * @param configFile 
	 * @return the initialized MongoConfig instance
	 */
	public static MongoConfig getConfig(String configFile) {
		if (config == null) {
			config = new MongoConfig(configFile);
		}
		return config;
	}

	/**
	 * @param cfg
	 */
	public static void setConfig(MongoConfig cfg) {
		config = cfg;
	}
	
	
	private MongoManager() {}

	
	/**
	 * @return
	 * @throws RuntimeException
	 */
	public synchronized MongoClient getClient() throws RuntimeException {
		
		if (mongo == null) {
			
			try {
				mongo = getConfig().buildClient();
				LOG.debug("Initialized MongoDB Client!!");
			} catch (Exception e) {
				LOG.error("Exception thrown building MongoDB client: {}", e);
				e.printStackTrace();
			}
		}
		
		return mongo;
	}
	
	/**
	 * @param jndi 
	 * @return
	 * @throws NamingException 
	 */
	public MongoClient getClient(String jndi) throws NamingException {
		Context ctx = new InitialContext(); 
		mongo = (MongoClient) ctx.lookup(jndi);
		return mongo;
	}
	
	/**
	 * @param client
	 */
	public void setClient(MongoClient client) {
		this.mongo = client;
	}

	/**
	 * @return
	 */
	public MongoDatabase getDatabase() {
		return getClient().getDatabase(config.getDbName());
	}

	
	/**
	 * @return
	 */
	public Datastore getDatastore() {
		if (datastore == null) {
			LOG.debug("Get Morphia datastore from client");
			datastore = getDatastore(getClient());
			datastore.ensureIndexes();
		}

		return datastore;
	}
	
	/**
	 * @param mongoClient
	 * @return
	 */
	public Datastore getDatastore(MongoClient mongoClient) {
		if (datastore == null) {
			datastore = getMorphia().createDatastore(mongoClient, getConfig().getDbName());
			datastore.ensureIndexes();
		}
		return datastore;
	}
	
	/**
	 * @param db
	 * @return
	 */
	public Datastore getDatastore(DB db) {
		
		if (datastore == null) {
			LOG.debug("Starting DataStore on DB {}", db.getName());
			datastore = getMorphia().createDatastore(getClient(), db.getName());
		}

		return datastore;
	}
	
	/**
	 * @param ds
	 */
	public void setDatastore(Datastore ds) {
		this.datastore = ds;
	}


	/**
	 * @return
	 */
	public Morphia getMorphia() {
	
		if (morphia == null) {
			LOG.trace("Starting Morphia");
			morphia = new Morphia();
			LOG.trace("Mapping entities from package {}", MongoConfig.BASE_DOMAIN);
			morphia.mapPackage(MongoConfig.BASE_DOMAIN, true);
		}

		return morphia;
	}

	
	
	/**
	 * 
	 */
	public void close() {
		if (mongo != null) {
			try {
				LOG.trace("Closing MongoDB client");
				mongo.close();
				LOG.trace("Null'ing the connection dependency objects");
				mongo = null;
				morphia = null;
				datastore = null;
			} catch (Exception e) {
				LOG.error("An error occurred when closing the MongoDB connection: {}", e.getMessage());
			}
		} else {
			LOG.warn("mongo object was null, wouldn't close connection");
		}
	}

}
