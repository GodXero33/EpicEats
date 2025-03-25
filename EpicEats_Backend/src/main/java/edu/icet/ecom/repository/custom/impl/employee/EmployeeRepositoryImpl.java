package edu.icet.ecom.repository.custom.impl.employee;

import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.repository.custom.employee.EmployeeRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {
	@Override
	public Response<EmployeeEntity> add (EmployeeEntity entity) {
		return null;
	}

	@Override
	public Response<EmployeeEntity> update (EmployeeEntity entity) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<EmployeeEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<EmployeeEntity>> getAll () {
		return null;
	}
}
