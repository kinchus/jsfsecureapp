db = db.getSiblingDB('eHuerta')

db.Role.insertOne(
	{
	    _id : ObjectId("000000000000000000000001"),
	    className : "es.qworks.jsfsecureapp.dao.entity.RoleEntity",
	    name : "ADMINISTRATOR_ROLE"
	}
)

db.Role.insertOne(
	{
	    _id : ObjectId("000000000000000000000002"),
	    className : "es.qworks.jsfsecureapp.dao.entity.RoleEntity",
	    name : "USER_ROLE"
	}
)

db.User.insertOne(
	{
	    _id : ObjectId("000000000000000000000001"),
	    className : "es.qworks.jsfsecureapp.dao.entity.UserEntity",
	    username : "admin",
	    email : "kinchus.dev@gmail.com",
	    firstName : "admin",
	    middleName : "admin",
	    lastName : "admin",
	    address : "none",
	    city : "unknown",
	    phoneNumber : "911111111",
	    enabled : true,
	    status : "",
	    roles : [ 
	        {
	            $ref : "Role",
	            $id : ObjectId("000000000000000000000001")
	        }
	    ]
	}
)

