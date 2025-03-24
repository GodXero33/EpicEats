package edu.icet.ecom.entity;

import edu.icet.ecom.util.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
	private Long employeeId;
	private String username;
	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime lastLogin;
	private UserRole role;
}
