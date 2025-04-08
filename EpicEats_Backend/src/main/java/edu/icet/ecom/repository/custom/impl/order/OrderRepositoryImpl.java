package edu.icet.ecom.repository.custom.impl.order;

import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import edu.icet.ecom.entity.misc.CustomerEntity;
import edu.icet.ecom.entity.order.OrderEntity;
import edu.icet.ecom.entity.order.OrderItemEntity;
import edu.icet.ecom.entity.order.OrderLiteEntity;
import edu.icet.ecom.entity.order.SuperOrderEntity;
import edu.icet.ecom.repository.custom.employee.EmployeeRepository;
import edu.icet.ecom.repository.custom.merchandise.MenuItemRepository;
import edu.icet.ecom.repository.custom.misc.CustomerRepository;
import edu.icet.ecom.repository.custom.order.OrderRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.DateTimeUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
	private final CrudUtil crudUtil;
	private final Logger logger;
	private final CustomerRepository customerRepository;
	private final EmployeeRepository employeeRepository;
	private final MenuItemRepository menuItemRepository;

	@Override
	public Response<SuperOrderEntity> add (SuperOrderEntity entity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final OrderLiteEntity orderLiteEntity = (OrderLiteEntity) entity;
			final long generatedOrderId = this.crudUtil.executeWithGeneratedKeys("""
				INSERT INTO `order` (discount, customer_id, employee_id)
				VALUES(?, ?, ?)
				""",
				orderLiteEntity.getDiscount(),
				orderLiteEntity.getCustomerId(),
				orderLiteEntity.getEmployeeId()
			);

			final StringBuilder orderItemsInsertQueryBuilder = new StringBuilder();
			final int orderItemsLength = orderLiteEntity.getOrderItems().size();
			final Object[] orderItemsInsertBindsArray = new Object[orderItemsLength * 4];
			final List<OrderItemEntity> orderItems = orderLiteEntity.getOrderItems();

			orderItemsInsertQueryBuilder.append("""
				INSERT INTO order_item (item_id, order_id, quantity, discount_per_unit)
				VALUES%s
				""".formatted("(?,?,?,?),".repeat(orderItemsLength))).setLength(orderItemsInsertQueryBuilder.length() - 2);

			for (int a = 0; a < orderItemsLength; a++) {
				final OrderItemEntity orderItem = orderItems.get(a);

				orderItemsInsertBindsArray[a * 4] = orderItem.getItemId();
				orderItemsInsertBindsArray[a * 4 + 1] = generatedOrderId;
				orderItemsInsertBindsArray[a * 4 + 2] = orderItem.getQuantity();
				orderItemsInsertBindsArray[a * 4 + 3] = orderItem.getDiscountPerUnit();
			}

			if ((Integer) this.crudUtil.execute(
				orderItemsInsertQueryBuilder.toString(),
				orderItemsInsertBindsArray
			) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_CREATED);
			}

			final Response<SuperOrderEntity> orderGetResponse = this.get(generatedOrderId);

			if (orderGetResponse.getStatus() == ResponseType.SERVER_ERROR) {
				connection.rollback();
				return new Response<>(null, orderGetResponse.getStatus());
			}

			if (orderGetResponse.getStatus() == ResponseType.NOT_FOUND) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_CREATED);
			}

			connection.commit();

			return new Response<>(orderGetResponse.getData(), ResponseType.CREATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<SuperOrderEntity> update (SuperOrderEntity entity) {
		return null;
	}

	@Override
	public Response<Object> delete (Long id) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			if ((Integer) this.crudUtil.execute("""
				UPDATE `order`
				SET is_deleted = TRUE
				WHERE is_deleted = FALSE AND id = ?
				""", id) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_DELETED);
			}

			if ((Integer) this.crudUtil.execute("""
				UPDATE order_item
				SET is_deleted = TRUE
				WHERE order_id = ?
				""", id) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_DELETED);
			}

			connection.commit();
			return new Response<>(null, ResponseType.DELETED);
		} catch (SQLException exception) {
			try {
				connection.rollback();
			} catch (SQLException rollbackException) {
				this.logger.error(rollbackException.getMessage());
			}

			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException exception) {
				this.logger.error(exception.getMessage());
			}
		}
	}

	@Override
	public Response<SuperOrderEntity> get (Long id) {
		try (final ResultSet orderResultSet = this.crudUtil.execute("""
			SELECT placed_at, discount, customer_id, employee_id
			FROM `order`
			WHERE is_deleted = FALSE AND id = ?
			""", id)) {
			if (!orderResultSet.next()) return new Response<>(null, ResponseType.NOT_FOUND);

			final OrderEntity order = OrderEntity.builder()
				.id(id)
				.placedAt(DateTimeUtil.parseDateTime(orderResultSet.getString(1)))
				.discount(orderResultSet.getDouble(2))
				.build();

			final Response<CustomerEntity> customerGetResponse = this.customerRepository.get(orderResultSet.getLong(3));

			if (customerGetResponse.getStatus() != ResponseType.FOUND) return new Response<>(null, customerGetResponse.getStatus());

			final Response<EmployeeEntity> employeeGetResponse = this.employeeRepository.get(orderResultSet.getLong(4));

			if (employeeGetResponse.getStatus() != ResponseType.FOUND) return new Response<>(null, customerGetResponse.getStatus());

			order.setCustomer(customerGetResponse.getData());
			order.setEmployee(employeeGetResponse.getData());

			final List<Long> menuItemIDs = new ArrayList<>();
			final List<OrderItemEntity> orderItems = new ArrayList<>();

			try (final ResultSet orderItemResultSet = this.crudUtil.execute("""
				SELECT item_id, quantity, discount_per_unit
				FROM order_item
				WHERE is_deleted = FALSE AND order_id = ?
				""", id)) {
				while (orderItemResultSet.next()) {
					orderItems.add(OrderItemEntity.builder()
						.itemId(orderItemResultSet.getLong(1))
						.quantity(orderItemResultSet.getInt(2))
						.discountPerUnit(orderItemResultSet.getDouble(3))
						.build());

					menuItemIDs.add(orderItemResultSet.getLong(1));
				}
			}

			final Response<List<MenuItemEntity>> menuItemsGetResponse = this.menuItemRepository.getAllByIDs(menuItemIDs);

			if (menuItemsGetResponse.getStatus() != ResponseType.FOUND) return new Response<>(null, menuItemsGetResponse.getStatus());

			order.setOrderItems(orderItems);
			order.setMenuItems(menuItemsGetResponse.getData());

			return new Response<>(order, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<SuperOrderEntity>> getAll () {
		return null;
	}

	@Override
	public Response<Object> deleteByEmployeeId (Long employeeId) {
		return null;
	}
}
