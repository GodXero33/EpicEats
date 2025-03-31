package edu.icet.ecom.service.custom.impl.employee;

import edu.icet.ecom.dto.employee.EmployeeShift;
import edu.icet.ecom.entity.employee.EmployeeShiftEntity;
import edu.icet.ecom.repository.custom.employee.EmployeeShiftRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.employee.EmployeeShiftService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class EmployeeShiftServiceImpl implements EmployeeShiftService {
	private final SuperServiceHandler<EmployeeShift, EmployeeShiftEntity> serviceHandler;
	private final EmployeeShiftRepository employeeShiftRepository;
	private final ModelMapper mapper;

	public EmployeeShiftServiceImpl (EmployeeShiftRepository employeeShiftRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(employeeShiftRepository, mapper, EmployeeShift.class, EmployeeShiftEntity.class);
		this.employeeShiftRepository = employeeShiftRepository;
		this.mapper = mapper;
	}

	@Override
	public Response<EmployeeShift> get (Long id) {
		return this.serviceHandler.get(id);
	}

	@Override
	public Response<List<EmployeeShift>> getAll () {
		return this.serviceHandler.getAll();
	}

	@Override
	public Response<EmployeeShift> add (EmployeeShift dto) {
		return this.serviceHandler.add(dto);
	}

	@Override
	public Response<EmployeeShift> update (EmployeeShift dto) {
		return this.serviceHandler.update(dto);
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.serviceHandler.delete(id);
	}

	@Override
	public Response<EmployeeShift> getFull (Long id) {
		final Response<EmployeeShiftEntity> response = this.employeeShiftRepository.getFull(id);

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			this.mapper.map(response.getData(), EmployeeShift.class) :
			null
			, response.getStatus());
	}

	@Override
	public Response<List<EmployeeShift>> getAllFull () {
		final Response<List<EmployeeShiftEntity>> response = this.employeeShiftRepository.getAllFull();

		return response.getStatus() == ResponseType.FOUND ?
			new Response<>(response.getData().stream().map(employeeShiftEntity -> this.mapper.map(employeeShiftEntity, EmployeeShift.class)).toList(), response.getStatus()) :
			new Response<>(null, response.getStatus());
	}

	@Override
	public Response<Object> deleteByEmployeeId (Long employeeId) {
		return this.employeeShiftRepository.deletedByEmployeeId(employeeId);
	}

	@Override
	public Response<List<EmployeeShift>> getAllByEmployeeId (Long employeeId) {
		final Response<List<EmployeeShiftEntity>> response = this.employeeShiftRepository.getAllByEmployeeId(employeeId);

		return response.getStatus() == ResponseType.FOUND ?
			new Response<>(response.getData().stream().map(employeeShiftEntity -> this.mapper.map(employeeShiftEntity, EmployeeShift.class)).toList(), response.getStatus()) :
			new Response<>(null, response.getStatus());
	}
}
