package edu.icet.ecom.repository.custom.impl.inventory;

import edu.icet.ecom.entity.inventory.*;
import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.repository.custom.inventory.InventoryPurchaseRepository;
import edu.icet.ecom.repository.custom.inventory.InventoryRepository;
import edu.icet.ecom.repository.custom.inventory.SupplierRepository;
import edu.icet.ecom.repository.custom.merchandise.MenuItemRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.DateTimeUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enums.MenuItemCategory;
import edu.icet.ecom.util.enums.ResponseType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class InventoryPurchaseRepositoryImpl implements InventoryPurchaseRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;
	private final InventoryRepository inventoryRepository;
	private final MenuItemRepository menuItemRepository;
	private final SupplierRepository supplierRepository;

	private <T> T getItemFromRepository (CrudRepository<T> repository, Long id) throws SQLException {
		if (id == null) return null;

		final Response<T> response = repository.get(id);

		if (response.getStatus() == ResponseType.SERVER_ERROR) throw new SQLException("Failed to retrieve inventory item");

		return response.getStatus() == ResponseType.FOUND ? response.getData() : null;
	}

	@Override
	public Response<SuperInventoryPurchaseEntity> add (SuperInventoryPurchaseEntity entity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final InventoryPurchaseLiteEntity liteEntity = (InventoryPurchaseLiteEntity) entity;
			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"""
				INSERT INTO inventory_purchase (inventory_id, menu_item_id, supplier_id, quantity, cost)
				VALUES (?, ?, ?, ?, ?)
				""",
				liteEntity.getInventoryId(),
				liteEntity.getMenuItemId(),
				liteEntity.getSupplierId(),
				liteEntity.getQuantity(),
				liteEntity.getCost()
			);

			final InventoryEntity inventory = this.getItemFromRepository(this.inventoryRepository, liteEntity.getInventoryId());
			final MenuItemEntity menuItem = this.getItemFromRepository(this.menuItemRepository, liteEntity.getMenuItemId());
			final SupplierEntity supplier = this.getItemFromRepository(this.supplierRepository, liteEntity.getSupplierId());

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
				.quantity(liteEntity.getQuantity())
				.cost(liteEntity.getCost())
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
	public Response<SuperInventoryPurchaseEntity> update (SuperInventoryPurchaseEntity entity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final InventoryPurchaseLiteEntity liteEntity = (InventoryPurchaseLiteEntity) entity;
			final boolean isUpdated = (Integer) this.crudUtil.execute(
				"""
				UPDATE inventory_purchase
				SET inventory_id = ?, menu_item_id = ?, supplier_id = ?, quantity = ?, cost = ?
				WHERE is_deleted = FALSE AND id = ?
				""",
				liteEntity.getInventoryId(),
				liteEntity.getMenuItemId(),
				liteEntity.getSupplierId(),
				liteEntity.getQuantity(),
				liteEntity.getCost(),
				liteEntity.getId()
			) != 0;

			if (!isUpdated) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			final InventoryEntity inventory = this.getItemFromRepository(this.inventoryRepository, liteEntity.getInventoryId());
			final MenuItemEntity menuItem = this.getItemFromRepository(this.menuItemRepository, liteEntity.getMenuItemId());
			final SupplierEntity supplier = this.getItemFromRepository(this.supplierRepository, liteEntity.getSupplierId());

			if ((inventory == null && menuItem == null) || supplier == null) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			connection.commit();

			final InventoryPurchaseEntity inventoryPurchaseEntity = InventoryPurchaseEntity.builder()
				.id(liteEntity.getId())
				.inventory(inventory)
				.menuItem(menuItem)
				.supplier(supplier)
				.quantity(liteEntity.getQuantity())
				.cost(liteEntity.getCost())
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
	public Response<SuperInventoryPurchaseEntity> getAllStructured () {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT
			inventory_purchase.id, inventory_purchase.quantity, inventory_purchase.cost, inventory_purchase.purchased_at,
			supplier.id, supplier.name, supplier.phone, supplier.email, supplier.address,
			menu_item.id, menu_item.name, menu_item.price, menu_item.img, menu_item.category, menu_item.quantity,
			inventory.id, inventory.name, inventory.description, inventory.quantity, inventory.unit, inventory.updated_at,
			CASE
			    WHEN menu_item.id IS NOT NULL THEN 'menu_item'
			    WHEN inventory.id IS NOT NULL THEN 'inventory'
			END AS type
			FROM inventory_purchase
			JOIN supplier ON inventory_purchase.supplier_id = supplier.id AND supplier.is_deleted = FALSE
			LEFT JOIN menu_item ON inventory_purchase.menu_item_id = menu_item.id AND menu_item.is_deleted = FALSE
			LEFT JOIN inventory ON inventory_purchase.inventory_id = inventory.id AND inventory.is_deleted = FALSE
			WHERE inventory_purchase.is_deleted = FALSE
			""")) {
			final Map<Long, SupplierEntity> suppliersMap = new HashMap<>();
			final Map<Long, InventoryEntity> inventoryMap = new HashMap<>();
			final Map<Long, MenuItemEntity> menuItemMap = new HashMap<>();
			final List<InventoryPurchaseLiteEntity> inventoryPurchaseLiteEntities = new ArrayList<>();

			while (resultSet.next()) {
				final boolean isMenuItem = this.isResultSetIsMenuItem(resultSet.getString(22));
				final InventoryPurchaseLiteEntity inventoryPurchaseLiteEntity = InventoryPurchaseLiteEntity.builder()
					.id(resultSet.getLong(1))
					.quantity(resultSet.getInt(2))
					.cost(resultSet.getDouble(3))
					.purchasedAt(DateTimeUtil.parseDateTime(resultSet.getString(4)))
					.build();

				final long supplierId = resultSet.getLong(5);

				inventoryPurchaseLiteEntity.setSupplierId(supplierId);
				suppliersMap.putIfAbsent(supplierId, SupplierEntity.builder()
					.id(supplierId)
					.name(resultSet.getString(6))
					.phone(resultSet.getString(7))
					.email(resultSet.getString(8))
					.address(resultSet.getString(9))
					.build());

				if (isMenuItem) {
					final long menuItemId = resultSet.getLong(10);

					inventoryPurchaseLiteEntity.setMenuItemId(menuItemId);
					menuItemMap.putIfAbsent(menuItemId, MenuItemEntity.builder()
						.id(menuItemId)
						.name(resultSet.getString(11))
						.price(resultSet.getDouble(12))
						.img(resultSet.getString(13))
						.category(MenuItemCategory.fromName(resultSet.getString(14)))
						.quantity(resultSet.getInt(15))
						.build());
				} else {
					final long inventoryId = resultSet.getLong(16);

					inventoryPurchaseLiteEntity.setInventoryId(inventoryId);
					inventoryMap.putIfAbsent(inventoryId, InventoryEntity.builder()
						.id(inventoryId)
						.name(resultSet.getString(17))
						.description(resultSet.getString(18))
						.quantity(resultSet.getInt(19))
						.unit(resultSet.getString(20))
						.updatedAt(DateTimeUtil.parseDateTime(resultSet.getString(21)))
						.build());
				}

				inventoryPurchaseLiteEntities.add(inventoryPurchaseLiteEntity);
			}

			return new Response<>(AllInventoryPurchasesEntity.builder()
				.suppliers(suppliersMap.values().stream().toList())
				.inventories(inventoryMap.values().stream().toList())
				.menuItems(menuItemMap.values().stream().toList())
				.purchases(inventoryPurchaseLiteEntities)
				.build(), ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Object> delete (Long id) {
		try {
			return (Integer) this.crudUtil.execute("""
				UPDATE inventory_purchase
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

	private boolean isResultSetIsMenuItem (String resultMenuItemColumn) {
		return Objects.equals(resultMenuItemColumn, "menu_item");
	}

	@Override
	public Response<SuperInventoryPurchaseEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT
			inventory_purchase.quantity, inventory_purchase.cost, inventory_purchase.purchased_at,
			supplier.id, supplier.name, supplier.phone, supplier.email, supplier.address,
			menu_item.id, menu_item.name, menu_item.price, menu_item.img, menu_item.category, menu_item.quantity,
			inventory.id, inventory.name, inventory.description, inventory.quantity, inventory.unit, inventory.updated_at,
			CASE
			    WHEN menu_item.id IS NOT NULL THEN 'menu_item'
			    WHEN inventory.id IS NOT NULL THEN 'inventory'
			END AS type
			FROM inventory_purchase
			JOIN supplier ON inventory_purchase.supplier_id = supplier.id AND supplier.is_deleted = FALSE
			LEFT JOIN menu_item ON ip.menu_item_id = menu_item.id AND menu_item.is_deleted = FALSE
			LEFT JOIN inventory ON ip.inventory_id = inventory.id AND inventory.is_deleted = FALSE
			WHERE inventory_purchase.is_deleted = FALSE AND inventory_purchase.id = ?
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
	public Response<List<SuperInventoryPurchaseEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT
			inventory_purchase.id, inventory_purchase.quantity, inventory_purchase.cost, inventory_purchase.purchased_at,
			supplier.id, supplier.name, supplier.phone, supplier.email, supplier.address,
			menu_item.id, menu_item.name, menu_item.price, menu_item.img, menu_item.category, menu_item.quantity,
			inventory.id, inventory.name, inventory.description, inventory.quantity, inventory.unit, inventory.updated_at,
			CASE
			    WHEN menu_item.id IS NOT NULL THEN 'menu_item'
			    WHEN inventory.id IS NOT NULL THEN 'inventory'
			END AS type
			FROM inventory_purchase
			JOIN supplier ON inventory_purchase.supplier_id = supplier.id AND supplier.is_deleted = FALSE
			LEFT JOIN menu_item ON ip.menu_item_id = menu_item.id AND menu_item.is_deleted = FALSE
			LEFT JOIN inventory ON ip.inventory_id = inventory.id AND inventory.is_deleted = FALSE
			WHERE inventory_purchase.is_deleted = FALSE
			""")) {
			final List<SuperInventoryPurchaseEntity> inventoryPurchaseEntities = new ArrayList<>();

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
