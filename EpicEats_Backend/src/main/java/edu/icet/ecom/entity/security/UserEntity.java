package edu.icet.ecom.entity.security;

import edu.icet.ecom.util.enumaration.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Getter
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
