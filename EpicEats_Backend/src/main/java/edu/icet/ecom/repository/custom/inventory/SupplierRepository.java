package edu.icet.ecom.repository.custom.inventory;

import edu.icet.ecom.entity.inventory.SupplierEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface SupplierRepository extends CrudRepository<SupplierEntity> {
	Response<Boolean> isExist (Long id);
}
