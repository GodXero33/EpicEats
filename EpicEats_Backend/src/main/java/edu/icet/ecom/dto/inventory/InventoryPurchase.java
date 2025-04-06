package edu.icet.ecom.dto.inventory;

import edu.icet.ecom.dto.merchandise.MenuItem;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryPurchase implements SuperInventoryPurchase {
	private Long id;
	private Inventory inventory;
	private MenuItem menuItem;
	@NotNull(message = "Supplier can't be null")
	private Supplier supplier;
	private Integer quantity;
	private Double cost;
	private LocalDateTime purchasedAt;
}
