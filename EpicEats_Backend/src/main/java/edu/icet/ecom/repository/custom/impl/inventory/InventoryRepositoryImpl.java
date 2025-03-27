package edu.icet.ecom.repository.custom.impl.inventory;

import edu.icet.ecom.entity.inventory.InventoryEntity;
import edu.icet.ecom.entity.inventory.SupplierInventoryRecordEntity;
import edu.icet.ecom.repository.custom.inventory.InventoryRepository;
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
public class InventoryRepositoryImpl implements InventoryRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	@Override
	public Response<InventoryEntity> add (InventoryEntity entity) {
		try {
			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"INSERT INTO inventory (name, description, quantity, unit) VALUES (?, ?, ?, ?)",
				entity.getName(),
				entity.getDescription(),
				entity.getQuantity(),
				entity.getUnit()
			);

			entity.setId(generatedId);

			return new Response<>(entity, ResponseType.CREATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<InventoryEntity> update (InventoryEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"UPDATE inventory SET name = ?, description = ?, quantity = ?, unit = ? WHERE is_deleted = FALSE AND id = ?",
				entity.getName(),
				entity.getDescription(),
				entity.getQuantity(),
				entity.getUnit(),
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
	public Response<Boolean> delete (Long id) {
		try {
			return (Integer) this.crudUtil.execute("UPDATE inventory SET is_deleted = TRUE WHERE is_deleted = FALSE AND id = ?", id) == 0 ?
				new Response<>(false, ResponseType.NOT_DELETED) :
				new Response<>(true, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<InventoryEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT name, description, quantity, unit, updated_at FROM inventory WHERE is_deleted = FALSE AND id = ?", id)) {
			return resultSet.next() ?
				new Response<>(InventoryEntity.builder()
					.id(id)
					.name(resultSet.getString(1))
					.description(resultSet.getString(2))
					.quantity(resultSet.getInt(3))
					.unit(resultSet.getString(4))
					.updatedAt(DateTimeUtil.parseDateTime(resultSet.getString(5)))
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<InventoryEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT id, name, description, quantity, unit, updated_at FROM inventory WHERE is_deleted = FALSE")) {
			final List<InventoryEntity> inventoryEntities = new ArrayList<>();

			while (resultSet.next()) inventoryEntities.add(InventoryEntity.builder()
					.id(resultSet.getLong(1))
					.name(resultSet.getString(2))
					.description(resultSet.getString(3))
					.quantity(resultSet.getInt(4))
					.unit(resultSet.getString(5))
					.updatedAt(DateTimeUtil.parseDateTime(resultSet.getString(6)))
				.build());

			return new Response<>(inventoryEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<SupplierInventoryRecordEntity>> getAllBySupplier (Long supplierId) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT i.id, i.name, i.description, i.quantity, i.unit, i.updated_at, si.quantity FROM inventory i JOIN supplier_inventory si ON i.id = si.inventory_id WHERE si.supplier_id = ?", supplierId)) {
			final List<SupplierInventoryRecordEntity> inventoryEntities = new ArrayList<>();

			while (resultSet.next()) inventoryEntities.add(new SupplierInventoryRecordEntity(
				InventoryEntity.builder()
					.id(resultSet.getLong(1))
					.name(resultSet.getString(2))
					.description(resultSet.getString(3))
					.quantity(resultSet.getInt(4))
					.unit(resultSet.getString(5))
					.updatedAt(DateTimeUtil.parseDateTime(resultSet.getString(6)))
					.build(),
				resultSet.getLong(7)
			));

			return new Response<>(inventoryEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
