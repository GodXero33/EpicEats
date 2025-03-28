package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.misc.customer.*;
import edu.icet.ecom.dto.misc.Customer;
import edu.icet.ecom.service.custom.misc.CustomerService;
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
@RequestMapping("/customer")
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {
	private final CustomerService customerService;
	private final ControllerResponseUtil controllerResponseUtil;

	@CustomerGetApiDoc
	@GetMapping("/{id}")
	public CustomHttpResponse<Customer> get (@PathVariable("id") Long id) {
		if (id <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Customer it can't be zero or negative");

		final Response<Customer> response = this.customerService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Customer found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Customer not found");
		};
	}

	@CustomerGetAllApiDoc
	@GetMapping("/all")
	public CustomHttpResponse<List<Customer>> getAll () {
		final Response<List<Customer>> response = this.customerService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), Customer.class, "All customers has retrieved successfully") :
			this.controllerResponseUtil.getServerErrorResponse(null);
	}

	@CustomerAddApiDoc
	@PostMapping("/")
	public CustomHttpResponse<Customer> add (@Valid @RequestBody Customer customer, BindingResult result) {
		if (result.hasErrors()) this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<Boolean> phoneExistResponse = this.customerService.isPhoneExist(customer.getPhone());

		if (phoneExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Customer phone number is already taken");
		if (phoneExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse(null);

		final Response<Boolean> emailExistResponse = this.customerService.isEmailExist(customer.getPhone());

		if (emailExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Customer email address is already taken");
		if (emailExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse(null);

		final Response<Customer> response = this.customerService.add(customer);

		return response.getStatus() == ResponseType.CREATED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Customer has added") :
			this.controllerResponseUtil.getServerErrorResponse(null);
	}

	@CustomerUpdateApiDoc
	@PutMapping("/")
	public CustomHttpResponse<Customer> update (@Valid @RequestBody Customer customer, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (customer.getId() == null || customer.getId() <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Customer id can't be null, zero or negative");

		final Response<Boolean> phoneExistResponse = this.customerService.isPhoneExist(customer.getPhone(), customer.getId());

		if (phoneExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Customer phone number is already taken");
		if (phoneExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse(null);

		final Response<Boolean> emailExistResponse = this.customerService.isEmailExist(customer.getPhone(), customer.getId());

		if (emailExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Customer email address is already taken");
		if (emailExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse(null);

		final Response<Customer> response = this.customerService.update(customer);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Customer updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update customer");
		};
	}

	@CustomerDeleteApiDoc
	@DeleteMapping("/{id}")
	public CustomHttpResponse<Boolean> delete (@PathVariable("id") Long id) {
		if (id <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Id can't be zero or negative");

		final Response<Boolean> response = this.customerService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, true, "Customer deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(false);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, false, "Failed to delete customer");
		};
	}
}
