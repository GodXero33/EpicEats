package edu.icet.ecom.entity.order;

import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import edu.icet.ecom.entity.misc.CustomerEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllOrdersEntity implements SuperOrderEntity {
	List<OrderLiteEntity> orders;
	List<MenuItemEntity> menuItems;
	List<EmployeeEntity> employees;
	List<CustomerEntity> customers;
}
