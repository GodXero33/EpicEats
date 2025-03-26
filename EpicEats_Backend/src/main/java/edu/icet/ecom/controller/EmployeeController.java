package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.employee.*;
import edu.icet.ecom.dto.employee.Employee;
import edu.icet.ecom.dto.employee.EmployeeShift;
import edu.icet.ecom.service.custom.employee.EmployeeService;
import edu.icet.ecom.service.custom.employee.EmployeeShiftService;
import edu.icet.ecom.util.ControllerResponseUtil;
import edu.icet.ecom.util.CustomHttpResponse;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {
	private final EmployeeService employeeService;
	private final EmployeeShiftService employeeShiftService;
	private final ControllerResponseUtil controllerResponseUtil;

	private <T> CustomHttpResponse<T> getInvalidUserDetailsResponse () {
		return this.controllerResponseUtil.getInvalidUserDetailsResponse("Id can't be negative or zero.");
	}

	@EmployeeGetApiDoc
	@GetMapping("/get/{id}")
	public CustomHttpResponse<Employee> get (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidUserDetailsResponse();

		final Response<Employee> response = this.employeeService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Employee found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Employee not found");
		};
	}

	@EmployeeGetAllApiDoc
	@GetMapping("/get-all")
	public CustomHttpResponse<List<Employee>> getAll () {
		final Response<List<Employee>> response = this.employeeService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), Employee.class, "All employees found") :
			new CustomHttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, "Internal server error");
	}

	@EmployeeAddApiDoc
	@PostMapping("/add")
	public CustomHttpResponse<Employee> add (@Valid @RequestBody Employee employee, BindingResult result) {
		if (result.hasErrors()) this.controllerResponseUtil.getInvalidUserDetailsResponse(result);

		final Response<Employee> response = this.employeeService.add(employee);

		return response.getStatus() == ResponseType.CREATED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Employee added successfully") :
			new CustomHttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, "Failed to add employee");
	}

	@EmployeeUpdateApiDoc
	@PutMapping("/update")
	public CustomHttpResponse<Employee> update (@Valid @RequestBody Employee employee, BindingResult result) {
		if (result.hasErrors()) this.controllerResponseUtil.getInvalidUserDetailsResponse(result);

		final Response<Employee> response = this.employeeService.update(employee);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Employee updated successfully");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update employee");
		};
	}

	@EmployeeTerminateApiDoc
	@PatchMapping("/terminate/{id}")
	public CustomHttpResponse<Boolean> terminate (@PathVariable("id") Long employeeId) {
		if (employeeId <= 0) return this.getInvalidUserDetailsResponse();

		final Response<Boolean> response = this.employeeService.terminate(employeeId);

		return switch (response.getStatus()) {
			case SUCCESS -> new CustomHttpResponse<>(HttpStatus.OK, true, "Terminate employee success");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(false);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, false, "Failed to terminate employee");
		};
	}

	@EmployeeShiftGetApiDoc
	@GetMapping("/shift/get/{id}")
	public CustomHttpResponse<EmployeeShift> getShift (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidUserDetailsResponse();

		final Response<EmployeeShift> response = this.employeeShiftService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Employee shift found.");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Employee shift not found");
		};
	}

	@EmployeeShiftGetAllApiDoc
	@GetMapping("/shift/get-all")
	public CustomHttpResponse<List<EmployeeShift>> getAllShifts () {
		final Response<List<EmployeeShift>> response = this.employeeShiftService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), EmployeeShift.class, "Employee shifts found") :
			this.controllerResponseUtil.getServerErrorResponse(null);
	}

	@EmployeeShiftGetByEmployeeApiDoc
	@GetMapping("/shift/get-by-employee/{employee_id}")
	public CustomHttpResponse<List<EmployeeShift>> getShiftsByEmployee (@PathVariable("employee_id") Long employeeId) {
		if (employeeId <= 0) return this.getInvalidUserDetailsResponse();

		final Response<List<EmployeeShift>> response = this.employeeShiftService.getAllByEmployeeId(employeeId);

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), EmployeeShift.class, "Employee shifts found") :
			this.controllerResponseUtil.getServerErrorResponse(null);
	}
}
