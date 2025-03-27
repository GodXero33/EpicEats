package edu.icet.ecom.repository.custom.impl.inventory;

import edu.icet.ecom.entity.inventory.InventoryPurchaseEntity;
import edu.icet.ecom.repository.custom.inventory.InventoryPurchaseRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.Response;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InventoryPurchaseRepositoryImpl implements InventoryPurchaseRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	@Override
	public Response<InventoryPurchaseEntity> add (InventoryPurchaseEntity entity) {
		return null;
	}

	@Override
	public Response<InventoryPurchaseEntity> update (InventoryPurchaseEntity entity) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<InventoryPurchaseEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<InventoryPurchaseEntity>> getAll () {
		return null;
	}
}
