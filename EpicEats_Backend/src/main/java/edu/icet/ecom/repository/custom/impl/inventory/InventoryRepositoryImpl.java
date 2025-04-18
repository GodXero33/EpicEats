package edu.icet.ecom.repository.custom.impl.inventory;

import edu.icet.ecom.entity.inventory.InventoryEntity;
import edu.icet.ecom.entity.inventory.SupplierEntity;
import edu.icet.ecom.entity.inventory.SupplierStockRecordEntity;
import edu.icet.ecom.entity.inventory.SupplierStockRecordLiteEntity;
import edu.icet.ecom.repository.custom.inventory.InventoryRepository;
import edu.icet.ecom.repository.custom.inventory.SupplierRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.DateTimeUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enums.ResponseType;
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
	private final SupplierRepository supplierRepository;

	@Override
	public Response<InventoryEntity> add (InventoryEntity entity) {
		return new Response<>(null, ResponseType.NOT_CREATED);
	}

	@Override
	public Response<InventoryEntity> update (InventoryEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"""
				UPDATE inventory
				SET name = ?, description = ?, quantity = ?, unit = ?
				WHERE is_deleted = FALSE AND id = ?
				""",
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
	public Response<Object> delete (Long id) {
		try {
			return (Integer) this.crudUtil.execute("""
				UPDATE inventory
				SET is_deleted = TRUE
				WHERE is_deleted = FALSE AND id = ?
				""", id) == 0 ?
				new Response<>(null, ResponseType.NOT_DELETED) :
				new Response<>(null, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<InventoryEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT name, description, quantity, unit, updated_at
			FROM inventory
			WHERE is_deleted = FALSE AND id = ?
			""", id)) {
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
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT id, name, description, quantity, unit, updated_at
			FROM inventory
			WHERE is_deleted = FALSE
			""")) {
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
	public Response<List<InventoryEntity>> getAllBySupplier (Long supplierId) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT
			inventory.id, inventory.name, inventory.description, inventory.quantity, inventory.unit, inventory.updated_at,
			supplier_inventory.quantity
			FROM inventory
			JOIN supplier_inventory ON inventory.id = supplier_inventory.inventory_id
			WHERE supplier_inventory.supplier_id = ?
			""", supplierId)) {
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
	public Response<SupplierStockRecordEntity> add (SupplierStockRecordLiteEntity supplierStockRecordEntity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final long generatedInventoryId = this.crudUtil.executeWithGeneratedKeys(
				"""
				INSERT INTO inventory (name, description, quantity, unit)
				VALUES (?, ?, ?, ?)
				""",
				supplierStockRecordEntity.getInventory().getName(),
				supplierStockRecordEntity.getInventory().getDescription(),
				supplierStockRecordEntity.getInventory().getQuantity(),
				supplierStockRecordEntity.getInventory().getUnit()
			);
			final Response<InventoryEntity> newInventoryGetResponse = this.get(generatedInventoryId);
			final boolean isSupplierInventoryRecordAdded = newInventoryGetResponse.getStatus() == ResponseType.FOUND && (Integer) this.crudUtil.execute(
				"""
				INSERT INTO supplier_inventory (supplier_id, inventory_id, quantity)
				VALUES (?, ?, ?)
				""",
				supplierStockRecordEntity.getSupplierId(),
				generatedInventoryId,
				supplierStockRecordEntity.getInventory().getQuantity() // Because we add new inventory item, supplier_inventory is new too. So quantity from direct 'SupplierInventoryRecordEntity' object can be ignored. Just can use inventory quantity
			) != 0;

			if (!isSupplierInventoryRecordAdded) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_CREATED);
			}

			final Response<SupplierEntity> supplierGetResponse = this.supplierRepository.get(supplierStockRecordEntity.getSupplierId());

			if (supplierGetResponse.getStatus() == ResponseType.NOT_FOUND) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_CREATED);
			}

			if (supplierGetResponse.getStatus() == ResponseType.SERVER_ERROR) {
				connection.rollback();
				return new Response<>(null, supplierGetResponse.getStatus());
			}

			connection.commit();

			return new Response<>(
				new SupplierStockRecordEntity(
					newInventoryGetResponse.getData(),
					(long) supplierStockRecordEntity.getInventory().getQuantity(), // Same reason, if inventory is new quantity must be same to inventory quantity. Casting won't be any problem while this is very first time
					supplierGetResponse.getData()
				),
				ResponseType.CREATED
			);
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
	public Response<SupplierStockRecordEntity> updateStock (SupplierStockRecordLiteEntity supplierStockRecordEntity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final int quantity = supplierStockRecordEntity.getInventory().getQuantity();
			final long inventoryId = supplierStockRecordEntity.getInventory().getId();
			final long supplierId = supplierStockRecordEntity.getSupplierId();

			if ((Integer) this.crudUtil.execute("""
				UPDATE inventory
				SET quantity = quantity + ?
				WHERE is_deleted = FALSE AND id = ?
				""", quantity, inventoryId) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			if ((Integer) this.crudUtil.execute("""
				UPDATE supplier_inventory
				SET quantity = quantity + ?
				WHERE supplier_id = ? AND inventory_id = ?
				""", quantity, supplierId, inventoryId) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			try (final ResultSet supplierInventoryResultSet = this.crudUtil.execute(
				"""
				SELECT quantity
				FROM supplier_inventory
				WHERE supplier_id = ? AND inventory_id = ?
				""",
				supplierId,
				inventoryId
			)) {
				if (!supplierInventoryResultSet.next()) {
					connection.rollback();
					return new Response<>(null, ResponseType.NOT_UPDATED);
				}

				final Response<InventoryEntity> inventoryGetResponse = this.get(inventoryId);

				if (inventoryGetResponse.getStatus() == ResponseType.SERVER_ERROR) {
					connection.rollback();
					return new Response<>(null, inventoryGetResponse.getStatus());
				}

				final Response<SupplierEntity> supplierGetResponse = this.supplierRepository.get(supplierId);

				if (supplierGetResponse.getStatus() == ResponseType.SERVER_ERROR) {
					connection.rollback();
					return new Response<>(null, supplierGetResponse.getStatus());
				}

				connection.commit();

				final SupplierStockRecordEntity updatedSupplierStockRecordEntity = new SupplierStockRecordEntity();
				updatedSupplierStockRecordEntity.setQuantity(supplierInventoryResultSet.getLong(1));
				updatedSupplierStockRecordEntity.setInventory(inventoryGetResponse.getData());
				updatedSupplierStockRecordEntity.setSupplier(supplierGetResponse.getData());

				return new Response<>(updatedSupplierStockRecordEntity, ResponseType.UPDATED);
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
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
