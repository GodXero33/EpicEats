package edu.icet.ecom.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
	private Long id;
	private LocalDateTime placedAt;
	private Double discount;
	private Long customerId;
	private Long employeeId;
}
