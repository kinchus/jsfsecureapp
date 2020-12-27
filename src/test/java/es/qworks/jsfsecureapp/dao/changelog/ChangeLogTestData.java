package es.qworks.jsfsecureapp.dao.changelog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.BsonArray;
import org.bson.BsonValue;
import org.bson.codecs.BsonArrayCodec;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import es.qworks.jsfsecureapp.dao.DaoTestBase;
import es.qworks.jsfsecureapp.dao.entity.RoleEntity;
import es.qworks.jsfsecureapp.dao.entity.UserEntity;
import es.qworks.jsfsecureapp.dao.mongo.MongoManager;
import es.qworks.jsfsecureapp.util.StringUtil;


/**
 * 
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 */
@ChangeLog(order = "001")
public class ChangeLogTestData {
	
	private static final Logger LOG = LoggerFactory.getLogger(ChangeLogTestData.class);
	
	/** */
	private static final String ROLES_COLLECTION = "Roles";
	/** */
	private static final String USERS_COLLECTION = "User";
	
	/** */
	private static final String ROLES_JSON_COLLECTION = "db/collections/json/roles.json";
	/** */
	private static final String USERS_JSON_COLLECTION = "db/collections/json/users.json";
	
	/**
	 * Roles collection schema
	 * @param db
	 */
	@ChangeSet(order = "001", id = "RolesTestData", author = "jmgarcia")
	public void initRolesCollection(DB db) {
		
		DBCollection col = db.getCollection(ROLES_COLLECTION);
		col.drop();
		
		try {
			List<RoleEntity> list = getDataModelFromJSONFile(ROLES_JSON_COLLECTION, RoleEntity.class);
			for (RoleEntity entity:list) {
				MongoManager.getInstance().getDatastore().save(entity);
				if (LOG.isTraceEnabled()) {
					LOG.trace("Added role: {} (id:{})", entity.getName(), entity.getId());
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage());
		}
	}
	
	
	/**
	 * Roles collection schema
	 * @param db
	 */
	@ChangeSet(order = "002", id = "UsersTestData", author = "jmgarcia")
	public void initUsersCollection(DB db) {
		
		DBCollection col = db.getCollection(USERS_COLLECTION);
		col.drop();
		
		try {
			List<UserEntity> list = getDataModelFromJSONFile(USERS_JSON_COLLECTION, UserEntity.class);
			for (UserEntity entity:list) {
				MongoManager.getInstance().getDatastore().save(entity);
				if (LOG.isTraceEnabled()) {
					LOG.trace("Added role: {} (id:{})", entity.getUsername(), entity.getId());
				}
			}			
			
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage());
		}
	}
	

	
	/**
	 * @param <T>
	 * @param filename
	 * @param clazz
	 * @return
	 * @throws FileNotFoundException
	 */
	public static <T> List<T> getDataModelFromJSONFile(String filename, Class<T> clazz) throws FileNotFoundException {
		final CodecRegistry codecRegistry = CodecRegistries.fromProviders(Arrays.asList(new ValueCodecProvider(),
				new BsonValueCodecProvider(),
				new DocumentCodecProvider()));
		List<T> ret = new ArrayList<T>();
		
		ObjectMapper mapper = new ObjectMapper();
		InputStream is = DaoTestBase.class.getClassLoader().getResourceAsStream(filename);
		String json = StringUtil.file2String(is);
		JsonReader reader = new JsonReader(json);
		BsonArrayCodec arrayReader = new BsonArrayCodec(codecRegistry);

		BsonArray docArray = arrayReader.decode(reader, DecoderContext.builder().build());

		for (BsonValue doc : docArray.getValues()) {
			T aux = null;
			try {
				aux = mapper.readValue(doc.toString(), clazz);
				ret.add(aux);
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		}
		return ret;
	}


	
}