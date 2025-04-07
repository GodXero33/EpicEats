package edu.icet.ecom.dto.order;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderLite implements SuperOrder {
	private Long id;
	private LocalDateTime placedAt;
	private Double discount;
	private Long customerId;
	private Long employeeId;
	private List<OrderItem> orderItems;
}
