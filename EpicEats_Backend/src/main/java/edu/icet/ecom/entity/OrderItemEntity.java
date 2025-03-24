package edu.icet.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {
	private Long itemId;
	private Long orderId;
	private Integer quantity;
	private Double discountPerUnit;
}
