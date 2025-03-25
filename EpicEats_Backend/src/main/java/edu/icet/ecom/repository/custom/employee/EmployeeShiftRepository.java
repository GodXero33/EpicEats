package edu.icet.ecom.repository.custom.employee;

import edu.icet.ecom.entity.employee.EmployeeShiftEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface EmployeeShiftRepository extends CrudRepository<EmployeeShiftEntity> {
	Response<List<EmployeeShiftEntity>> getAllByEmployeeId (Long employeeId);
	Response<Boolean> deleteByEmployeeId (Long employeeId);
}
