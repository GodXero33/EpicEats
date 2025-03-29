package edu.icet.ecom.service.custom.merchandise;

import edu.icet.ecom.dto.merchandise.SalesPackage;
import edu.icet.ecom.dto.merchandise.SalesPackageRecord;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface SalesPackageService extends SuperService<SalesPackage> {
	Response<SalesPackage> add (SalesPackageRecord salesPackageRecord);
	Response<Boolean> isNameExist (String name);
}
