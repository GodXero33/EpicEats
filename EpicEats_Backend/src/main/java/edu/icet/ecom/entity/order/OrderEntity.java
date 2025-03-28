package edu.icet.ecom.entity.order;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
	private Long id;
	private LocalDateTime placedAt;
	private Double discount;
	private Long customerId;
	private Long employeeId;
}
