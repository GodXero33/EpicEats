package edu.icet.ecom.dto.security;

import edu.icet.ecom.util.enumaration.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@NotNull(message = "Employee id is required")
	@Min(value = 1, message = "Employee id can't be zero or negative")
	private Long employeeId;

	@NotBlank(message = "Username can't be empty")
	@Pattern(regexp = "^[A-Za-z0-9]+(_[A-Za-z0-9]+)*$", message = "Invalid admin name")
	@Size(min = 4, max = 15, message = "Name must be between 4 and 15 characters")
	private String username;

	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime lastLogin;

	@NotNull(message = "User role can't be null")
	private UserRole role;
}
