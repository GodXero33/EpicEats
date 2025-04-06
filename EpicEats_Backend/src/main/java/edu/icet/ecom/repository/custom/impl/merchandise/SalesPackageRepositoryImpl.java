package edu.icet.ecom.repository.custom.impl.merchandise;

import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import edu.icet.ecom.entity.merchandise.SalesPackageEntity;
import edu.icet.ecom.entity.merchandise.SalesPackageLiteEntity;
import edu.icet.ecom.entity.merchandise.SuperSalesPackageEntity;
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
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SalesPackageRepositoryImpl implements SalesPackageRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;
	private final MenuItemRepository menuItemRepository;

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

			final StringBuilder salesPackageItemsInsertQueryBuilder = new StringBuilder();
			final int salesPackageItemsSize = salesPackageLiteEntity.getMenuItemIDs().size();
			final Object[] salesPackageItemsInsertBindsArray = new Object[salesPackageItemsSize * 3];
			final List<Long> menuItemIDs = salesPackageLiteEntity.getMenuItemIDs();
			final List<Integer> menuItemQuantities = salesPackageLiteEntity.getMenuItemQuantities();

			salesPackageItemsInsertQueryBuilder.append("""
				INSERT INTO sales_package_item (package_id, item_id, quantity)
				VALUES %s
				""".formatted(" (?, ?, ?),".repeat(salesPackageItemsSize)));
			salesPackageItemsInsertQueryBuilder.setLength(salesPackageItemsInsertQueryBuilder.length() - 1);

			for (int a = 0; a < salesPackageItemsSize; a++) {
				salesPackageItemsInsertBindsArray[a * 3] = generatedId;
				salesPackageItemsInsertBindsArray[a * 3 + 1] = menuItemIDs.get(a);
				salesPackageItemsInsertBindsArray[a * 3 + 2] = menuItemQuantities.get(a);
			}

			if ((Integer) this.crudUtil.execute(salesPackageItemsInsertQueryBuilder.toString(), salesPackageItemsInsertBindsArray) == 0) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_CREATED);
			}

			final Response<List<MenuItemEntity>> menuItemsGetResponse = this.menuItemRepository.getAllByIDs(menuItemIDs);

			if (menuItemsGetResponse.getStatus() == ResponseType.SERVER_ERROR) {
				connection.rollback();
				return new Response<>(null, menuItemsGetResponse.getStatus());
			}

			connection.commit();

			return new Response<>(SalesPackageEntity.builder()
				.id(generatedId)
				.name(salesPackageLiteEntity.getName())
				.description(salesPackageLiteEntity.getDescription())
				.discountPercentage(salesPackageLiteEntity.getDiscountPercentage())
				.menuItems(menuItemsGetResponse.getData())
				.menuItemQuantities(menuItemQuantities)
				.build()
			, ResponseType.CREATED);
		} catch (SQLException exception) {
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
		return null;
	}

	@Override
	public Response<Object> delete (Long id) {
		try {
			return new Response<>(null, (Integer) this.crudUtil.execute(
				"""
				UPDATE sales_package
				SET is_deleted = TRUE
				WHERE is_deleted = FALSE AND id = ?
				""",
				id
			) == 0 ?
				ResponseType.NOT_DELETED :
				null);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<SuperSalesPackageEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<SuperSalesPackageEntity>> getAll () {
		return new Response<>(null, ResponseType.SERVER_ERROR);
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
	public Response<SuperSalesPackageEntity> getAllStructured () {
		return null;
	}
}
