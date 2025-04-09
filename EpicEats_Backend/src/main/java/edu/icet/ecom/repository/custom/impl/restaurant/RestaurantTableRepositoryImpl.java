package edu.icet.ecom.repository.custom.impl.restaurant;

import edu.icet.ecom.dto.restaurant.TimeRange;
import edu.icet.ecom.entity.misc.CustomerEntity;
import edu.icet.ecom.entity.restaurant.RestaurantTableBookingEntity;
import edu.icet.ecom.entity.restaurant.RestaurantTableBookingLiteEntity;
import edu.icet.ecom.entity.restaurant.RestaurantTableEntity;
import edu.icet.ecom.repository.custom.misc.CustomerRepository;
import edu.icet.ecom.repository.custom.restaurant.RestaurantTableRepository;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestaurantTableRepositoryImpl implements RestaurantTableRepository {
	private final CrudUtil crudUtil;
	private final Logger logger;
	private final CustomerRepository customerRepository;

	@Override
	public Response<RestaurantTableEntity> add (RestaurantTableEntity entity) {
		try {
			final long generatedTableId = this.crudUtil.executeWithGeneratedKeys("""
				INSERT INTO restaurant_table (table_number, capacity)
				VALUES (?, ?)
				""",
				entity.getTableNumber(),
				entity.getCapacity());

			entity.setId(generatedTableId);

			return new Response<>(entity, ResponseType.CREATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<RestaurantTableEntity> update (RestaurantTableEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"""
				UPDATE restaurant_table
				SET table_number = ?, capacity = ?, is_available = ?
				WHERE is_deleted = FALSE AND id = ?
				""",
				entity.getTableNumber(),
				entity.getCapacity(),
				entity.getIsAvailable(),
				entity.getId()
			) == 0 ?
				new Response<>(null, ResponseType.NOT_UPDATED) :
				new Response<>(entity, ResponseType.UPDATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Object> delete (Long id) {
		try {
			return (Integer) this.crudUtil.execute(
				"""
				UPDATE restaurant_table
				SET is_deleted = TRUE
				WHERE is_deleted = FALSE AND id = ?
				""",
				id
			) == 0 ?
				new Response<>(null, ResponseType.NOT_DELETED) :
				new Response<>(null, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<RestaurantTableEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT table_number, capacity, last_booked, is_available
			FROM restaurant_table
			WHERE is_deleted = FALSE AND id = ?
			""", id)) {
			return resultSet.next() ?
				new Response<>(RestaurantTableEntity.builder()
					.id(id)
					.tableNumber(resultSet.getInt(1))
					.capacity(resultSet.getInt(2))
					.lastBooked(DateTimeUtil.parseDateTime(resultSet.getString(3)))
					.isAvailable(resultSet.getBoolean(4))
					.build(),
					ResponseType.FOUND
				) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<RestaurantTableEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT id, table_number, capacity, last_booked, is_available
			FROM restaurant_table
			WHERE is_deleted = FALSE
			""")) {
			final List<RestaurantTableEntity> tableEntities = new ArrayList<>();

			while (resultSet.next()) tableEntities.add(RestaurantTableEntity.builder()
				.id(resultSet.getLong(1))
				.tableNumber(resultSet.getInt(2))
				.capacity(resultSet.getInt(3))
				.lastBooked(DateTimeUtil.parseDateTime(resultSet.getString(4)))
				.isAvailable(resultSet.getBoolean(5))
				.build());

			return new Response<>(tableEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	private Response<Boolean> getExistence (String condition, Object ...binds) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT 1 FROM restaurant_table " + condition, binds)) {
			return new Response<>(null, resultSet.next() ? ResponseType.FOUND : ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> isTableExist (Long tableId) {
		return this.getExistence("WHERE id = ?", tableId);
	}

	@Override
	public Response<Boolean> isTableNumberExist (Integer tableNumber) {
		return this.getExistence("WHERE table_number = ?", tableNumber);
	}

	@Override
	public Response<Boolean> isTableNumberExist (Integer tableNumber, Long tableId) {
		return this.getExistence("WHERE id != ? AND table_number = ?", tableId, tableNumber);
	}

	@Override
	public Response<RestaurantTableBookingEntity> addBooking (RestaurantTableBookingLiteEntity restaurantTableBookingLiteEntity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final long generatedBookingId = this.crudUtil.executeWithGeneratedKeys(
				"""
				INSERT INTO restaurant_table_booking (table_id, customer_id, booking_date, start_time, end_time)
				VALUES (?, ?, ?, ?, ?)
				""",
				restaurantTableBookingLiteEntity.getTableId(),
				restaurantTableBookingLiteEntity.getCustomerId(),
				restaurantTableBookingLiteEntity.getBookingDate(),
				restaurantTableBookingLiteEntity.getStartTime(),
				restaurantTableBookingLiteEntity.getEndTime()
			);

			if ((Integer) this.crudUtil.execute(
				"""
				UPDATE restaurant_table
				SET last_booked = ?
				WHERE id = ?
				""",
				DateTimeUtil.getCurrentDateTime(),
				restaurantTableBookingLiteEntity.getTableId()
			) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_CREATED);
			}

			connection.commit();

			final Response<RestaurantTableEntity> tableGetResponse = this.get(restaurantTableBookingLiteEntity.getTableId());

			if (tableGetResponse.getStatus() == ResponseType.SERVER_ERROR) {
				connection.rollback();
				return new Response<>(null, ResponseType.SERVER_ERROR);
			}

			final Response<CustomerEntity> customerGetResponse = this.customerRepository.get(restaurantTableBookingLiteEntity.getCustomerId());

			if (customerGetResponse.getStatus() == ResponseType.SERVER_ERROR) {
				connection.rollback();
				return new Response<>(null, ResponseType.SERVER_ERROR);
			}

			return new Response<>(RestaurantTableBookingEntity.builder()
				.id(generatedBookingId)
				.table(tableGetResponse.getData())
				.customer(customerGetResponse.getData())
				.bookingDate(restaurantTableBookingLiteEntity.getBookingDate())
				.startTime(restaurantTableBookingLiteEntity.getStartTime())
				.endTime(restaurantTableBookingLiteEntity.getEndTime())
				.build(), ResponseType.CREATED);
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
	public Response<RestaurantTableBookingEntity> updateBooking (RestaurantTableBookingLiteEntity restaurantTableBookingLiteEntity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			if ((Integer) this.crudUtil.execute(
				"""
				UPDATE restaurant_table_booking
				SET table_id = ?, customer_id = ?, booking_date = ?, start_time = ?, end_time = ?
				WHERE is_deleted = FALSE AND id = ?
				""",
				restaurantTableBookingLiteEntity.getTableId(),
				restaurantTableBookingLiteEntity.getCustomerId(),
				restaurantTableBookingLiteEntity.getBookingDate(),
				restaurantTableBookingLiteEntity.getStartTime(),
				restaurantTableBookingLiteEntity.getEndTime(),
				restaurantTableBookingLiteEntity.getId()
			) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			if ((Integer) this.crudUtil.execute(
				"""
				UPDATE restaurant_table
				SET last_booked = ?
				WHERE id = ?
				""",
				DateTimeUtil.getCurrentDateTime(),
				restaurantTableBookingLiteEntity.getTableId()
			) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			connection.commit();

			final Response<RestaurantTableEntity> tableGetResponse = this.get(restaurantTableBookingLiteEntity.getTableId());

			if (tableGetResponse.getStatus() == ResponseType.SERVER_ERROR) {
				connection.rollback();
				return new Response<>(null, ResponseType.SERVER_ERROR);
			}

			final Response<CustomerEntity> customerGetResponse = this.customerRepository.get(restaurantTableBookingLiteEntity.getCustomerId());

			if (customerGetResponse.getStatus() == ResponseType.SERVER_ERROR) {
				connection.rollback();
				return new Response<>(null, ResponseType.SERVER_ERROR);
			}

			return new Response<>(RestaurantTableBookingEntity.builder()
				.id(restaurantTableBookingLiteEntity.getId())
				.table(tableGetResponse.getData())
				.customer(customerGetResponse.getData())
				.bookingDate(restaurantTableBookingLiteEntity.getBookingDate())
				.startTime(restaurantTableBookingLiteEntity.getStartTime())
				.endTime(restaurantTableBookingLiteEntity.getEndTime())
				.build(), ResponseType.UPDATED);
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
	public Response<RestaurantTableBookingEntity> getBooking (Long id) {
		return null;
	}

	@Override
	public Response<RestaurantTableBookingEntity> getAllBookings () {
		return null;
	}

	@Override
	public Response<RestaurantTableBookingEntity> getAllBookingsByTableId (Long tableId) {
		return null;
	}

	@Override
	public Response<Object> deleteBooking (Long id) {
		return null;
	}

	@Override
	public Response<Object> deleteAllBookingsByTableId (Long tableId) {
		return null;
	}

	@Override
	public Response<List<TimeRange>> getTimeSlotsForTargetTableInTargetDate (Long tableId, LocalDate date, Long bookingId) {
		try (final ResultSet resultSet = this.crudUtil.execute(
			"""
			SELECT start_time, end_time
			FROM restaurant_table_booking
			WHERE is_deleted = FALSE AND table_id = ? AND booking_date = ?%s
			""".formatted(bookingId == null ? "" : " AND id != ?"),
			tableId,
			date,
			bookingId
		)) {
			final List<TimeRange> targetTimeSlotsForTableResponse = new ArrayList<>();

			while (resultSet.next()) targetTimeSlotsForTableResponse.add(new TimeRange(
				DateTimeUtil.parseTime(resultSet.getString(1)),
				DateTimeUtil.parseTime(resultSet.getString(2))
			));

			return new Response<>(targetTimeSlotsForTableResponse, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
