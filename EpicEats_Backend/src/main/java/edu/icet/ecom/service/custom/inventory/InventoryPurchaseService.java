package edu.icet.ecom.service.custom.inventory;

import edu.icet.ecom.dto.inventory.SuperInventoryPurchase;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface InventoryPurchaseService extends SuperService<SuperInventoryPurchase> {
	Response<SuperInventoryPurchase> add (SuperInventoryPurchase inventoryPurchase);
	Response<SuperInventoryPurchase> update (SuperInventoryPurchase inventoryPurchase);
	Response<SuperInventoryPurchase> getAllStructured ();
}
