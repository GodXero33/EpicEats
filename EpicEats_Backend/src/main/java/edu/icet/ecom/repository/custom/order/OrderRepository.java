package edu.icet.ecom.repository.custom.order;

import edu.icet.ecom.entity.order.OrderEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface OrderRepository extends CrudRepository<OrderEntity> {
	Response<Object> deleteByEmployeeId (Long employeeId);
}
