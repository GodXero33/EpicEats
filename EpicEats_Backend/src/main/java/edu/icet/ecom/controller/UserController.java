package edu.icet.ecom.controller;

import edu.icet.ecom.dto.User;
import edu.icet.ecom.response.UserResponse;
import edu.icet.ecom.service.custom.UserService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.ResponseType;
import edu.icet.ecom.config.apidoc.user.UserDeleteApiDoc;
import edu.icet.ecom.config.apidoc.user.UserLoginApiDoc;
import edu.icet.ecom.config.apidoc.user.UserRegisterApiDoc;
import edu.icet.ecom.config.apidoc.user.UserUpdateApiDoc;
import edu.icet.ecom.validation.ValidationErrorsHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "Admin Management", description = "APIs for managing admins(users)")
public class UserController {
	private final UserService userService;
	private final BCryptPasswordEncoder passwordEncoder;

	private <T> UserResponse<T> getServerErrorResponse () {
		return new UserResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, "Server error");
	}

	@UserLoginApiDoc
	@PostMapping("/login")
	public UserResponse<String> login (@RequestBody User user) {
		final Response<String> response = this.userService.verify(user);

		return response.getStatus() == ResponseType.SUCCESS ?
			new UserResponse<>(HttpStatus.OK, response.getData(), "Authorized") :
			new UserResponse<>(HttpStatus.UNAUTHORIZED, null, "Unauthorized");
	}

	@PostMapping("/register")
	@UserRegisterApiDoc
	public UserResponse<User> register (@Valid @RequestBody User user, BindingResult result) {
		if (result.hasErrors()) return new UserResponse<>(HttpStatus.BAD_REQUEST, null, "Invalid user details", ValidationErrorsHelper.getValidationErrors(result));
		if (user.getPassword() == null) return new UserResponse<>(HttpStatus.BAD_REQUEST, null, "Invalid user details", "Password is required");

		final Response<Boolean> usernameExistResponse = this.userService.isUsernameExist(user.getUsername());

		if (usernameExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.getServerErrorResponse();
		if (usernameExistResponse.getStatus() == ResponseType.FOUND) return new UserResponse<>(HttpStatus.CONFLICT, null, "Username is already taken");

		final Response<Boolean> employeeAlreadyUserResponse = this.userService.isEmployeeAlreadyUser(user.getEmployeeId());

		if (employeeAlreadyUserResponse.getStatus() == ResponseType.SERVER_ERROR) return this.getServerErrorResponse();
		if (employeeAlreadyUserResponse.getStatus() == ResponseType.FOUND) return new UserResponse<>(HttpStatus.CONFLICT, null, "The target employee is already has user account");

		final Response<Boolean> employeeExistResponse = this.userService.isEmployeeExistById(user.getEmployeeId());

		if (employeeExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.getServerErrorResponse();
		if (employeeExistResponse.getStatus() == ResponseType.NOT_FOUND) return new UserResponse<>(HttpStatus.BAD_REQUEST, null, "No employee has found with given employeeId");

		user.setPassword(this.passwordEncoder.encode(user.getPassword()));

		final Response<User> response = this.userService.add(user);

		if (response.getData() != null) response.getData().setPassword(null);

		return response.getStatus() == ResponseType.CREATED ?
			new UserResponse<>(HttpStatus.OK, response.getData(), "User added") :
			this.getServerErrorResponse();
	}

	@UserUpdateApiDoc
	@PutMapping("/update")
	public UserResponse<User> update (@Valid @RequestBody User user, BindingResult result) {
		if (result.hasErrors()) return new UserResponse<>(HttpStatus.BAD_REQUEST, null, "Invalid user details", ValidationErrorsHelper.getValidationErrors(result));
		if (user.getEmployeeId() == null) return new UserResponse<>(HttpStatus.BAD_REQUEST, null, "Invalid user details", "User id required for update");

		final Response<Boolean> userExistResponse = this.userService.isUsernameExist(user.getUsername());

		if (userExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.getServerErrorResponse();
		if (userExistResponse.getStatus() == ResponseType.FOUND) return new UserResponse<>(HttpStatus.CONFLICT, null, "Username is already taken");

		final Response<User> response = this.userService.update(user);

		return switch (response.getStatus()) {
			case UPDATED -> new UserResponse<>(HttpStatus.OK, response.getData(), "User updated");
			case SERVER_ERROR -> this.getServerErrorResponse();
			default -> new UserResponse<>(HttpStatus.NOT_MODIFIED, null, "User update failed");
		};
	}

	@UserDeleteApiDoc
	@DeleteMapping("/delete/{username}")
	public UserResponse<Object> delete (@PathVariable("username") String username) {
		final Response<Boolean> response = this.userService.deleteByUsername(username);

		return switch (response.getStatus()) {
			case DELETED -> new UserResponse<>(HttpStatus.OK, null, "User deleted");
			case SERVER_ERROR -> this.getServerErrorResponse();
			default -> new UserResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete user");
		};
	}
}
