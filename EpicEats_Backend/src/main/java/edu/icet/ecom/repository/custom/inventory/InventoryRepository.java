package edu.icet.ecom.repository.custom.inventory;

import edu.icet.ecom.entity.inventory.InventoryEntity;
import edu.icet.ecom.entity.inventory.SupplierStockRecordEntity;
import edu.icet.ecom.entity.inventory.SupplierStockRecordLiteEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface InventoryRepository extends CrudRepository<InventoryEntity> {
	Response<List<InventoryEntity>> getAllBySupplier (Long supplierId);
	Response<SupplierStockRecordEntity> add (SupplierStockRecordLiteEntity supplierStockRecordEntity);
	Response<SupplierStockRecordEntity> updateStock (SupplierStockRecordLiteEntity supplierStockRecordEntity);
	Response<Boolean> isExist (Long id);
}
