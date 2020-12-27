/*
 * Version 1.0 14/3/2015
 * 
 */
package es.qworks.jsfsecureapp.dao.mongo.id;

import java.io.Serializable;

import org.bson.types.ObjectId;


/**
 * Generic interface for entities that must provide an id.
 * Id class must be defined in the implementation classes. 
 * 
 * @param <K> 
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 * @version 1.0
 */
public interface IdEntity<K> extends Serializable {
	
	/** OID_BASE */
	String OID_BASE = "000000000000000000000000";
	
	
	/**
	 * @return
	 */
	K getId();
	
	/**
	 * @param id
	 */
	void setId(K id);

	/**
	 * @param in
	 * @return
	 */
	ObjectId toObjectId();
	
	/**
	 * Builds a valid ObjectID instance from any hex string 
	 * @param str
	 * @return
	 */
	static ObjectId getObjectId(String str) {
		String aux = OID_BASE + str;
		return new ObjectId(aux.substring(aux.length() - OID_BASE.length()));
		
	}

}
