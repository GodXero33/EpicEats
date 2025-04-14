package edu.icet.ecom.dto.security;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TokenAndUser {
	private User user;
	private String token;
}
