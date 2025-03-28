package edu.icet.ecom.dto.inventory;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
	private Long id;
	private String name;
	private String phone;
	private String email;
	private String address;
}
