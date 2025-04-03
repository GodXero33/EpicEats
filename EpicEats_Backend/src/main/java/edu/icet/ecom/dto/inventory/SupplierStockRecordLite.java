package edu.icet.ecom.dto.inventory;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStockRecordLite {
	@NotNull(message = "Inventory can't be null")
	private Inventory inventory;
	private Long quantity;
	@NotNull(message = "supplierId can't be null")
	private Long supplierId;
}
