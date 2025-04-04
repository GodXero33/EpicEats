package edu.icet.ecom.dto.inventory;

import edu.icet.ecom.dto.merchandise.MenuItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllInventoryPurchases {
	private List<InventoryPurchaseLite> purchases;
	private List<Inventory> inventories;
	private List<MenuItem> menuItems;
	private List<Supplier> suppliers;
}
