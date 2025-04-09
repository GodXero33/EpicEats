package edu.icet.ecom.service.custom.impl.restaurant;

import edu.icet.ecom.dto.restaurant.RestaurantTable;
import edu.icet.ecom.entity.restaurant.RestaurantTableEntity;
import edu.icet.ecom.repository.custom.restaurant.RestaurantTableRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.restaurant.RestaurantTableService;
import edu.icet.ecom.util.Response;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class RestaurantTableServiceImpl implements RestaurantTableService {
	private final SuperServiceHandler<RestaurantTable, RestaurantTableEntity> serviceHandler;
	private final RestaurantTableRepository restaurantTableRepository;

	public RestaurantTableServiceImpl (RestaurantTableRepository restaurantTableRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(restaurantTableRepository, mapper, RestaurantTable.class, RestaurantTableEntity.class);
		this.restaurantTableRepository = restaurantTableRepository;
	}

	@Override
	public Response<RestaurantTable> get (Long id) {
		return this.serviceHandler.get(id);
	}

	@Override
	public Response<List<RestaurantTable>> getAll () {
		return this.serviceHandler.getAll();
	}

	@Override
	public Response<RestaurantTable> add (RestaurantTable dto) {
		return this.serviceHandler.add(dto);
	}

	@Override
	public Response<RestaurantTable> update (RestaurantTable dto) {
		return this.serviceHandler.update(dto);
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.serviceHandler.delete(id);
	}

	@Override
	public Response<Boolean> isTableNumberExist (Integer tableNumber) {
		return this.restaurantTableRepository.isTableNumberExist(tableNumber);
	}

	@Override
	public Response<Boolean> isTableNumberExist (Integer tableNumber, Long tableId) {
		return this.restaurantTableRepository.isTableNumberExist(tableNumber, tableId);
	}
}
