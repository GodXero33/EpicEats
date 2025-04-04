package edu.icet.ecom.repository.custom.employee;

import edu.icet.ecom.entity.employee.AllShiftsEntity;
import edu.icet.ecom.entity.employee.EmployeeShiftEntity;
import edu.icet.ecom.entity.employee.EmployeeShiftLiteEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface EmployeeShiftRepository extends CrudRepository<EmployeeShiftEntity> {
	Response<List<EmployeeShiftEntity>> getAllByEmployeeId (Long employeeId);
	Response<Object> deletedByEmployeeId (Long employeeId);
	Response<EmployeeShiftEntity> add (EmployeeShiftLiteEntity entity);
	Response<EmployeeShiftEntity> update (EmployeeShiftLiteEntity entity);
	Response<AllShiftsEntity> getAllStructured ();
}
