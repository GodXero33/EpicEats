package edu.icet.ecom.service.custom.employee;

import edu.icet.ecom.dto.employee.Employee;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface EmployeeService extends SuperService<Employee> {
	Response<Boolean> terminate (Long employeeId);
}
