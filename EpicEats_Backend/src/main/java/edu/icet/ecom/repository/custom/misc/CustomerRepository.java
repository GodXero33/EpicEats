package edu.icet.ecom.repository.custom.misc;

import edu.icet.ecom.entity.misc.CustomerEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface CustomerRepository extends CrudRepository<CustomerEntity> {
	Response<Boolean> isExist (Long id);
	Response<Boolean> isPhoneExist (String phone);
	Response<Boolean> isPhoneExist (String phone, Long customerId);
	Response<Boolean> isEmailExist (String email);
	Response<Boolean> isEmailExist (String email, Long customerId);
}
