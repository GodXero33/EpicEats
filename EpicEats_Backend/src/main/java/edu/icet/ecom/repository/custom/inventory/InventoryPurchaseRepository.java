package edu.icet.ecom.repository.custom.inventory;

import edu.icet.ecom.entity.inventory.InventoryPurchaseEntity;
import edu.icet.ecom.entity.inventory.InventoryPurchaseLiteEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface InventoryPurchaseRepository extends CrudRepository<InventoryPurchaseEntity> {
	Response<InventoryPurchaseEntity> add (InventoryPurchaseLiteEntity entity);
	Response<InventoryPurchaseEntity> update (InventoryPurchaseLiteEntity entity);
}
