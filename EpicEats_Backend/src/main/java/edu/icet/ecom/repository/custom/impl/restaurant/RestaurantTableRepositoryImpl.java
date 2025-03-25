package edu.icet.ecom.repository.custom.impl.restaurant;

import edu.icet.ecom.entity.restaurant.RestaurantTableEntity;
import edu.icet.ecom.repository.custom.restaurant.RestaurantTableRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public class RestaurantTableRepositoryImpl implements RestaurantTableRepository {
	@Override
	public Response<RestaurantTableEntity> add (RestaurantTableEntity entity) {
		return null;
	}

	@Override
	public Response<RestaurantTableEntity> update (RestaurantTableEntity entity) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<RestaurantTableEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<RestaurantTableEntity>> getAll () {
		return null;
	}
}
