package edu.icet.ecom.repository.custom.impl.merchandise;

import edu.icet.ecom.dto.merchandise.SalesPackageRecord;
import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import edu.icet.ecom.entity.merchandise.SalesPackageEntity;
import edu.icet.ecom.entity.merchandise.SalesPackageItemEntity;
import edu.icet.ecom.entity.merchandise.SalesPackageRecordEntity;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SalesPackageRepositoryImpl implements SalesPackageRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;
	private final MenuItemRepository menuItemRepository;

	@Override
	public Response<SalesPackageEntity> add (SalesPackageEntity entity) {
		return null;
	}

	@Override
	public Response<SalesPackageEntity> update (SalesPackageEntity entity) {
		return null;
	}

	@Override
	public Response<Object> delete (Long id) {
		return null;
	}

	@Override
	public Response<SalesPackageEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<SalesPackageEntity>> getAll () {
		return null;
	}

	@Override
	public Response<SalesPackageEntity> add (SalesPackageRecordEntity salesPackageRecordEntity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"INSERT INTO sales_package (name, description, discount_percentage) VALUES (?, ?, ?)",
				salesPackageRecordEntity.getName(),
				salesPackageRecordEntity.getDescription(),
				salesPackageRecordEntity.getDiscountPercentage()
			);

			final List<SalesPackageItemEntity> salesPackageItemEntities = salesPackageRecordEntity.getSalesPackageItems();
			final boolean isSalesPackageItemEntitiesInserted = (Integer) this.crudUtil.execute(
				"INSERT INTO sales_package_item (package_id, item_id, quantity) VALUES " + String.join(", ", Collections.nCopies(salesPackageItemEntities.size(), "(?, ?, ?)")),
				salesPackageItemEntities.stream().map(salesPackageItemEntity -> new Object[] {
					generatedId,
					salesPackageItemEntity.getItemId(),
					salesPackageItemEntity.getQuantity()
				}).flatMap(Arrays::stream).toList()
			) != 0;

			if (!isSalesPackageItemEntitiesInserted) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_CREATED);
			}

			connection.commit();

			return new Response<>(SalesPackageEntity.builder()
					.id(generatedId)
					.name(salesPackageRecordEntity.getName())
					.description(salesPackageRecordEntity.getDescription())
					.discountPercentage(salesPackageRecordEntity.getDiscountPercentage())
					.menuItemIDs(salesPackageItemEntities.stream().map(SalesPackageItemEntity::getItemId).toList())
					.build(), ResponseType.CREATED);
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
}
