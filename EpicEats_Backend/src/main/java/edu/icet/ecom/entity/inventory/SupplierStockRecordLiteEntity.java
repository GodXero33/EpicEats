package edu.icet.ecom.entity.inventory;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStockRecordLiteEntity {
	private InventoryEntity inventory;
	private Long quantity;
	private Long supplierId;
}
