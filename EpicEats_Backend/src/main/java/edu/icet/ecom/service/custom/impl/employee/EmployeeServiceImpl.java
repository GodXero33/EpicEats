package edu.icet.ecom.service.custom.impl.employee;

import edu.icet.ecom.dto.employee.Employee;
import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.repository.custom.employee.EmployeeRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.employee.EmployeeService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enums.ResponseType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class EmployeeServiceImpl implements EmployeeService {
	private final SuperServiceHandler<Employee, EmployeeEntity> serviceHandler;
	private final EmployeeRepository employeeRepository;
	private final ModelMapper mapper;

	public EmployeeServiceImpl (EmployeeRepository employeeRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(employeeRepository, mapper, Employee.class, EmployeeEntity.class);
		this.employeeRepository = employeeRepository;
		this.mapper = mapper;
	}

	@Override
	public Response<Employee> get (Long id) {
		return this.serviceHandler.get(id);
	}

	@Override
	public Response<List<Employee>> getAll () {
		return this.serviceHandler.getAll();
	}

	@Override
	public Response<Employee> add (Employee dto) {
		return this.serviceHandler.add(dto);
	}

	@Override
	public Response<Employee> update (Employee dto) {
		return this.serviceHandler.update(dto);
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.serviceHandler.delete(id);
	}

	@Override
	public Response<Object> terminate (Long employeeId) {
		return this.employeeRepository.terminate(employeeId);
	}

	@Override
	public Response<Boolean> isExist (Long employeeId) {
		return this.employeeRepository.isExist(employeeId);
	}

	@Override
	public Response<Boolean> isPhoneExist (String phone) {
		return this.employeeRepository.isPhoneExist(phone);
	}

	@Override
	public Response<Boolean> isPhoneExist (String phone, Long employeeId) {
		return this.employeeRepository.isPhoneExist(phone, employeeId);
	}

	@Override
	public Response<Boolean> isEmailExist (String email) {
		return this.employeeRepository.isEmailExist(email);
	}

	@Override
	public Response<Boolean> isEmailExist (String email, Long employeeId) {
		return this.employeeRepository.isEmailExist(email, employeeId);
	}

	@Override
	public Response<List<Employee>> filter (Employee employee) {
		final Response<List<EmployeeEntity>> response = this.employeeRepository.filter(this.mapper.map(employee, EmployeeEntity.class));

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			response.getData().stream().map(entity -> this.mapper.map(entity, Employee.class)).toList() :
			null
			, response.getStatus());
	}
}
