/*
 * 
 *
 * @author <a href="mailto:garciadjx@gmail.com">J.M. García</a>
 */
package es.qworks.jsfsecureapp.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import es.qworks.jsfsecureapp.dao.entity.UserEntity;
import es.qworks.jsfsecureapp.dao.mongo.UserDaoImpl;


/**
 * 
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 */
public class UserDaoTest extends DaoTestBase {

	private UserDao userDao = new UserDaoImpl(getDatastore());

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * Test find by username.
	 */
	@Test
	public void testFindByUsername() {
		UserEntity user = userDao.findByUsername("admin");
		assertNotNull(user);
	}

	/**
	 * Test find by email.
	 */
	@Test
	public void testFindByEmail() {
		UserEntity user = userDao.findByEmail("admin@admin.net");
		assertNotNull(user);
	}


	/**
	 * Test find all.
	 */
	@Test
	public void testFindAll() {
		List<UserEntity> users = userDao.findAll();
		assertNotNull(users);
	}

	/**
	 * Test find by id.
	 */
	@Test
	public void testFindById() {
		UserEntity user = userDao.findById("000000000000000000000001");
		assertNotNull(user);
	}

	/**
	 * Test find by ids.
	 */
	@Test
	public void testFindByIds() {
		List<String> ids = Arrays.asList("1", "2", "3", "4");
		List<UserEntity> users = userDao.findByIds(ids);
		assertNotNull(users);
	}

	/**
	 * Test save.
	 */
	@Test
	public void testSave() {
		UserEntity user = new UserEntity("new");
		user = userDao.save(user);
		assertNotNull(user);
	}

	/**
	 * Test delete T.
	 */
	@Test
	public void testDeleteT() {
		UserEntity user = userDao.findById("1");
		int n = userDao.delete(user);
		assertEquals(1, n, 0);
	}

}
