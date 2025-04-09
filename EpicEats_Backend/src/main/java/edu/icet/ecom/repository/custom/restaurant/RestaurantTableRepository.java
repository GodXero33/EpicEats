package edu.icet.ecom.repository.custom.restaurant;

import edu.icet.ecom.entity.restaurant.RestaurantTableEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface RestaurantTableRepository extends CrudRepository<RestaurantTableEntity> {
	Response<Boolean> isTableNumberExist (Integer tableNumber);
	Response<Boolean> isTableNumberExist (Integer tableNumber, Long tableId);
}
