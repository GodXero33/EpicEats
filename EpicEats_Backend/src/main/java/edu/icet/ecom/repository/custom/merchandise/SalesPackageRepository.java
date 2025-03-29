package edu.icet.ecom.repository.custom.merchandise;

import edu.icet.ecom.entity.merchandise.SalesPackageEntity;
import edu.icet.ecom.entity.merchandise.SalesPackageRecordEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface SalesPackageRepository extends CrudRepository<SalesPackageEntity> {
	Response<SalesPackageEntity> add (SalesPackageRecordEntity salesPackageRecordEntity);
	Response<Boolean> isNameExist (String name);
}
