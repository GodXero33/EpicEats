package edu.icet.ecom.service.custom.order;

import edu.icet.ecom.dto.order.SuperOrder;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface OrderService extends SuperService<SuperOrder> {
	Response<SuperOrder> getAllStructured ();
}
