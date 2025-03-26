package edu.icet.ecom.service.custom.misc;

import edu.icet.ecom.dto.misc.Customer;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface CustomerService extends SuperService<Customer> {
	Response<Boolean> isPhoneExist (String phone);
	Response<Boolean> isPhoneExist (String phone, Long customerId);
	Response<Boolean> isEmailExist (String email);
	Response<Boolean> isEmailExist (String email, Long customerId);
}
