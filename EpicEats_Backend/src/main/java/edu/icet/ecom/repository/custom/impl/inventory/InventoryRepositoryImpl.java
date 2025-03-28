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

import java.sql.Connection;
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
		return new Response<>(null, ResponseType.NOT_CREATED);
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
				resultSet.getLong(7),
				supplierId
			));

			return new Response<>(inventoryEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<SupplierInventoryRecordEntity> add (SupplierInventoryRecordEntity supplierInventoryRecordEntity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final long generatedInventoryId = this.crudUtil.executeWithGeneratedKeys(
				"INSERT INTO inventory (name, description, quantity, unit) VALUES (?, ?, ?, ?)",
				supplierInventoryRecordEntity.getInventory().getName(),
				supplierInventoryRecordEntity.getInventory().getDescription(),
				supplierInventoryRecordEntity.getInventory().getQuantity(),
				supplierInventoryRecordEntity.getInventory().getUnit()
			);
			final Response<InventoryEntity> newInventoryGetResponse = this.get(generatedInventoryId);
			final boolean isSupplierInventoryRecordAdded = newInventoryGetResponse.getStatus() == ResponseType.FOUND && (Integer) this.crudUtil.execute(
				"INSERT INTO supplier_inventory (supplier_id, inventory_id, quantity) VALUES (?, ?, ?)",
				supplierInventoryRecordEntity.getSupplierId(),
				generatedInventoryId,
				supplierInventoryRecordEntity.getInventory().getQuantity() // Because we add new inventory item, supplier_inventory is new too. So quantity from direct 'SupplierInventoryRecordEntity' object can be ignored. Just can use inventory quantity
			) != 0;

			if (isSupplierInventoryRecordAdded) {
				connection.commit();

				return new Response<>(
					new SupplierInventoryRecordEntity(
						newInventoryGetResponse.getData(),
						(long) supplierInventoryRecordEntity.getInventory().getQuantity(), // Same reason, if inventory is new quantity must be same to inventory quantity. Casting won't be any problem while this is very first time
						supplierInventoryRecordEntity.getSupplierId()
					),
					ResponseType.CREATED
				);
			}

			connection.rollback();

			return new Response<>(null, ResponseType.NOT_CREATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());

			try {
				connection.rollback();
			} catch (SQLException rollbackException) {
				this.logger.error(rollbackException.getMessage());
			}

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
	public Response<SupplierInventoryRecordEntity> updateStock (SupplierInventoryRecordEntity supplierInventoryRecordEntity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final int quantity = supplierInventoryRecordEntity.getInventory().getQuantity();
			final long inventoryId = supplierInventoryRecordEntity.getInventory().getId();
			final long supplierId = supplierInventoryRecordEntity.getSupplierId();

			if ((Integer) this.crudUtil.execute("UPDATE inventory SET quantity = quantity + ? WHERE is_deleted = FALSE AND id = ?", quantity, inventoryId) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			if ((Integer) this.crudUtil.execute("UPDATE supplier_inventory SET quantity = quantity + ? WHERE supplier_id = ? AND inventory_id = ?", quantity, supplierId, inventoryId) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			try (final ResultSet supplierInventoryResultSet = this.crudUtil.execute(
				"SELECT quantity FROM supplier_inventory WHERE supplier_id = ? AND inventory_id = ?",
				supplierId,
				inventoryId
			)) {
				if (!supplierInventoryResultSet.next()) {
					connection.rollback();
					return new Response<>(null, ResponseType.NOT_UPDATED);
				}

				final Response<InventoryEntity> inventoryGetResponse = this.get(inventoryId);

				if (inventoryGetResponse.getStatus() != ResponseType.FOUND) {
					connection.rollback();
					return new Response<>(null, ResponseType.NOT_UPDATED);
				}

				connection.commit();
				supplierInventoryRecordEntity.setQuantity(supplierInventoryResultSet.getLong(1));
				supplierInventoryRecordEntity.setInventory(inventoryGetResponse.getData());

				return new Response<>(supplierInventoryRecordEntity, ResponseType.UPDATED);
			}
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());

			try {
				connection.rollback();
			} catch (SQLException rollbackException) {
				this.logger.error(rollbackException.getMessage());
			}

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
	public Response<Boolean> isExist (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT 1 FROM inventory WHERE is_deleted = FALSE AND id = ?", id)) {
			return resultSet.next() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}
}
