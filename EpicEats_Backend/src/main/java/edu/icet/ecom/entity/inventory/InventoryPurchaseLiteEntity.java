package edu.icet.ecom.entity.inventory;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryPurchaseLiteEntity {
	private Long id;
	private Long inventoryId;
	private Long menuItemId;
	private Long supplierId;
	private Integer quantity;
	private Double cost;
	private LocalDateTime purchasedAt;
}
