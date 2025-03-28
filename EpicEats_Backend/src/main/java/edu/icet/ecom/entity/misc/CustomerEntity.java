package edu.icet.ecom.entity.misc;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
	private Long id;
	private String name;
	private String phone;
	private String email;
	private String address;
}
