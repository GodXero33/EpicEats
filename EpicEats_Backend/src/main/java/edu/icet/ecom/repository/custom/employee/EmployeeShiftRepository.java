package edu.icet.ecom.repository.custom.employee;

import edu.icet.ecom.entity.employee.EmployeeShiftEntity;
import edu.icet.ecom.repository.FullDataRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface EmployeeShiftRepository extends FullDataRepository<EmployeeShiftEntity> {
	Response<List<EmployeeShiftEntity>> getAllByEmployeeId (Long employeeId);
	Response<Object> deletedByEmployeeId (Long employeeId);
}
