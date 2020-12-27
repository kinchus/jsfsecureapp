package es.qworks.jsfsecureapp.dao.mongo;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;

/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class MongoAdaptor implements Serializable {

	private static final long serialVersionUID = -7698379745360235254L;
	
	private Datastore datastore = null;

	/**
	 *
	 */
	public MongoAdaptor() {
		super();
	}
	
	/**
	 * @param datastore
	 */
	public MongoAdaptor(Datastore datastore) {
		super();
		this.datastore = datastore;
	}

	protected Datastore getDatastore() {
		return datastore;
	}

	protected void setDatastore(Datastore ds) {
		this.datastore = ds;
	}

	protected <T> List<T> list(Query<T> query) {
		return query.find().toList();
	}


    protected <T> int delete(Query<T> query) {
		WriteResult res = getDatastore().delete(query);
		return res.getN();
	}
    
    protected <T> Iterator<T> find(Query<T> query) {
		return query.find();
	}
	
	/**
     * Returns a new query bound to the collection (a specific {@link DBCollection})
     *
     * @param collection The collection to query
     * @param <T> the type of the query
     * @return the query
     */
	protected <T> Query<T> getQuery(Class<T> collection) {
		return getDatastore().createQuery(collection);
	}
	
	
	/**
     * The builder for update operations on a collection
     *
     * @param collection the collection to update
     * @param <T>   the type to update
     * @return the new UpdateOperations instance
     */
    protected <T> UpdateOperations<T> createUpdateOperations(Class<T> collection) {
		return getDatastore().createUpdateOperations(collection);
	}	

	protected ObjectId newObjectId() {
		ObjectId oid = ObjectId.get();
		byte[] srcBuff =  oid.toByteArray();
		// Copy the timestamp to the 5..8 indexes
		srcBuff[5] = srcBuff[0];
		srcBuff[6] = srcBuff[1];
		srcBuff[7] = srcBuff[2];
		srcBuff[8] = srcBuff[3];
		// Zero the first 5 bytes
		srcBuff[0] = 0;
		srcBuff[1] = 0;
		srcBuff[2] = 0;
		srcBuff[3] = 0;
		srcBuff[4] = 0;
		return new ObjectId(srcBuff);
	}

}