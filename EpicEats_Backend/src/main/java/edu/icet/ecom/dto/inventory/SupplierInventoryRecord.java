package edu.icet.ecom.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierInventoryRecord {
	private Inventory inventory;
	private Long quantity;
}
