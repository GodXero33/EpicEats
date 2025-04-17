package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.order.OrderAddApiDoc;
import edu.icet.ecom.config.apidoc.order.OrderDeleteApiDoc;
import edu.icet.ecom.config.apidoc.order.OrderGetAllApiDoc;
import edu.icet.ecom.config.apidoc.order.OrderGetApiDoc;
import edu.icet.ecom.dto.order.*;
import edu.icet.ecom.service.custom.employee.EmployeeService;
import edu.icet.ecom.service.custom.merchandise.MenuItemService;
import edu.icet.ecom.service.custom.misc.CustomerService;
import edu.icet.ecom.service.custom.order.OrderService;
import edu.icet.ecom.util.ControllerResponseUtil;
import edu.icet.ecom.util.CustomHttpResponse;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enums.ResponseType;
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
@RequestMapping("/order")
@Tag(name = "Order Management", description = "APIs for managing orders and receipts")
public class OrderController {
	private final OrderService orderService;
	private final ControllerResponseUtil controllerResponseUtil;
	private final CustomerService customerService;
	private final EmployeeService employeeService;
	private final MenuItemService menuItemService;

	@OrderAddApiDoc
	@PostMapping("")
	public CustomHttpResponse<Order> add (@Valid @RequestBody OrderLite order, BindingResult result) {
		if (result.hasErrors()) this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<Boolean> customerExistResponse = this.customerService.isExist(order.getCustomerId());

		if (customerExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (customerExistResponse.getStatus() == ResponseType.NOT_FOUND) return new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "No customer found with customerId");

		final Response<Boolean> employeeExistResponse = this.employeeService.isExist(order.getEmployeeId());

		if (employeeExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (employeeExistResponse.getStatus() == ResponseType.NOT_FOUND) return new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "No employee found with employeeId");

		final List<Long> menuItemIDs = order.getOrderItems().stream().map(OrderItem::getItemId).toList();

		if (employeeExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (employeeExistResponse.getStatus() == ResponseType.NOT_FOUND) return new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "No employee found with employeeId");

		final Response<Boolean> menuItemsExistResponse = this.menuItemService.isAllMenuItemsExist(menuItemIDs);

		if (menuItemsExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (menuItemsExistResponse.getStatus() == ResponseType.NOT_FOUND) return new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "There is an order item that has menu item id that not found in server");

		final Response<SuperOrder> response = this.orderService.add(order);

		return switch (response.getStatus()) {
			case CREATED -> new CustomHttpResponse<>(HttpStatus.OK, (Order) response.getData(), "Order has placed successfully");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to place the order");
		};
	}

	@OrderGetApiDoc
	@GetMapping("/{id}")
	public CustomHttpResponse<Order> get (@PathVariable("id") Long id) {
		if (id <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Order id can't be zero or negative");

		final Response<SuperOrder> response = this.orderService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, (Order) response.getData(), "Order has found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Order not found");
		};
	}

	@OrderGetAllApiDoc
	@GetMapping("/all")
	public CustomHttpResponse<AllOrders> getAll () {
		final Response<SuperOrder> response = this.orderService.getAllStructured();

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, (AllOrders) response.getData(), "All order has found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Orders not found");
		};
	}

	@OrderDeleteApiDoc
	@DeleteMapping("/{id}")
	public CustomHttpResponse<Object> delete (@PathVariable("id") Long id) {
		if (id <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Id can't be zero or negative");

		final Response<Object> response = this.orderService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Order deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Order delete failed");
		};
	}
}
