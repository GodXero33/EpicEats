package edu.icet.ecom.dto.inventory;

import edu.icet.ecom.dto.merchandise.MenuItem;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryPurchase {
	private Long id;
	private Inventory inventory;
	private MenuItem menuItem;
	private Supplier supplier;
	private Integer quantity;
	private Double cost;
	private LocalDateTime purchasedAt;
}
