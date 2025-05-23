package edu.icet.ecom.repository.custom.employee;

import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<EmployeeEntity> {
	Response<Object> terminate (Long employeeId);
	Response<Boolean> isExist (Long employeeId);
	Response<Boolean> isPhoneExist (String phone);
	Response<Boolean> isPhoneExist (String phone, Long employeeId);
	Response<Boolean> isEmailExist (String email);
	Response<Boolean> isEmailExist (String email, Long employeeId);
	Response<List<EmployeeEntity>> getAllByIDs (List<Long> ids);
	Response<List<EmployeeEntity>> filter (EmployeeEntity employee);
}
