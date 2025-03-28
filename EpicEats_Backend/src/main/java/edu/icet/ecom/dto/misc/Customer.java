package edu.icet.ecom.dto.misc;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	private Long id;
	private String name;
	private String phone;
	private String email;
	private String address;
}
