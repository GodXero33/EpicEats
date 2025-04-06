package edu.icet.ecom.dto.inventory;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryPurchaseLite implements SuperInventoryPurchase {
	private Long id;
	private Long inventoryId;
	private Long menuItemId;
	private Long supplierId;
	private Integer quantity;
	private Double cost;
	private LocalDateTime purchasedAt;
}
