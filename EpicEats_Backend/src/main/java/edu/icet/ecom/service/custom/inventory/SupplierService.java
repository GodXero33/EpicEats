package edu.icet.ecom.service.custom.inventory;

import edu.icet.ecom.dto.inventory.Supplier;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface SupplierService extends SuperService<Supplier> {
	Response<Boolean> isExist (Long id);
	Response<Boolean> isPhoneExist (String phone);
	Response<Boolean> isPhoneExist (String phone, Long supplierId);
	Response<Boolean> isEmailExist (String email);
	Response<Boolean> isEmailExist (String email, Long supplierId);
}
