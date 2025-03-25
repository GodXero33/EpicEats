package edu.icet.ecom.controller;

import edu.icet.ecom.dto.employee.Employee;
import edu.icet.ecom.service.custom.employee.EmployeeService;
import edu.icet.ecom.util.ControllerResponseUtil;
import edu.icet.ecom.util.CustomHttpResponse;
import edu.icet.ecom.util.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {
	private final EmployeeService employeeService;
	private final ControllerResponseUtil controllerResponseUtil;

	@GetMapping("/get/{id}")
	public CustomHttpResponse<Employee> get (@PathVariable("id") Long id) {
		if (id <= 0) return new CustomHttpResponse<>(HttpStatus.BAD_REQUEST, null, "Id can't be negative or zero.");

		final Response<Employee> response = this.employeeService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Employee found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Employee not found");
		};
	}

	@PatchMapping("/terminate/{id}")
	public CustomHttpResponse<Boolean> terminate (@PathVariable("id") Long employeeId) {
		if (employeeId <= 0) return new CustomHttpResponse<>(HttpStatus.BAD_REQUEST, null, "Id can't be negative or zero.");

		final Response<Boolean> response = this.employeeService.terminate(employeeId);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, true, "Un employed success");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(false);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, false, "Failed to un employ employee");
		};
	}
}
