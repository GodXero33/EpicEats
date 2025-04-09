package edu.icet.ecom.service.custom.restaurant;

import edu.icet.ecom.dto.restaurant.RestaurantTable;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface RestaurantTableService extends SuperService<RestaurantTable> {
	Response<Boolean> isTableNumberExist (Integer tableNumber);
	Response<Boolean> isTableNumberExist (Integer tableNumber, Long tableId);
}
