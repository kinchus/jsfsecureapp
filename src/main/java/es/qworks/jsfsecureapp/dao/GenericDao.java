/**
 * 
 */
package es.qworks.jsfsecureapp.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.WriteResult;

import es.qworks.jsfsecureapp.dao.mongo.MongoAdaptor;
import es.qworks.jsfsecureapp.dao.mongo.MongoManager;
import es.qworks.jsfsecureapp.dao.mongo.id.IdEntity;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import dev.morphia.query.internal.MorphiaCursor;

/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 * @param <T>
 * @param <K>
 */
public class GenericDao<T extends IdEntity<K>,K extends Serializable> 
		extends MongoAdaptor 
		implements Dao<T, K>, Serializable {

	private static final long serialVersionUID = 6314714981317571793L;
	
	private Class<T> persistentClass = null;
	
	
	/**
	 * 
	 */
	public GenericDao() {
		super();
	}

	/**
	 * @param clazz
	 */
	public GenericDao(Class<T> clazz) {
		super(MongoManager.getInstance().getDatastore());
		this.persistentClass = clazz;
	}
	
	/**
	 * @param clazz
	 * @param datastore
	 */
	public GenericDao(Class<T> clazz, Datastore datastore) {
		super(datastore);
		this.persistentClass = clazz;
	}
	
	/**
	 * @return
	 * @see es.qworks.jsfsecureapp.dao.Dao#findAll()
	 */
	@Override
	public List<T> findAll() {
		// QueryResults<T> res = getDao().find();
		MorphiaCursor<T> cur = getQuery().find();
		// cur.getServerAddress();
		// cur.getServerCursor();
		return cur.toList();
	}

	/**
	 * @param id
	 * @return
	 * @see es.qworks.jsfsecureapp.dao.Dao#findById(java.io.Serializable)
	 */
	@Override
	public T findById(K id) {
		ObjectId oid = IdEntity.getObjectId(id.toString());
		return getQuery().field("_id").equal(oid).first();
	}
	
	
	/**
	 * @param ids
	 * @return
	 * @see es.qworks.jsfsecureapp.dao.Dao#findByIds(java.util.List)
	 */
	@Override
	public List<T> findByIds(List<K> ids) {
		List<ObjectId> oids = new ArrayList<ObjectId>();
		ids.forEach(id -> oids.add(IdEntity.getObjectId(id.toString())));
		return list(getQuery().field("_id").in(oids));
	}

	/**
	 * @param object
	 * @return
	 * @see es.qworks.jsfsecureapp.dao.Dao#save(es.qworks.jsfsecureapp.dao.mongo.id.IdEntity)
	 */
	@Override
	public T save(T object) {
		getDatastore().save(object);
		return object;
	}
	
	/**
	 * @param object
	 * @return
	 * @see es.qworks.jsfsecureapp.dao.Dao#delete(es.qworks.jsfsecureapp.dao.mongo.id.IdEntity)
	 */
	@Override
	public int delete(T object) {
		WriteResult res = getDatastore().delete(object);
		return res.getN();
	}

	/**
	 * @return
	 */
	protected Query<T> getQuery() {
		return getQuery(persistentClass);
	}

	
	/**
	 * @return
	 */
	protected UpdateOperations<T> createUpdateOperations() {
		return createUpdateOperations(persistentClass);
	}

}
