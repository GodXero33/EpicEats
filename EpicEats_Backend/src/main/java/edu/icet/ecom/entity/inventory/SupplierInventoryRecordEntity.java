package edu.icet.ecom.entity.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierInventoryRecordEntity {
	private InventoryEntity inventory;
	private Long quantity;
}
