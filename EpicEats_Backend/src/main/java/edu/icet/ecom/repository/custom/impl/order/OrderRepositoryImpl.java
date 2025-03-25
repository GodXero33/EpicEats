package edu.icet.ecom.repository.custom.impl.order;

import edu.icet.ecom.entity.order.OrderEntity;
import edu.icet.ecom.repository.custom.order.OrderRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {
	@Override
	public Response<OrderEntity> add (OrderEntity entity) {
		return null;
	}

	@Override
	public Response<OrderEntity> update (OrderEntity entity) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<OrderEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<OrderEntity>> getAll () {
		return null;
	}
}
