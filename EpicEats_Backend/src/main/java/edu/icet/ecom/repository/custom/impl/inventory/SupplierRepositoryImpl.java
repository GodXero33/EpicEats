package edu.icet.ecom.repository.custom.impl.inventory;

import edu.icet.ecom.entity.inventory.SupplierEntity;
import edu.icet.ecom.repository.custom.inventory.SupplierRepository;
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
public class SupplierRepositoryImpl implements SupplierRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	@Override
	public Response<SupplierEntity> add (SupplierEntity entity) {
		return null;
	}

	@Override
	public Response<SupplierEntity> update (SupplierEntity entity) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<SupplierEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<SupplierEntity>> getAll () {
		return null;
	}

	@Override
	public Response<Boolean> isExist (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT 1 FROM supplier WHERE is_deleted = FALSE AND id = ?", id)) {
			return resultSet.next() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}
}
