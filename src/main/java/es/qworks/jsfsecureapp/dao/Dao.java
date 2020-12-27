package es.qworks.jsfsecureapp.dao;

import java.io.Serializable;
import java.util.List;

import es.qworks.jsfsecureapp.dao.mongo.id.IdEntity;




/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 * 
 * Definition of the the minimal contract for Dao classes
 *
 * @param <T>
 * @param <K>
 */
public interface Dao<T extends IdEntity<K>, K extends Serializable> extends Serializable {


	/**
	 * Find all entities in the collection
	 * @return List containing the found Object entities
	 */
	List<T> findAll();

	/**
	 * @param id
	 * @return Object entity or NULL if none found
	 */
	T findById(K id);
	
	/**
	 * @param ids
	 * @return
	 */
	List<T> findByIds(List<K> ids);

	/**
	 * @param entity
	 * @return updated object
	 */
	T save(T entity);

	/**
	 * @param entity
	 * @return number of deleted entities
	 */
	int delete(T entity);
	
}