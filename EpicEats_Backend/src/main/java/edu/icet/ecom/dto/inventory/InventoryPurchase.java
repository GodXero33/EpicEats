package edu.icet.ecom.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryPurchase {
	private Long id;
	private Long inventoryId;
	private Long supplierId;
	private Integer quantity;
	private Double cost;
	private LocalDateTime purchasedAt;
}
