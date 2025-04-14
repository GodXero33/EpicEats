package edu.icet.ecom.repository.custom.security;

import edu.icet.ecom.entity.security.UserEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface UserRepository extends CrudRepository<UserEntity> {
	Response<UserEntity> getByUserName (String name);
	Response<UserEntity> getByUserNameForAuth (String name);
	Response<Boolean> isUsernameExist (String username);
	Response<Boolean> isEmployeeExistById (Long employeeId);
	Response<Boolean> isEmployeeAlreadyUser (Long employeeId);
}
