package edu.icet.ecom.entity.inventory;

import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllInventoryPurchasesEntity {
	private List<InventoryPurchaseLiteEntity> purchases;
	private List<InventoryEntity> inventories;
	private List<MenuItemEntity> menuItems;
	private List<SupplierEntity> suppliers;
}
