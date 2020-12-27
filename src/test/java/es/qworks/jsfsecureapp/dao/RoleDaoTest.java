/*
 * 
 *
 * @author <a href="mailto:garciadjx@gmail.com">J.M. García</a>
 */
package es.qworks.jsfsecureapp.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import es.qworks.jsfsecureapp.dao.entity.RoleEntity;
import es.qworks.jsfsecureapp.dao.mongo.RoleDaoImpl;


/**
 * 
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 */
public class RoleDaoTest extends DaoTestBase {

	private RoleDao roleDao = new RoleDaoImpl(getDatastore());

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * Test find by name.
	 */
	@Test
	public void testFindByName() {
		RoleEntity role = roleDao.findByName("TEST_ROLE");
		assertNotNull(role);
	}


	/**
	 * Test find all.
	 */
	@Test
	public void testFindAll() {
		List<RoleEntity> roles = roleDao.findAll();
		assertNotNull(roles);
		assertFalse(roles.isEmpty());
		
	}

	/**
	 * Test find by id.
	 */
	@Test
	public void testFindById() {
		RoleEntity role = roleDao.findById("1");
		assertNotNull(role);
	}

	/**
	 * Test find by ids.
	 */
	@Test
	public void testFindByIds() {
		List<String> ids = Arrays.asList("1", "2", "3", "4");
		List<RoleEntity> roles = roleDao.findByIds(ids);
		assertNotNull(roles);
		assertEquals(4, roles.size(), 0);
	}

	/**
	 * Test save.
	 */
	@Test
	public void testSave() {
		RoleEntity r = new RoleEntity("new");
		r = roleDao.save(r);
		assertNotNull(r);
		assertNotNull(r.getId());
	}

	/**
	 * Test delete T.
	 */
	@Test
	public void testDeleteT() {
		RoleEntity role = roleDao.findById("1");
		int n = roleDao.delete(role);
		assertEquals(1, n, 0);
	}

}
