package edu.icet.ecom.service.custom.impl.order;

import edu.icet.ecom.dto.order.AllOrders;
import edu.icet.ecom.dto.order.Order;
import edu.icet.ecom.dto.order.SuperOrder;
import edu.icet.ecom.entity.order.OrderLiteEntity;
import edu.icet.ecom.entity.order.SuperOrderEntity;
import edu.icet.ecom.repository.custom.order.OrderRepository;
import edu.icet.ecom.service.custom.order.OrderService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enums.ResponseType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final ModelMapper mapper;

	@Override
	public Response<SuperOrder> get (Long id) {
		final Response<SuperOrderEntity> response = this.orderRepository.get(id);

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			this.mapper.map(response.getData(), Order.class) :
			null
		, response.getStatus());
	}

	@Override
	public Response<List<SuperOrder>> getAll () {
		final Response<List<SuperOrderEntity>> response = this.orderRepository.getAll();

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			response.getData().stream().map(orderEntity -> (SuperOrder) this.mapper.map(orderEntity, Order.class)).toList() :
			null
		, response.getStatus());
	}

	@Override
	public Response<SuperOrder> add (SuperOrder dto) {
		final Response<SuperOrderEntity> response = this.orderRepository.add(this.mapper.map(dto, OrderLiteEntity.class));

		return new Response<>(response.getStatus() == ResponseType.CREATED ?
			this.mapper.map(response.getData(), Order.class) :
			null
		, response.getStatus());
	}

	@Override
	public Response<SuperOrder> update (SuperOrder dto) {
		return null;
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.orderRepository.delete(id);
	}

	@Override
	public Response<SuperOrder> getAllStructured () {
		final Response<SuperOrderEntity> response = this.orderRepository.getAllStructured();

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			this.mapper.map(response.getData(), AllOrders.class) :
			null
		, response.getStatus());
	}
}
