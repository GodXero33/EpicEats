package edu.icet.ecom.repository.custom.impl.inventory;

import edu.icet.ecom.entity.inventory.SupplierEntity;
import edu.icet.ecom.repository.custom.inventory.SupplierRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public class SupplierRepositoryImpl implements SupplierRepository {
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
