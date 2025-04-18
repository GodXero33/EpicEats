package edu.icet.ecom.dto.order;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
	private Long itemId;
	private Integer quantity;
	private Double discountPerUnit;
}
