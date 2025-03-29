package edu.icet.ecom.repository.custom.impl.inventory;

import edu.icet.ecom.entity.inventory.InventoryPurchaseEntity;
import edu.icet.ecom.repository.custom.inventory.InventoryPurchaseRepository;
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
public class InventoryPurchaseRepositoryImpl implements InventoryPurchaseRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	@Override
	public Response<InventoryPurchaseEntity> add (InventoryPurchaseEntity entity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"INSERT INTO inventory_purchase (inventory_id, menu_item_id, supplier_id, quantity, cost) VALUES (?, ?, ?, ?, ?)",
				entity.getInventoryId(),
				entity.getMenuItemId(),
				entity.getSupplierId(),
				entity.getQuantity(),
				entity.getCost()
			);

			try (final ResultSet resultSet = this.crudUtil.execute("SELECT purchased_at FROM inventory_purchase WHERE id = ?", generatedId)) {
				if (!resultSet.next()) {
					connection.rollback();
					return new Response<>(null, ResponseType.NOT_CREATED);
				}

				connection.commit();
				entity.setId(generatedId);
				entity.setPurchasedAt(DateTimeUtil.parseDateTime(resultSet.getString(1)));

				return new Response<>(entity, ResponseType.CREATED);
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
	public Response<InventoryPurchaseEntity> update (InventoryPurchaseEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"UPDATE inventory_purchase SET inventory_id = ?, menu_item_id = ?, supplier_id = ?, quantity = ?, cost = ? WHERE is_deleted = FALSE AND id = ?",
				entity.getInventoryId(),
				entity.getMenuItemId(),
				entity.getSupplierId(),
				entity.getQuantity(),
				entity.getCost(),
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
			return (Integer) this.crudUtil.execute("UPDATE inventory_purchase SET is_deleted = TRUE WHERE is_deleted = FALSE AND id = ?", id) == 0 ?
				new Response<>(null, ResponseType.NOT_DELETED) :
				new Response<>(null, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<InventoryPurchaseEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT inventory_id, menu_item_id, supplier_id, quantity, cost, purchased_at FROM inventory_purchase WHERE is_deleted = FALSE AND id = ?", id)) {
			return resultSet.next() ?
				new Response<>(InventoryPurchaseEntity.builder()
					.id(id)
					.inventoryId(resultSet.getLong(1))
					.menuItemId(resultSet.getLong(2))
					.supplierId(resultSet.getLong(3))
					.quantity(resultSet.getInt(4))
					.cost(resultSet.getDouble(5))
					.purchasedAt(DateTimeUtil.parseDateTime(resultSet.getString(6)))
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<InventoryPurchaseEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT id, inventory_id, menu_item_id, supplier_id, quantity, cost, purchased_at FROM inventory_purchase WHERE is_deleted = FALSE")) {
			final List<InventoryPurchaseEntity> inventoryPurchaseEntities = new ArrayList<>();

			while (resultSet.next()) inventoryPurchaseEntities.add(InventoryPurchaseEntity.builder()
				.id(resultSet.getLong(1))
				.inventoryId(resultSet.getLong(2))
				.menuItemId(resultSet.getLong(3))
				.supplierId(resultSet.getLong(4))
				.quantity(resultSet.getInt(5))
				.cost(resultSet.getDouble(6))
				.purchasedAt(DateTimeUtil.parseDateTime(resultSet.getString(7)))
				.build());

			return new Response<>(inventoryPurchaseEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
