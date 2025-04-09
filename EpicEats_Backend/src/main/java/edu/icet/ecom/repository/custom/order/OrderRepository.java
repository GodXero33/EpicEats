package edu.icet.ecom.repository.custom.order;

import edu.icet.ecom.entity.order.SuperOrderEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface OrderRepository extends CrudRepository<SuperOrderEntity> {
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<SuperOrderEntity> getAllStructured ();
}
