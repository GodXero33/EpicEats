package edu.icet.ecom.service.custom.employee;

import edu.icet.ecom.dto.employee.Employee;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface EmployeeService extends SuperService<Employee> {
	Response<Boolean> terminate (Long employeeId);
	Response<Boolean> isExist (Long employeeId);
	Response<Boolean> isPhoneExist (String phone);
	Response<Boolean> isPhoneExist (String phone, Long employeeId);
	Response<Boolean> isEmailExist (String email);
	Response<Boolean> isEmailExist (String email, Long employeeId);
}
