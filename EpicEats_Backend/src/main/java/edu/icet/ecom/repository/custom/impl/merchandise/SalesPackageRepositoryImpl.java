package edu.icet.ecom.repository.custom.impl.merchandise;

import edu.icet.ecom.entity.merchandise.*;
import edu.icet.ecom.repository.custom.merchandise.MenuItemRepository;
import edu.icet.ecom.repository.custom.merchandise.SalesPackageRepository;
import edu.icet.ecom.util.CrudUtil;
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
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SalesPackageRepositoryImpl implements SalesPackageRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;
	private final MenuItemRepository menuItemRepository;

	private String getSalesPackageItemsInsertQuery (SalesPackageLiteEntity salesPackageLiteEntity) {
		final StringBuilder salesPackageItemsInsertQueryBuilder = new StringBuilder();
		final int salesPackageItemsSize = salesPackageLiteEntity.getMenuItemIDs().size();

		salesPackageItemsInsertQueryBuilder.append("""
				INSERT INTO sales_package_item (package_id, item_id, quantity)
				VALUES%s
				""".formatted(" (?, ?, ?),".repeat(salesPackageItemsSize)));
		salesPackageItemsInsertQueryBuilder.setLength(salesPackageItemsInsertQueryBuilder.length() - 2);

		return salesPackageItemsInsertQueryBuilder.toString();
	}

	private Object[] getSalesPackageItemsInsertBindsArray (SalesPackageLiteEntity salesPackageLiteEntity, Long packageId) {
		final int salesPackageItemsSize = salesPackageLiteEntity.getMenuItemIDs().size();
		final Object[] salesPackageItemsInsertBindsArray = new Object[salesPackageItemsSize * 3];
		final List<Long> menuItemIDs = salesPackageLiteEntity.getMenuItemIDs();
		final List<Integer> menuItemQuantities = salesPackageLiteEntity.getMenuItemQuantities();

		for (int a = 0; a < salesPackageItemsSize; a++) {
			salesPackageItemsInsertBindsArray[a * 3] = packageId;
			salesPackageItemsInsertBindsArray[a * 3 + 1] = menuItemIDs.get(a);
			salesPackageItemsInsertBindsArray[a * 3 + 2] = menuItemQuantities.get(a);
		}

		return salesPackageItemsInsertBindsArray;
	}

	private Response<SuperSalesPackageEntity> buildSalesPackageEntityFromSalesPackageLiteEntity (SalesPackageLiteEntity salesPackageLiteEntity, Long packageId, Connection connection, boolean isUpdate) throws SQLException {
		if ((Integer) this.crudUtil.execute(
			this.getSalesPackageItemsInsertQuery(salesPackageLiteEntity),
			this.getSalesPackageItemsInsertBindsArray(salesPackageLiteEntity, packageId)
		) == 0) {
			connection.rollback();
			return new Response<>(null, isUpdate ? ResponseType.NOT_UPDATED : ResponseType.NOT_CREATED);
		}

		final Response<List<MenuItemEntity>> menuItemsGetResponse = this.menuItemRepository.getAllByIDs(salesPackageLiteEntity.getMenuItemIDs());

		if (menuItemsGetResponse.getStatus() == ResponseType.SERVER_ERROR) {
			connection.rollback();
			return new Response<>(null, menuItemsGetResponse.getStatus());
		}

		connection.commit();

		return new Response<>(SalesPackageEntity.builder()
			.id(packageId)
			.name(salesPackageLiteEntity.getName())
			.description(salesPackageLiteEntity.getDescription())
			.discountPercentage(salesPackageLiteEntity.getDiscountPercentage())
			.menuItems(menuItemsGetResponse.getData())
			.menuItemQuantities(salesPackageLiteEntity.getMenuItemQuantities())
			.build()
			, isUpdate ? ResponseType.UPDATED : ResponseType.CREATED);
	}

	@Override
	public Response<SuperSalesPackageEntity> add (SuperSalesPackageEntity entity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final SalesPackageLiteEntity salesPackageLiteEntity = (SalesPackageLiteEntity) entity;

			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"""
				INSERT INTO sales_package (name, description, discount_percentage)
				VALUES (?, ?, ?)
				""",
				salesPackageLiteEntity.getName(),
				salesPackageLiteEntity.getDescription(),
				salesPackageLiteEntity.getDiscountPercentage()
			);

			return this.buildSalesPackageEntityFromSalesPackageLiteEntity(salesPackageLiteEntity, generatedId, connection, false);
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
	public Response<SuperSalesPackageEntity> update (SuperSalesPackageEntity entity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final SalesPackageLiteEntity salesPackageLiteEntity = (SalesPackageLiteEntity) entity;

			if ((Integer) this.crudUtil.execute("""
				UPDATE sales_package
				SET name = ?, description = ?, discount_percentage = ?
				WHERE is_deleted = FALSE AND id = ?
				""",
				salesPackageLiteEntity.getName(),
				salesPackageLiteEntity.getDescription(),
				salesPackageLiteEntity.getDiscountPercentage(),
				salesPackageLiteEntity.getId()
				) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			if ((Integer) this.crudUtil.execute("""
				UPDATE sales_package_item
				SET is_deleted = TRUE
				WHERE package_id = ?
				""", salesPackageLiteEntity.getId()) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			return this.buildSalesPackageEntityFromSalesPackageLiteEntity(salesPackageLiteEntity, salesPackageLiteEntity.getId(), connection, true);
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
	public Response<Object> delete (Long id) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			if ((Integer) this.crudUtil.execute(
				"""
				UPDATE sales_package
				SET is_deleted = TRUE
				WHERE is_deleted = FALSE AND id = ?
				""",
				id
			) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_DELETED);
			}

			if ((Integer) this.crudUtil.execute(
				"""
				UPDATE sales_package_item
				SET is_deleted = TRUE
				WHERE is_deleted = FALSE AND package_id = ?
				""",
				id
			) == 0) {
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
	public Response<SuperSalesPackageEntity> get (Long id) {
		try (final ResultSet salesPackageResultSet = this.crudUtil.execute("""
			SELECT name, description, discount_percentage
			FROM sales_package
			WHERE is_deleted = FALSE AND id = ?
			""", id)) {
				if (!salesPackageResultSet.next()) return new Response<>(null, ResponseType.NOT_FOUND);

				final List<Long> menuItemIds = new ArrayList<>();
				final List<Integer> menuItemQuantities = new ArrayList<>();

				try (final ResultSet salesPackageItemsResultSet = this.crudUtil.execute("""
					SELECT item_id, quantity
					FROM sales_package_item
					WHERE is_deleted = FALSE AND package_id = ?
					""", id)) {
					while (salesPackageItemsResultSet.next()) {
						menuItemIds.add(salesPackageItemsResultSet.getLong(1));
						menuItemQuantities.add(salesPackageItemsResultSet.getInt(2));
					}
				}

			final Response<List<MenuItemEntity>> menuItemsGetResponse = this.menuItemRepository.getAllByIDs(menuItemIds);

			if (menuItemsGetResponse.getStatus() == ResponseType.SERVER_ERROR) return new Response<>(null, menuItemsGetResponse.getStatus());

			return new Response<>(SalesPackageEntity.builder()
				.id(id)
				.name(salesPackageResultSet.getString(1))
				.description(salesPackageResultSet.getString(2))
				.discountPercentage(salesPackageResultSet.getDouble(3))
				.menuItems(menuItemsGetResponse.getData())
				.menuItemQuantities(menuItemQuantities)
				.build()
				, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<SuperSalesPackageEntity>> getAll () {
		return null;
	}

	@Override
	public Response<Boolean> isNameExist (String name) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT 1 FROM sales_package WHERE name = ?", name)) {
			return resultSet.next() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> isNameExist (String name, Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT 1 FROM sales_package WHERE id != ? AND name = ?", id, name)) {
			return resultSet.next() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}

	private Map.Entry<List<Long>, List<Integer>> getSalesPackageDetailsByPackageId (Long packageId) throws SQLException {
		final List<Long> menuItemIds = new ArrayList<>();
		final List<Integer> menuItemQuantities = new ArrayList<>();

		try (final ResultSet salesPackageDetailsResultSet = this.crudUtil.execute("""
			SELECT item_id, quantity
			FROM sales_package_item
			WHERE is_deleted = FALSE AND package_id = ?
			""", packageId)) {
			while (salesPackageDetailsResultSet.next()) {
				menuItemIds.add(salesPackageDetailsResultSet.getLong(1));
				menuItemQuantities.add(salesPackageDetailsResultSet.getInt(2));
			}
		}

		return Map.entry(menuItemIds, menuItemQuantities);
	}

	@Override
	public Response<SuperSalesPackageEntity> getAllStructured () {
		try (final ResultSet salesPackageResultSet = this.crudUtil.execute("""
			SELECT id, name, description, discount_percentage
			FROM sales_package
			WHERE is_deleted = FALSE
			""")) {
			final List<SalesPackageLiteEntity> salesPackages = new ArrayList<>();
			final List<Long> allMenuItemIds = new ArrayList<>();

			while (salesPackageResultSet.next()) {
				final SalesPackageLiteEntity salesPackageLiteEntity = SalesPackageLiteEntity.builder()
					.id(salesPackageResultSet.getLong(1))
					.name(salesPackageResultSet.getString(2))
					.description(salesPackageResultSet.getString(3))
					.discountPercentage(salesPackageResultSet.getDouble(4))
					.build();

				final Map.Entry<List<Long>, List<Integer>> salesPackageDetails = this.getSalesPackageDetailsByPackageId(salesPackageResultSet.getLong(1));

				salesPackageLiteEntity.setMenuItemIDs(salesPackageDetails.getKey());
				salesPackageLiteEntity.setMenuItemQuantities(salesPackageDetails.getValue());
				salesPackages.add(salesPackageLiteEntity);
				allMenuItemIds.addAll(salesPackageDetails.getKey());
			}

			final Response<List<MenuItemEntity>> menuItemsGetResponse = this.menuItemRepository.getAllByIDs(allMenuItemIds);

			if (menuItemsGetResponse.getStatus() == ResponseType.SERVER_ERROR) return new Response<>(null, menuItemsGetResponse.getStatus());

			return new Response<>(new AllSalesPackagesEntity(
				menuItemsGetResponse.getData(),
				salesPackages
			), ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
