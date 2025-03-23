package edu.icet.ecom.service.custom;

import edu.icet.ecom.dto.User;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface UserService extends SuperService<User> {
	Response<User> getByUserName (String name);
	Response<Boolean> deleteByUsername (String adminName);
	Response<String> isAnyUniqueKeyExist (String ...values);
	Response<String> verify (User user);

	Response<Boolean> isUsernameExist (String username);
	Response<Boolean> isEmployeeExistById (Long employeeId);
	Response<Boolean> isEmployeeAlreadyUser (Long employeeId);
}
