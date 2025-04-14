package edu.icet.ecom.service.custom.security;

import edu.icet.ecom.dto.security.TokenAndUser;
import edu.icet.ecom.dto.security.User;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface UserService extends SuperService<User> {
	Response<TokenAndUser> verify (User user);
	Response<Boolean> isUsernameExist (String username);
	Response<Boolean> isEmployeeExistById (Long employeeId);
	Response<Boolean> isEmployeeAlreadyUser (Long employeeId);
}
