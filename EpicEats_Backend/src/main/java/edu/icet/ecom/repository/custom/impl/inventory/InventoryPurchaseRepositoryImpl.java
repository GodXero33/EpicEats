package edu.icet.ecom.repository.custom.impl.inventory;

import edu.icet.ecom.entity.inventory.InventoryEntity;
import edu.icet.ecom.entity.inventory.InventoryPurchaseEntity;
import edu.icet.ecom.entity.inventory.InventoryPurchaseLiteEntity;
import edu.icet.ecom.entity.inventory.SupplierEntity;
import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.repository.custom.inventory.InventoryPurchaseRepository;
import edu.icet.ecom.repository.custom.inventory.InventoryRepository;
import edu.icet.ecom.repository.custom.inventory.SupplierRepository;
import edu.icet.ecom.repository.custom.merchandise.MenuItemRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.DateTimeUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.MenuItemCategory;
import edu.icet.ecom.util.enumaration.ResponseType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class InventoryPurchaseRepositoryImpl implements InventoryPurchaseRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;
	private final InventoryRepository inventoryRepository;
	private final MenuItemRepository menuItemRepository;
	private final SupplierRepository supplierRepository;

	@Override
	public Response<InventoryPurchaseEntity> add (InventoryPurchaseEntity entity) {
		return new Response<>(null, ResponseType.SERVER_ERROR);
	}

	@Override
	public Response<InventoryPurchaseEntity> update (InventoryPurchaseEntity entity) {
		return new Response<>(null, ResponseType.SERVER_ERROR);
	}

	private <T> T getItemFromRepository (CrudRepository<T> repository, Long id) throws SQLException {
		if (id == null) return null;

		final Response<T> response = repository.get(id);

		if (response.getStatus() == ResponseType.SERVER_ERROR) throw new SQLException("Failed to retrieve inventory item");

		return response.getStatus() == ResponseType.FOUND ? response.getData() : null;
	}

	@Override
	public Response<InventoryPurchaseEntity> add (InventoryPurchaseLiteEntity entity) {
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

			final InventoryEntity inventory = this.getItemFromRepository(this.inventoryRepository, entity.getInventoryId());
			final MenuItemEntity menuItem = this.getItemFromRepository(this.menuItemRepository, entity.getMenuItemId());
			final SupplierEntity supplier = this.getItemFromRepository(this.supplierRepository, entity.getSupplierId());

			if ((inventory == null && menuItem == null) || supplier == null) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_CREATED);
			}

			connection.commit();

			final InventoryPurchaseEntity inventoryPurchaseEntity = InventoryPurchaseEntity.builder()
				.id(generatedId)
				.inventory(inventory)
				.menuItem(menuItem)
				.supplier(supplier)
				.quantity(entity.getQuantity())
				.cost(entity.getCost())
				.purchasedAt(DateTimeUtil.parseDateTime(DateTimeUtil.getCurrentDateTime()))
				.build();

			return new Response<>(inventoryPurchaseEntity, ResponseType.CREATED);
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
	public Response<InventoryPurchaseEntity> update (InventoryPurchaseLiteEntity entity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final boolean isUpdated = (Integer) this.crudUtil.execute(
				"UPDATE inventory_purchase SET inventory_id = ?, menu_item_id = ?, supplier_id = ?, quantity = ?, cost = ? WHERE is_deleted = FALSE AND id = ?",
				entity.getInventoryId(),
				entity.getMenuItemId(),
				entity.getSupplierId(),
				entity.getQuantity(),
				entity.getCost(),
				entity.getId()
			) != 0;

			if (!isUpdated) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			final InventoryEntity inventory = this.getItemFromRepository(this.inventoryRepository, entity.getInventoryId());
			final MenuItemEntity menuItem = this.getItemFromRepository(this.menuItemRepository, entity.getMenuItemId());
			final SupplierEntity supplier = this.getItemFromRepository(this.supplierRepository, entity.getSupplierId());

			if ((inventory == null && menuItem == null) || supplier == null) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			connection.commit();

			final InventoryPurchaseEntity inventoryPurchaseEntity = InventoryPurchaseEntity.builder()
				.id(entity.getId())
				.inventory(inventory)
				.menuItem(menuItem)
				.supplier(supplier)
				.quantity(entity.getQuantity())
				.cost(entity.getCost())
				.purchasedAt(DateTimeUtil.parseDateTime(DateTimeUtil.getCurrentDateTime()))
				.build();

			return new Response<>(inventoryPurchaseEntity, ResponseType.UPDATED);
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

	private boolean isResultSetIsMenuItem (String resultMenuItemColumn) {
		return Objects.equals(resultMenuItemColumn, "menu_item");
	}

	@Override
	public Response<InventoryPurchaseEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT
			    ip.quantity,
			    ip.cost,
			    ip.purchased_at,
			    s.id,
			    mi.id,
			    inv.id,
			    CASE
			        WHEN mi.id IS NOT NULL THEN 'menu_item'
			        WHEN inv.id IS NOT NULL THEN 'inventory'
			    END AS type
			FROM inventory_purchase ip
			JOIN supplier s ON ip.supplier_id = s.id AND s.is_deleted = FALSE
			LEFT JOIN menu_item mi ON ip.menu_item_id = mi.id AND mi.is_deleted = FALSE
			LEFT JOIN inventory inv ON ip.inventory_id = inv.id AND inv.is_deleted = FALSE
			WHERE ip.is_deleted = FALSE AND ip.id = ?
			""", id)) {
			if (resultSet.next()) {
				final boolean isMenuItem = this.isResultSetIsMenuItem(resultSet.getString(7));

				return new Response<>(InventoryPurchaseEntity.builder()
					.id(id)
					.quantity(resultSet.getInt(1))
					.cost(resultSet.getDouble(2))
					.purchasedAt(DateTimeUtil.parseDateTime(resultSet.getString(3)))
					.supplier(SupplierEntity.builder().id(resultSet.getLong(4)).build())
					.menuItem(isMenuItem ? MenuItemEntity.builder().id(resultSet.getLong(5)).build() : null)
					.inventory(!isMenuItem ? InventoryEntity.builder().id(resultSet.getLong(6)).build() : null)
					.build(), ResponseType.FOUND);
			}

			return new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<InventoryPurchaseEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT
			    ip.id,
			    ip.quantity,
			    ip.cost,
			    ip.purchased_at,
			    s.id,
			    mi.id,
			    inv.id,
			    CASE
			        WHEN mi.id IS NOT NULL THEN 'menu_item'
			        WHEN inv.id IS NOT NULL THEN 'inventory'
			    END AS type
			FROM inventory_purchase ip
			JOIN supplier s ON ip.supplier_id = s.id AND s.is_deleted = FALSE
			LEFT JOIN menu_item mi ON ip.menu_item_id = mi.id AND mi.is_deleted = FALSE
			LEFT JOIN inventory inv ON ip.inventory_id = inv.id AND inv.is_deleted = FALSE
			WHERE ip.is_deleted = FALSE
			""")) {
			final List<InventoryPurchaseEntity> inventoryPurchaseEntities = new ArrayList<>();

			while (resultSet.next()) {
				final boolean isMenuItem = this.isResultSetIsMenuItem(resultSet.getString(8));

				inventoryPurchaseEntities.add(InventoryPurchaseEntity.builder()
					.id(resultSet.getLong(1))
					.quantity(resultSet.getInt(2))
					.cost(resultSet.getDouble(3))
					.purchasedAt(DateTimeUtil.parseDateTime(resultSet.getString(4)))
					.supplier(SupplierEntity.builder().id(resultSet.getLong(5)).build())
					.menuItem(isMenuItem ? MenuItemEntity.builder().id(resultSet.getLong(6)).build() : null)
					.inventory(!isMenuItem ? InventoryEntity.builder().id(resultSet.getLong(7)).build() : null)
					.build());
			}

			return new Response<>(inventoryPurchaseEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<InventoryPurchaseEntity> getFull (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT
			    ip.quantity,
			    ip.cost,
			    ip.purchased_at,
			    s.id,
			    s.name,
			    s.phone,
			    s.email,
			    s.address,
			    mi.id,
			    mi.name,
			    mi.price,
			    mi.img,
			    mi.category,
			    mi.quantity,
			    inv.id,
			    inv.name,
			    inv.description,
			    inv.quantity,
			    inv.unit,
			    inv.updated_at,
			    CASE
			        WHEN mi.id IS NOT NULL THEN 'menu_item'
			        WHEN inv.id IS NOT NULL THEN 'inventory'
			    END AS type
			FROM inventory_purchase ip
			JOIN supplier s ON ip.supplier_id = s.id AND s.is_deleted = FALSE
			LEFT JOIN menu_item mi ON ip.menu_item_id = mi.id AND mi.is_deleted = FALSE
			LEFT JOIN inventory inv ON ip.inventory_id = inv.id AND inv.is_deleted = FALSE
			WHERE ip.is_deleted = FALSE AND ip.id = ?
			""", id)) {
			if (resultSet.next()) {
				final boolean isMenuItem = this.isResultSetIsMenuItem(resultSet.getString(21));

				return new Response<>(InventoryPurchaseEntity.builder()
					.id(id)
					.quantity(resultSet.getInt(1))
					.cost(resultSet.getDouble(2))
					.purchasedAt(DateTimeUtil.parseDateTime(resultSet.getString(3)))
					.supplier(SupplierEntity.builder()
						.id(resultSet.getLong(4))
						.name(resultSet.getString(5))
						.phone(resultSet.getString(6))
						.email(resultSet.getString(7))
						.address(resultSet.getString(8))
						.build())
					.menuItem(isMenuItem ? MenuItemEntity.builder()
						.id(resultSet.getLong(9))
						.name(resultSet.getString(10))
						.price(resultSet.getDouble(11))
						.img(resultSet.getString(12))
						.category(MenuItemCategory.fromName(resultSet.getString(13)))
						.quantity(resultSet.getInt(14))
						.build() : null)
					.inventory(!isMenuItem ? InventoryEntity.builder()
						.id(resultSet.getLong(15))
						.name(resultSet.getString(16))
						.description(resultSet.getString(17))
						.quantity(resultSet.getInt(18))
						.unit(resultSet.getString(19))
						.updatedAt(DateTimeUtil.parseDateTime(resultSet.getString(20)))
						.build() : null)
					.build(), ResponseType.FOUND);
			}

			return new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<InventoryPurchaseEntity>> getAllFull () {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT
			    ip.id,
			    ip.quantity,
			    ip.cost,
			    ip.purchased_at,
			    s.id,
			    s.name,
			    s.phone,
			    s.email,
			    s.address,
			    mi.id,
			    mi.name,
			    mi.price,
			    mi.img,
			    mi.category,
			    mi.quantity,
			    inv.id,
			    inv.name,
			    inv.description,
			    inv.quantity,
			    inv.unit,
			    inv.updated_at,
			    CASE
			        WHEN mi.id IS NOT NULL THEN 'menu_item'
			        WHEN inv.id IS NOT NULL THEN 'inventory'
			    END AS type
			FROM inventory_purchase ip
			JOIN supplier s ON ip.supplier_id = s.id AND s.is_deleted = FALSE
			LEFT JOIN menu_item mi ON ip.menu_item_id = mi.id AND mi.is_deleted = FALSE
			LEFT JOIN inventory inv ON ip.inventory_id = inv.id AND inv.is_deleted = FALSE
			WHERE ip.is_deleted = FALSE
			""")) {
			final List<InventoryPurchaseEntity> inventoryPurchaseEntities = new ArrayList<>();

			while (resultSet.next()) {
				final boolean isMenuItem = this.isResultSetIsMenuItem(resultSet.getString(22));

				inventoryPurchaseEntities.add(InventoryPurchaseEntity.builder()
					.id(resultSet.getLong(1))
					.quantity(resultSet.getInt(2))
					.cost(resultSet.getDouble(3))
					.purchasedAt(DateTimeUtil.parseDateTime(resultSet.getString(4)))
					.supplier(SupplierEntity.builder()
						.id(resultSet.getLong(5))
						.name(resultSet.getString(6))
						.phone(resultSet.getString(7))
						.email(resultSet.getString(8))
						.address(resultSet.getString(9))
						.build())
					.menuItem(isMenuItem ? MenuItemEntity.builder()
						.id(resultSet.getLong(10))
						.name(resultSet.getString(11))
						.price(resultSet.getDouble(12))
						.img(resultSet.getString(13))
						.category(MenuItemCategory.fromName(resultSet.getString(14)))
						.quantity(resultSet.getInt(15))
						.build() : null)
					.inventory(!isMenuItem ? InventoryEntity.builder()
						.id(resultSet.getLong(16))
						.name(resultSet.getString(17))
						.description(resultSet.getString(18))
						.quantity(resultSet.getInt(19))
						.unit(resultSet.getString(20))
						.updatedAt(DateTimeUtil.parseDateTime(resultSet.getString(21)))
						.build() : null)
					.build());
			}

			return new Response<>(inventoryPurchaseEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
