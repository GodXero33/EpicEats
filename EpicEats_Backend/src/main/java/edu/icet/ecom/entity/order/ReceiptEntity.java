package edu.icet.ecom.entity.order;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptEntity {
	private Long id;
	private Long orderId;
	private Double amountGiven;
}
