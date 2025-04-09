package edu.icet.ecom.repository.custom.impl.restaurant;

import edu.icet.ecom.entity.restaurant.RestaurantTableEntity;
import edu.icet.ecom.repository.custom.restaurant.RestaurantTableRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.DateTimeUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestaurantTableRepositoryImpl implements RestaurantTableRepository {
	private final CrudUtil crudUtil;
	private final Logger logger;

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

	@Override
	public Response<Boolean> isTableNumberExist (Integer tableNumber) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT 1 FROM restaurant_table WHERE table_number = ?", tableNumber)) {
			return new Response<>(null, resultSet.next() ? ResponseType.FOUND : ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> isTableNumberExist (Integer tableNumber, Long tableId) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT 1 FROM restaurant_table WHERE id != ? AND table_number = ?", tableId, tableNumber)) {
			return new Response<>(null, resultSet.next() ? ResponseType.FOUND : ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
