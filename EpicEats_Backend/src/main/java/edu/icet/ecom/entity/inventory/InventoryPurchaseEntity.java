package edu.icet.ecom.entity.inventory;

import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryPurchaseEntity {
	private Long id;
	private InventoryEntity inventory;
	private MenuItemEntity menuItem;
	private SupplierEntity supplier;
	private Integer quantity;
	private Double cost;
	private LocalDateTime purchasedAt;
}
