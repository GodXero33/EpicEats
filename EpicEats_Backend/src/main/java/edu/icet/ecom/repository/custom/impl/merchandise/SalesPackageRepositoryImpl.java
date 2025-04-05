package edu.icet.ecom.repository.custom.impl.merchandise;

import edu.icet.ecom.entity.merchandise.SuperSalesPackageEntity;
import edu.icet.ecom.repository.custom.merchandise.MenuItemRepository;
import edu.icet.ecom.repository.custom.merchandise.SalesPackageRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

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
		return null;
	}

	@Override
	public Response<SuperSalesPackageEntity> update (SuperSalesPackageEntity entity) {
		return null;
	}

	@Override
	public Response<Object> delete (Long id) {
		try {
			return new Response<>(null, (Integer) this.crudUtil.execute("UPDATE sales_package SET is_deleted = TRUE WHERE is_deleted = FALSE AND id = ?", id) == 0 ?
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
