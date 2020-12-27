package es.qworks.jsfsecureapp.service.mapper;

import java.util.ArrayList;
import java.util.List;

import es.qworks.jsfsecureapp.dao.entity.RoleEntity;
import es.qworks.jsfsecureapp.dao.entity.UserEntity;
import es.qworks.jsfsecureapp.model.Role;
import es.qworks.jsfsecureapp.model.User;



/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class ServiceMapper {
	
	
	/** INSTANCE */
	public static final ServiceMapper INSTANCE = new ServiceMapper();
	
	/**
	 * @return
	 */
	public static ServiceMapper getInstance() {
		return INSTANCE;
	}
	
	
	private ServiceMapper() {
		
	}
	

    /**
     * @param entity
     * @return
     */
	public Role mapRole(RoleEntity entity) {
    	Role dto = new Role();
    	dto.setId(entity.getId());
    	dto.setName(entity.getName());
   
    	return dto;
    }

    /**
     * @param dto
     * @return
     */
	public RoleEntity mapRole(Role dto){
    	RoleEntity entity = new RoleEntity();
    	entity.setId(dto.getId().toString());
    	entity.setName(dto.getName());
    	return entity;
    }

    /**
     * @param entities
     * @return
     */
	public List<Role> mapRoleEntities(List<RoleEntity> entities) {
    	List<Role> dtos = new ArrayList<Role>();
    	entities.forEach(ent -> dtos.add(mapRole(ent)));
    	return dtos;
    }
    
    /**
     * @param dtos
     * @return
     */
	public List<RoleEntity> mapRoleDtos(List<Role> dtos) {
		List<RoleEntity> entities = new ArrayList<RoleEntity>();
    	dtos.forEach(ent -> entities.add(mapRole(ent)));
    	return entities;
    }

    /**
     * @param entity
     * @return
     */
	public User mapUser(UserEntity entity) {
    	User dto = new User();
    	dto.setId( entity.getId() );
    	dto.setUsername( entity.getUsername() );
    	dto.setEmail( entity.getEmail() );
    	dto.setFirstName( entity.getFirstName() );
    	dto.setLastName( entity.getLastName() );
    	dto.setAccountId( entity.getAccountId() );
    	dto.setGroups( entity.getGroups() );
    	dto.setRoles( mapRoleEntities(entity.getRoles()) );
    	return dto;
    }
    
    /**
     * @param entities
     * @return
     */
	public List<User> mapUserEntities(List<UserEntity> entities) {
    	List<User> dtos = new ArrayList<User>();
    	entities.forEach(ent -> dtos.add(mapUser(ent)));
    	return dtos;
    }
 
 
    /**
     * @param dto
     * @return
     */
	public UserEntity mapUser(User dto) {
    	UserEntity ent = new UserEntity();
    	ent.setId( dto.getId().toString() );
    	ent.setUsername( dto.getUsername() );
    	ent.setEmail( dto.getEmail() );
    	ent.setFirstName( dto.getFirstName() );
    	ent.setLastName( dto.getLastName() );
    	ent.setAccountId( dto.getAccountId() );
    	ent.setGroups( dto.getGroups() );
    	ent.setRoles( mapRoleDtos(dto.getRoles()) );
    	return ent;
    }
    
    /**
     * @param dtos
     * @return
     */
    public List<UserEntity> mapUserDtos(List<User> dtos) {
    	List<UserEntity> entities = new ArrayList<UserEntity>();
    	dtos.forEach(ent -> entities.add(mapUser(ent)));
    	return entities;
    }
 


}