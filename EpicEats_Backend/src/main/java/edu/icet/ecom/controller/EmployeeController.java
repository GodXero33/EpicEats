package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.employee.*;
import edu.icet.ecom.dto.employee.*;
import edu.icet.ecom.service.custom.employee.EmployeeService;
import edu.icet.ecom.service.custom.employee.EmployeeShiftService;
import edu.icet.ecom.service.custom.employee.PromotionHistoryService;
import edu.icet.ecom.util.ControllerResponseUtil;
import edu.icet.ecom.util.CustomHttpResponse;
import edu.icet.ecom.util.CustomHttpResponseMap;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {
	private final EmployeeService employeeService;
	private final EmployeeShiftService employeeShiftService;
	private final PromotionHistoryService promotionHistoryService;
	private final ControllerResponseUtil controllerResponseUtil;

	private <T> CustomHttpResponse<T> getInvalidIdResponse () {
		return this.controllerResponseUtil.getInvalidDetailsResponse("Id can't be negative or zero.");
	}

	@EmployeeGetApiDoc
	@GetMapping("/{id}")
	public CustomHttpResponse<Employee> get (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Employee> response = this.employeeService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Employee found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Employee not found");
		};
	}

	@EmployeeGetAllApiDoc
	@GetMapping("/all")
	public CustomHttpResponse<List<Employee>> getAll () {
		final Response<List<Employee>> response = this.employeeService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All employees found") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@EmployeeAddApiDoc
	@PostMapping("/")
	public CustomHttpResponse<Employee> add (@Valid @RequestBody Employee employee, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<Boolean> phoneExistResponse = this.employeeService.isPhoneExist(employee.getPhone());

		if (phoneExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (phoneExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Phone number is already taken");

		final Response<Boolean> emailExistResponse = this.employeeService.isEmailExist(employee.getEmail());

		if (emailExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (emailExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Email address is already taken");

		final Response<Employee> response = this.employeeService.add(employee);

		return response.getStatus() == ResponseType.CREATED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Employee added successfully") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@EmployeeUpdateApiDoc
	@PutMapping("/")
	public CustomHttpResponse<Employee> update (@Valid @RequestBody Employee employee, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (employee.getId() == null || employee.getId() <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Employee id can't be null, zero or negative");

		final Response<Boolean> phoneExistResponse = this.employeeService.isPhoneExist(employee.getPhone(), employee.getId());

		if (phoneExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (phoneExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Phone number is already taken");

		final Response<Boolean> emailExistResponse = this.employeeService.isEmailExist(employee.getEmail(), employee.getId());

		if (emailExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (emailExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Email address is already taken");

		final Response<Employee> response = this.employeeService.update(employee);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Employee updated successfully");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update employee");
		};
	}

	@EmployeeTerminateApiDoc
	@PatchMapping("/terminate/{id}")
	public CustomHttpResponse<Boolean> terminate (@PathVariable("id") Long employeeId) {
		if (employeeId <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.employeeService.terminate(employeeId);

		return switch (response.getStatus()) {
			case SUCCESS -> new CustomHttpResponse<>(HttpStatus.OK, null, "Terminate employee success");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to terminate employee");
		};
	}

	@EmployeeShiftGetApiDoc
	@GetMapping("/shift/{id}")
	public CustomHttpResponse<EmployeeShift> getShift (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<EmployeeShift> response = this.employeeShiftService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Employee shift found.");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Employee shift not found");
		};
	}

	@EmployeeShiftGetAllApiDoc
	@GetMapping("/shift/all")
	public CustomHttpResponse<AllShifts> getAllShifts () {
		final Response<AllShifts> response = this.employeeShiftService.getAllStructured();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All shifts are retrieved successfully") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@EmployeeShiftGetByEmployeeApiDoc
	@GetMapping("/shift/by-employee/{employeeId}")
	public CustomHttpResponse<Map<String, Object>> getShiftsByEmployee (@PathVariable("employeeId") Long employeeId) {
		if (employeeId <= 0) return this.getInvalidIdResponse();

		final Response<Employee> employeeGetResponse = this.employeeService.get(employeeId);

		if (employeeGetResponse.getStatus() == ResponseType.NOT_FOUND) return new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "No employee found with employeeId");
		if (employeeGetResponse.getStatus() == ResponseType.SERVER_ERROR) this.controllerResponseUtil.getServerErrorResponse();

		final Response<List<EmployeeShift>> response = this.employeeShiftService.getAllByEmployeeId(employeeId);

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, CustomHttpResponseMap.builder()
				.keys("shifts", "employee")
				.values(response.getData(), employeeGetResponse.getData())
				.build(), "Employee shifts found") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	private CustomHttpResponse<EmployeeShift> getResponseAfterEmployeeShiftValidation (EmployeeShiftLite employeeShift) {
		final long employeeId = employeeShift.getEmployeeId();

		if (employeeId <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Employee id can't be negative or zero");

		final Response<Boolean> employeeExistResponse = this.employeeService.isExist(employeeId);

		if (employeeExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.controllerResponseUtil.getInvalidDetailsResponse("No employee found with given employeeId");
		if (employeeExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		return null;
	}

	@EmployeeShiftAddApiDoc
	@PostMapping("/shift/")
	public CustomHttpResponse<EmployeeShift> addShift (@Valid @RequestBody EmployeeShiftLite employeeShift, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final CustomHttpResponse<EmployeeShift> responseAfterEmployeeShiftValidation = this.getResponseAfterEmployeeShiftValidation(employeeShift);

		if (responseAfterEmployeeShiftValidation != null) return responseAfterEmployeeShiftValidation;

		final Response<EmployeeShift> response = this.employeeShiftService.add(employeeShift);

		return response.getStatus() == ResponseType.CREATED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Employee shift has added") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@EmployeeShiftUpdateApiDoc
	@PutMapping("/shift/")
	public CustomHttpResponse<EmployeeShift> updateShift (@Valid @RequestBody EmployeeShiftLite employeeShift, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (employeeShift.getId() == null || employeeShift.getId() <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Shift id can't be null, zero or negative");

		final CustomHttpResponse<EmployeeShift> responseAfterEmployeeShiftValidation = this.getResponseAfterEmployeeShiftValidation(employeeShift);

		if (responseAfterEmployeeShiftValidation != null) return responseAfterEmployeeShiftValidation;

		final Response<EmployeeShift> response = this.employeeShiftService.update(employeeShift);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Employee shift updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update employee shift");
		};
	}

	@EmployeeShiftDeleteApiDoc
	@DeleteMapping("/shift/{id}")
	public CustomHttpResponse<Object> deleteShift (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.employeeShiftService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Employee shift deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete shift");
		};
	}

	@EmployeeShiftDeleteByEmployeeApiDoc
	@DeleteMapping("/shift/by-employee/{employeeId}")
	public CustomHttpResponse<Object> deleteShiftsByEmployee (@PathVariable("employeeId") Long employeeId) {
		if (employeeId <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.employeeShiftService.deleteByEmployeeId(employeeId);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Employee shifts deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete shifts");
		};
	}

	@PromotionHistoryGetApiDoc
	@GetMapping("/promotion/{id}")
	public CustomHttpResponse<PromotionHistory> getPromotion (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<PromotionHistory> response = this.promotionHistoryService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Promotion record found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Failed to find promotion record with given id");
		};
	}

	@PromotionHistoryGetAllApiDoc
	@GetMapping("/promotion/all")
	public CustomHttpResponse<List<PromotionHistory>> getAllPromotions () {
		final Response<List<PromotionHistory>> response = this.promotionHistoryService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All promotion history loaded") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@PromotionHistoryGetByEmployeeApiDoc
	@GetMapping("/promotion/by-employee/{employeeId}")
	public CustomHttpResponse<List<PromotionHistory>> getAllPromotionsByEmployee (@PathVariable("employeeId") Long employeeId) {
		if (employeeId <= 0) return this.getInvalidIdResponse();

		final Response<List<PromotionHistory>> response = this.promotionHistoryService.getAllByEmployeeId(employeeId);

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All promotion history loaded for employee") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@PromotionHistoryUpdateApiShift
	@PutMapping("/promotion/")
	public CustomHttpResponse<PromotionHistory> updatePromotion (@Valid @RequestBody PromotionHistoryLite promotionHistory, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (promotionHistory.getId() == null || promotionHistory.getId() <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Promotion history id can't be null, zero or negative");
		if (promotionHistory.getEmployeeId() <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Promotion history employee id can't be null, zero or negative");

		final Response<Boolean> employeeExistResponse = this.employeeService.isExist(promotionHistory.getEmployeeId());

		if (employeeExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.controllerResponseUtil.getInvalidDetailsResponse("No employee found with given employee id");
		if (employeeExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<PromotionHistory> response = this.promotionHistoryService.update(promotionHistory);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Promotion history updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update promotion history");
		};
	}

	@PromotionHistoryDeleteApiDoc
	@DeleteMapping("/promotion/{id}")
	public CustomHttpResponse<Object> deletePromotion (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.promotionHistoryService.delete(id);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Promotion history deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete promotion history");
		};
	}

	@PromotionHistoryDeleteByEmployeeApiDoc
	@DeleteMapping("/promotion/by-employee/{employeeId}")
	public CustomHttpResponse<Object> deletePromotionsByEmployee (@PathVariable("employeeId") Long employeeId) {
		if (employeeId <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.promotionHistoryService.deleteByEmployeeId(employeeId);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Promotion histories deleted related to " +
				"target employee");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete promotion histories");
		};
	}
}
