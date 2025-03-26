package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.finance.*;
import edu.icet.ecom.dto.finance.Expense;
import edu.icet.ecom.dto.finance.Report;
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
	private final ControllerResponseUtil controllerResponseUtil;

	private <T> CustomHttpResponse<T> getInvalidIdResponse () {
		return this.controllerResponseUtil.getInvalidDetailsResponse("Id can't be negative or zero.");
	}

	@ExpenseGetApiDoc
	@GetMapping("/expense/get/{id}")
	public CustomHttpResponse<Expense> getExpense (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Expense> response = this.expenseService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Expense record found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Failed to find expense record with given id");
		};
	}

	@ExpenseGetAllApiDoc
	@GetMapping("/expense/get-all")
	public CustomHttpResponse<List<Expense>> getAllExpenses () {
		final Response<List<Expense>> response = this.expenseService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All expenses are retrieved successfully") :
			this.controllerResponseUtil.getServerErrorResponse(null);
	}

	@ExpenseAddApiDoc
	@PostMapping("/expense/add")
	public CustomHttpResponse<Expense> addExpense (@Valid @RequestBody Expense expense, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<Expense> response = this.expenseService.add(expense);

		return response.getStatus() == ResponseType.CREATED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Expense record created") :
			this.controllerResponseUtil.getServerErrorResponse(null);
	}

	@ExpenseUpdateApiDoc
	@PutMapping("/expense/update")
	public CustomHttpResponse<Expense> updateExpense (@Valid @RequestBody Expense expense, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<Expense> response = this.expenseService.update(expense);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Expense record updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update expense record");
		};
	}

	@ExpenseDeleteApiDoc
	@DeleteMapping("/expense/delete/{id}")
	public CustomHttpResponse<Boolean> deleteExpense (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Boolean> response = this.expenseService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, true, "Expense record deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(false);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, false, "Falied to delete expense record");
		};
	}

	@ReportGetApiDoc
	@GetMapping("/report/get/{id}")
	public CustomHttpResponse<Report> getReport (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Report> response = this.reportService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Report found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Failed to find report with given id");
		};
	}
}
