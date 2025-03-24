package edu.icet.ecom.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
	private Long id;
	private Long orderId;
	private Double amountGiven;
}
