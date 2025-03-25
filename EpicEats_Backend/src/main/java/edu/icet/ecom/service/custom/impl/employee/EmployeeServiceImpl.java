package edu.icet.ecom.service.custom.impl.employee;

import edu.icet.ecom.dto.employee.Employee;
import edu.icet.ecom.repository.custom.employee.EmployeeRepository;
import edu.icet.ecom.service.custom.employee.EmployeeService;
import edu.icet.ecom.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
	private final EmployeeRepository employeeRepository;

	@Override
	public Response<Employee> get (Long id) {
		return null;
	}

	@Override
	public Response<List<Employee>> getAll () {
		return null;
	}

	@Override
	public Response<Employee> add (Employee dto) {
		return null;
	}

	@Override
	public Response<Employee> update (Employee dto) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<Boolean> terminate (Long employeeId) {
		return this.employeeRepository.terminate(employeeId);
	}
}
