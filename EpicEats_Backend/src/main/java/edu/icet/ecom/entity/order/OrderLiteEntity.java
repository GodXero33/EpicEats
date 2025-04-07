package edu.icet.ecom.entity.order;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderLiteEntity implements SuperOrderEntity {
	private Long id;
	private LocalDateTime placedAt;
	private Double discount;
	private Long customerId;
	private Long employeeId;
	private List<OrderItemEntity> orderItems;
}
