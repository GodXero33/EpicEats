package edu.icet.ecom.service.custom.employee;

import edu.icet.ecom.dto.employee.EmployeeShift;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface EmployeeShiftService extends SuperService<EmployeeShift> {
	Response<EmployeeShift> getFull (Long id);
	Response<List<EmployeeShift>> getAllFull ();
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<List<EmployeeShift>> getAllByEmployeeId (Long employeeId);
}
