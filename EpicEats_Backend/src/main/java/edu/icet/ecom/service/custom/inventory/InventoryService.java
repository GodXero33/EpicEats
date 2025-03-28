package edu.icet.ecom.service.custom.inventory;

import edu.icet.ecom.dto.inventory.Inventory;
import edu.icet.ecom.dto.inventory.SupplierStockRecord;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface InventoryService extends SuperService<Inventory> {
	Response<List<Inventory>> getAllBySupplier (Long supplierId);
	Response<SupplierStockRecord> add (SupplierStockRecord supplierStockRecord);
	Response<SupplierStockRecord> updateStock (SupplierStockRecord supplierStockRecord);
	Response<Boolean> isExist (Long id);
}
