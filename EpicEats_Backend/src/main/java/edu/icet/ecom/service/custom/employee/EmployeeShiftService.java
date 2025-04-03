package edu.icet.ecom.service.custom.employee;

import edu.icet.ecom.dto.employee.EmployeeShift;
import edu.icet.ecom.dto.employee.EmployeeShiftLite;
import edu.icet.ecom.service.FullDataService;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface EmployeeShiftService extends FullDataService<EmployeeShift> {
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<List<EmployeeShift>> getAllByEmployeeId (Long employeeId);
	Response<EmployeeShift> add (EmployeeShiftLite employeeShift);
	Response<EmployeeShift> update (EmployeeShiftLite employeeShift);
}
