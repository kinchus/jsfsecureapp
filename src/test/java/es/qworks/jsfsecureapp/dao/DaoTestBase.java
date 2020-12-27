/**
 * 
 */
package es.qworks.jsfsecureapp.dao;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mongobee.Mongobee;
import com.github.mongobee.exception.MongobeeException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import es.qworks.jsfsecureapp.dao.mongo.MongoConfig;
import es.qworks.jsfsecureapp.dao.mongo.MongoManager;

import dev.morphia.Datastore;

/**
 * Data provider for DAO Unit Tests
 * 
@author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 */
public class DaoTestBase {
	
	private static final Logger LOG = LoggerFactory.getLogger(DaoTestBase.class);
	
	private static final String CHANGELOG_PACKAGE = "es.qworks.jsfsecureapp.dao.changelog";
		
	private static MongoManager mongoManager = MongoManager.getInstance();
	private static MongoConfig mongoConfig = MongoManager.getConfig();
	private static MongoClient mongoClient = null;
	private static Datastore datastore = null;
	
		
	/**
	 * Initialize MongoDB instance with default test schemas
	 */
	@Before
	public  void initTestDB() {
		
		LOG.debug("Initializing MongoDB Database");
		
		Mongobee runner = new Mongobee(getMongoClient());
		runner.setDbName(mongoConfig.getDbName());    
		runner.setChangeLogsScanPackage( CHANGELOG_PACKAGE );
		try {
			runner.execute();
		} catch (MongobeeException e) {
			LOG.error("Error initializing database: {}", e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Initialize MongoDB instance with default test schemas
	 */
	@After
	public  void clearTestDB() {
		MongoDatabase db = getMongoClient().getDatabase(mongoConfig.getDbName());
		db.drop();
	}
	
	

	/**
	 * @return the mongoClient
	 */
	public static MongoClient getMongoClient() {
		
		//localDatabase = MongoManager.getConfig().isFileParsed();
		if (mongoClient == null) {
			try {
				mongoClient = mongoManager.getClient();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return mongoClient;
	}

	/**
	 * @return
	 */
	public static Datastore getDatastore() {
		if (datastore == null) {
			datastore = mongoManager.getDatastore(getMongoClient());
		}
		return datastore;
	}
	

}
