package edu.icet.ecom.repository.custom.merchandise;

import edu.icet.ecom.entity.merchandise.SuperSalesPackageEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface SalesPackageRepository extends CrudRepository<SuperSalesPackageEntity> {
	Response<Boolean> isNameExist (String name);
	Response<SuperSalesPackageEntity> getAllStructured ();
}
