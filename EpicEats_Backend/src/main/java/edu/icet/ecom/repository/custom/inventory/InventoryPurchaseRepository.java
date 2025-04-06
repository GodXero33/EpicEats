package edu.icet.ecom.repository.custom.inventory;

import edu.icet.ecom.entity.inventory.SuperInventoryPurchaseEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface InventoryPurchaseRepository extends CrudRepository<SuperInventoryPurchaseEntity> {
	Response<SuperInventoryPurchaseEntity> add (SuperInventoryPurchaseEntity entity);
	Response<SuperInventoryPurchaseEntity> update (SuperInventoryPurchaseEntity entity);
	Response<SuperInventoryPurchaseEntity> getAllStructured ();
}
