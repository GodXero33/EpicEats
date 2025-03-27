package edu.icet.ecom.repository.custom.impl.inventory;

import edu.icet.ecom.entity.inventory.SupplierEntity;
import edu.icet.ecom.repository.custom.inventory.SupplierRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.Response;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

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
}
