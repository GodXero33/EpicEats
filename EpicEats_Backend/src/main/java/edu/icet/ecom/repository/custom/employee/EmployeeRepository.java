package edu.icet.ecom.repository.custom.employee;

import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface EmployeeRepository extends CrudRepository<EmployeeEntity> {
	Response<Boolean> terminate (Long employeeId);
}
