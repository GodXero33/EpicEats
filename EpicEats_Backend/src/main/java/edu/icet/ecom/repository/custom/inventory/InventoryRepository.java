package edu.icet.ecom.repository.custom.inventory;

import edu.icet.ecom.entity.inventory.InventoryEntity;
import edu.icet.ecom.entity.inventory.SupplierInventoryRecordEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface InventoryRepository extends CrudRepository<InventoryEntity> {
	Response<List<SupplierInventoryRecordEntity>> getAllBySupplier (Long supplierId);
}
