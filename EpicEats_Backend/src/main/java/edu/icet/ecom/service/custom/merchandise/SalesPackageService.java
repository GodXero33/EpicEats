package edu.icet.ecom.service.custom.merchandise;

import edu.icet.ecom.dto.merchandise.SuperSalesPackage;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface SalesPackageService extends SuperService<SuperSalesPackage> {
	Response<Boolean> isNameExist (String name);
	Response<Boolean> isNameExist (String name, Long id);
	Response<SuperSalesPackage> getAllStructured ();
}
