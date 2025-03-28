package edu.icet.ecom.dto.inventory;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStockRecord {
	private Inventory inventory;
	private Long quantity;
	private Long supplierId;
}
