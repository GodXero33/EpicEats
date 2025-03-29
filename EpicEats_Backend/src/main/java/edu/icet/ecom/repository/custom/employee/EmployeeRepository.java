package edu.icet.ecom.repository.custom.employee;

import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface EmployeeRepository extends CrudRepository<EmployeeEntity> {
	Response<Object> terminate (Long employeeId);
	Response<Boolean> isExist (Long employeeId);
	Response<Boolean> isPhoneExist (String phone);
	Response<Boolean> isPhoneExist (String phone, Long employeeId);
	Response<Boolean> isEmailExist (String email);
	Response<Boolean> isEmailExist (String email, Long employeeId);
}
