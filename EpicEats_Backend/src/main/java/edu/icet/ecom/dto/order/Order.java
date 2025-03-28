package edu.icet.ecom.dto.order;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	private Long id;
	private LocalDateTime placedAt;
	private Double discount;
	private Long customerId;
	private Long employeeId;
}
