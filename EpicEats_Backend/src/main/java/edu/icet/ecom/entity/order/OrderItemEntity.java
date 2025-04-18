package edu.icet.ecom.entity.order;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {
	private Long itemId;
	private Integer quantity;
	private Double discountPerUnit;
}
