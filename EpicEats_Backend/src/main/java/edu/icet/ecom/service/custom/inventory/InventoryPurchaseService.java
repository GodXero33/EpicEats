package edu.icet.ecom.service.custom.inventory;

import edu.icet.ecom.dto.inventory.AllInventoryPurchases;
import edu.icet.ecom.dto.inventory.InventoryPurchase;
import edu.icet.ecom.dto.inventory.InventoryPurchaseLite;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface InventoryPurchaseService extends SuperService<InventoryPurchase> {
	Response<InventoryPurchase> add (InventoryPurchaseLite inventoryPurchase);
	Response<InventoryPurchase> update (InventoryPurchaseLite inventoryPurchase);
	Response<AllInventoryPurchases> getAllStructured ();
}
