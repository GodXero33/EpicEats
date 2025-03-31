package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.finance.*;
import edu.icet.ecom.dto.finance.Expense;
import edu.icet.ecom.dto.finance.Report;
import edu.icet.ecom.service.custom.employee.EmployeeService;
import edu.icet.ecom.service.custom.finance.ExpenseService;
import edu.icet.ecom.service.custom.finance.ReportService;
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
@RequestMapping("/finance")
@Tag(name = "Finance Management", description = "APIs for managing finance")
public class FinanceController {
	private final ExpenseService expenseService;
	private final ReportService reportService;
	private final EmployeeService employeeService;
	private final ControllerResponseUtil controllerResponseUtil;

	private <T> CustomHttpResponse<T> getInvalidIdResponse () {
		return this.controllerResponseUtil.getInvalidDetailsResponse("Id can't be negative or zero.");
	}

	private <T> CustomHttpResponse<T> getEmployeeNotFoundResponse () {
		return this.controllerResponseUtil.getInvalidDetailsResponse("No employee found with given employee id");
	}

	@ExpenseGetApiDoc
	@GetMapping("/expense/{id}")
	public CustomHttpResponse<Expense> getExpense (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Expense> response = this.expenseService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Expense record found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Failed to find expense record with given id");
		};
	}

	@ExpenseGetAllApiDoc
	@GetMapping("/expense/all")
	public CustomHttpResponse<List<Expense>> getAllExpenses () {
		final Response<List<Expense>> response = this.expenseService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All expenses are retrieved successfully") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@ExpenseAddApiDoc
	@PostMapping("/expense/")
	public CustomHttpResponse<Expense> addExpense (@Valid @RequestBody Expense expense, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<Expense> response = this.expenseService.add(expense);

		return response.getStatus() == ResponseType.CREATED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Expense record created") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@ExpenseUpdateApiDoc
	@PutMapping("/expense/")
	public CustomHttpResponse<Expense> updateExpense (@Valid @RequestBody Expense expense, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<Expense> response = this.expenseService.update(expense);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Expense record updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update expense record");
		};
	}

	@ExpenseDeleteApiDoc
	@DeleteMapping("/expense/{id}")
	public CustomHttpResponse<Object> deleteExpense (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.expenseService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Expense record deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Falied to delete expense record");
		};
	}

	@ReportGetApiDoc
	@GetMapping("/report/{id}")
	public CustomHttpResponse<Report> getReport (@PathVariable("id") Long id, @RequestParam(name = "full", defaultValue = "true") boolean isFull) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Report> response = isFull ? this.reportService.getFull(id) : this.reportService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Report found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Failed to find report with given id");
		};
	}

	@ReportGetAllApiDoc
	@GetMapping("/report/all")
	public CustomHttpResponse<List<Report>> getAllReports (@RequestParam(name = "full", defaultValue = "true") boolean isFull) {
		final Response<List<Report>> response = isFull ? this.reportService.getAllFull() : this.reportService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All reports are retrieved successfully") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@ReportGetAllByEmployeeApiDoc
	@GetMapping("/report/by-employee/{employeeId}")
	public CustomHttpResponse<List<Report>> getAllByEmployee (@PathVariable("employeeId") Long employeeId) {
		if (employeeId <= 0) return this.getInvalidIdResponse();

		final Response<Boolean> employeeExistResponse = this.employeeService.isExist(employeeId);

		if (employeeExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getEmployeeNotFoundResponse();
		if (employeeExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<List<Report>> response = this.reportService.getAllByEmployeeId(employeeId);

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All reports are retrieved successfully created by target employee") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	private CustomHttpResponse<Report> getResponseAfterReportValidation (Report report) {
		final long employeeId = report.getGeneratedBy().getId();

		if (employeeId <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Employee id can't be negative or zero");

		final Response<Boolean> employeeExistResponse = this.employeeService.isExist(employeeId);

		if (employeeExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getEmployeeNotFoundResponse();
		if (employeeExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		return null;
	}

	@ReportAddApiDoc
	@PostMapping("/report/")
	public CustomHttpResponse<Report> addReport (@Valid @RequestBody Report report, BindingResult result) {
		if (result.hasErrors()) this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final CustomHttpResponse<Report> responseAfterReportValidation = this.getResponseAfterReportValidation(report);

		if (responseAfterReportValidation != null) return responseAfterReportValidation;

		final Response<Report> response = this.reportService.add(report);

		return response.getStatus() == ResponseType.CREATED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Report added successfully") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@ReportUpdateApiDoc
	@PutMapping("/report/")
	public CustomHttpResponse<Report> updateReport (@Valid @RequestBody Report report, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final CustomHttpResponse<Report> responseAfterReportValidation = this.getResponseAfterReportValidation(report);

		if (responseAfterReportValidation != null) return responseAfterReportValidation;

		final Response<Report> response = this.reportService.update(report);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Report has updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update report");
		};
	}

	@ReportDeleteApiDoc
	@DeleteMapping("/report/{id}")
	public CustomHttpResponse<Object> deleteReport (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.reportService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Report deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete report");
		};
	}

	@ReportDeleteAllByEmployeeApiDoc
	@DeleteMapping("/report/by-employee/{employeeId}")
	public CustomHttpResponse<Object> deleteAllByEmployee (@PathVariable("employeeId") Long employeeId) {
		if (employeeId <= 0) return this.getInvalidIdResponse();
		final Response<Boolean> employeeExistResponse = this.employeeService.isExist(employeeId);

		if (employeeExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getEmployeeNotFoundResponse();
		if (employeeExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<Object> response = this.reportService.deleteByEmployeeId(employeeId);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Reports deleted created by target employee");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete reports of target " +
				"employee");
		};
	}
}
