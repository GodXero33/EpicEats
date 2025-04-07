package edu.icet.ecom.entity.order;

import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import edu.icet.ecom.entity.misc.CustomerEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity implements SuperOrderEntity {
	private Long id;
	private LocalDateTime placedAt;
	private Double discount;
	private CustomerEntity customer;
	private EmployeeEntity employee;
	private List<OrderItemEntity> orderItems;
	private List<MenuItemEntity> menuItems;
}
