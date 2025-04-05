package edu.icet.ecom.repository.custom.merchandise;

import edu.icet.ecom.util.Response;

public interface SalesPackageRepository /*extends CrudRepository<SalesPackageLiteEntity>*/ {
//	Response<SalesPackageLiteEntity> add (SalesPackageEntity salesPackageEntity);
	Response<Boolean> isNameExist (String name);
}
