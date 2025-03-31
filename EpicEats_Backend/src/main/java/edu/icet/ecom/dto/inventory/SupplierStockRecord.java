package edu.icet.ecom.dto.inventory;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStockRecord {
	@NotNull(message = "Inventory can't be null")
	private Inventory inventory;
	private Long quantity;
	@NotNull(message = "Supplier can't be null")
	private Supplier supplier;
}
