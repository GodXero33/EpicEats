package edu.icet.ecom.dto.order;

import edu.icet.ecom.dto.employee.Employee;
import edu.icet.ecom.dto.merchandise.MenuItem;
import edu.icet.ecom.dto.misc.Customer;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllOrders implements SuperOrder {
	List<OrderLite> orders;
	List<MenuItem> menuItems;
	List<Employee> employees;
	List<Customer> customers;
}
