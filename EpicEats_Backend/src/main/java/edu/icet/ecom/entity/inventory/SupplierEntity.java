package edu.icet.ecom.entity.inventory;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SupplierEntity {
	private Long id;
	private String name;
	private String phone;
	private String email;
	private String address;
}
