package edu.icet.ecom.repository.custom.impl.inventory;

import edu.icet.ecom.entity.inventory.InventoryEntity;
import edu.icet.ecom.repository.custom.inventory.InventoryRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public class InventoryRepositoryImpl implements InventoryRepository {
	@Override
	public Response<InventoryEntity> add (InventoryEntity entity) {
		return null;
	}

	@Override
	public Response<InventoryEntity> update (InventoryEntity entity) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<InventoryEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<InventoryEntity>> getAll () {
		return null;
	}
}
