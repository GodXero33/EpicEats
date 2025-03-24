package edu.icet.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryPurchaseEntity {
	private Long id;
	private Long inventoryId;
	private Long supplierId;
	private Integer quantity;
	private Double cost;
	private LocalDateTime purchasedAt;
}
