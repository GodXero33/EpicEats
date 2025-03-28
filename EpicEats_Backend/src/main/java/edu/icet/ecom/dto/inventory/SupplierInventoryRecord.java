package edu.icet.ecom.dto.inventory;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SupplierInventoryRecord {
	private Inventory inventory;
	private Long quantity;
	private Long supplierId;
}
