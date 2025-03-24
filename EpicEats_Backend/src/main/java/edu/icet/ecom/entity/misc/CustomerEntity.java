package edu.icet.ecom.entity.misc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
	private Long id;
	private String name;
	private String phone;
	private String email;
	private String address;
}
