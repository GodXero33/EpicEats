package edu.icet.ecom.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
	private Long itemId;
	private Long orderId;
	private Integer quantity;
	private Double discountPerUnit;
}
