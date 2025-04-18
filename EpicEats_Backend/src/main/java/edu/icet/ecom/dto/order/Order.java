package edu.icet.ecom.dto.order;

import edu.icet.ecom.dto.employee.Employee;
import edu.icet.ecom.dto.merchandise.MenuItem;
import edu.icet.ecom.dto.misc.Customer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order implements SuperOrder {
	private Long id;
	private LocalDateTime placedAt;
	private Double discount;
	private Customer customer;
	private Employee employee;
	private List<OrderItem> orderItems;
	private List<MenuItem> menuItems;
}
