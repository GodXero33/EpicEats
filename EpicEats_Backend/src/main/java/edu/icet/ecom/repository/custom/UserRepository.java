package edu.icet.ecom.repository.custom;

import edu.icet.ecom.entity.UserEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface UserRepository extends CrudRepository<UserEntity> {
	Response<UserEntity> getByUserName (String name);
	Response<Boolean> deleteByUserName (String name);
	Response<Boolean> isExistsByFieldName (String fieldName, Object value);

	Response<Boolean> isUsernameExist (String username);
	Response<Boolean> isEmployeeExistById (Long employeeId);
	Response<Boolean> isEmployeeAlreadyUser (Long employeeId);
}
