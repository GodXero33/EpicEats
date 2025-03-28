package edu.icet.ecom.dto.order;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
	private Long id;
	private Long orderId;
	private Double amountGiven;
}
