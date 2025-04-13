package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.restaurant.*;
import edu.icet.ecom.dto.restaurant.*;
import edu.icet.ecom.service.custom.misc.CustomerService;
import edu.icet.ecom.service.custom.restaurant.RestaurantTableService;
import edu.icet.ecom.util.Constants;
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
@RequestMapping("/restaurant")
@Tag(name = "Restaurant Management", description = "APIs for managing restaurant")
public class RestaurantController {
	private final RestaurantTableService restaurantTableService;
	private final CustomerService customerService;
	private final ControllerResponseUtil controllerResponseUtil;

	private <T> CustomHttpResponse<T> getInvalidIdResponse () {
		return this.controllerResponseUtil.getInvalidDetailsResponse("Id can't be negative or zero");
	}

	@PostMapping("/table")
	@RestaurantTableAddApiDoc
	public CustomHttpResponse<RestaurantTable> addTable (@Valid @RequestBody RestaurantTable restaurantTable, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<Boolean> tableNumberExistResponse = this.restaurantTableService.isTableNumberExist(restaurantTable.getTableNumber());

		if (tableNumberExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (tableNumberExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "There is a table with same given table number");

		final Response<RestaurantTable> response = this.restaurantTableService.add(restaurantTable);

		return response.getStatus() == ResponseType.CREATED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Restaurant table added") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@PutMapping("/table")
	@RestaurantTableUpdateApiDoc
	public CustomHttpResponse<RestaurantTable> updateTable (@Valid @RequestBody RestaurantTable restaurantTable, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (restaurantTable.getId() == null || restaurantTable.getId() <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Table id can't be null or negative or zero");

		final Response<Boolean> tableNumberExistResponse = this.restaurantTableService.isTableNumberExist(restaurantTable.getTableNumber(), restaurantTable.getId());

		if (tableNumberExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (tableNumberExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "There is a table with same given table number");

		final Response<RestaurantTable> response = this.restaurantTableService.update(restaurantTable);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Restaurant table updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update restaurant table");
		};
	}

	@RestaurantTableGetApiDoc
	@GetMapping("/table/{id}")
	public CustomHttpResponse<RestaurantTable> getTable (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<RestaurantTable> response = this.restaurantTableService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Restaurant table has found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "No restaurant table has found");
		};
	}

	@GetMapping("/table/all")
	@RestaurantTableGetAllApiDoc
	public CustomHttpResponse<List<RestaurantTable>> getALlTables () {
		final Response<List<RestaurantTable>> response = this.restaurantTableService.getAll();

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "all restaurant tables were found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Failed to get all restaurant tables");
		};
	}

	@RestaurantTableDeleteApiDoc
	@DeleteMapping("/table/{id}")
	public CustomHttpResponse<Object> deleteTable (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.restaurantTableService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Restaurant table deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete restaurant table");
		};
	}

	private CustomHttpResponse<RestaurantTableBooking> getTableBookingInvalidResponse (RestaurantTableBookingLite bookingLite, boolean isUpdate) {
		final Response<Boolean> customerExistResponse = this.customerService.isExist(bookingLite.getCustomerId());

		if (customerExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (customerExistResponse.getStatus() == ResponseType.NOT_FOUND) return new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "No customer found with given customer id");

		final Response<Boolean> tableExistResponse = this.restaurantTableService.isTableExist(bookingLite.getTableId());

		if (tableExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (tableExistResponse.getStatus() == ResponseType.NOT_FOUND) return new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "No table found with given table id");

		final Response<Boolean> tableAvailableResponse = this.restaurantTableService.isTableAvailable(bookingLite.getTableId());

		if (tableAvailableResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (tableAvailableResponse.getStatus() == ResponseType.NOT_FOUND) return new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "The target table is not available");

		if (bookingLite.getStartTime().isAfter(bookingLite.getEndTime())) return new CustomHttpResponse<>(HttpStatus.BAD_REQUEST, null, "Booking start time must be before time than end time");

		if (bookingLite.getStartTime().plusMinutes(Constants.MINIMUM_BOOKING_DURATION_MINUTES).isAfter(bookingLite.getEndTime())) return new CustomHttpResponse<>(HttpStatus.BAD_REQUEST, null, "A booking must be at least %d minutes long.".formatted(Constants.MINIMUM_BOOKING_DURATION_MINUTES));

		final Response<Boolean> timeSlotOverlapsResponse = this.restaurantTableService.isTableBookingOverlaps(bookingLite, isUpdate);

		if (timeSlotOverlapsResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();
		if (timeSlotOverlapsResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.BAD_REQUEST, null, "The given booking slot is overlap with another booking for same table");

		return null;
	}

	@PostMapping("/table/booking")
	@RestaurantTableBookingAddApiDoc
	public CustomHttpResponse<RestaurantTableBooking> addBooking (@Valid @RequestBody RestaurantTableBookingLite bookingLite, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final CustomHttpResponse<RestaurantTableBooking> invalidInvalidTableBookingTimeResponse = this.getTableBookingInvalidResponse(bookingLite, false);

		if (invalidInvalidTableBookingTimeResponse != null) return invalidInvalidTableBookingTimeResponse;

		final Response<RestaurantTableBooking> response = this.restaurantTableService.addBooking(bookingLite);

		return switch (response.getStatus()) {
			case CREATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Booking added");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to add booking");
		};
	}

	@PutMapping("/table/booking")
	@RestaurantTableBookingUpdateApiDoc
	public CustomHttpResponse<RestaurantTableBooking> updateBooking (@Valid @RequestBody RestaurantTableBookingLite bookingLite, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (bookingLite.getId() == null || bookingLite.getId() <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("bookingId can't be null or negative or zero");

		final CustomHttpResponse<RestaurantTableBooking> invalidInvalidTableBookingTimeResponse = this.getTableBookingInvalidResponse(bookingLite, true);

		if (invalidInvalidTableBookingTimeResponse != null) return invalidInvalidTableBookingTimeResponse;

		final Response<RestaurantTableBooking> response = this.restaurantTableService.updateBooking(bookingLite);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Booking updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update booking");
		};
	}

	@RestaurantTableBookingGetApiDoc
	@GetMapping("/table/booking/{id}")
	public CustomHttpResponse<RestaurantTableBooking> getBooking (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<RestaurantTableBooking> response = this.restaurantTableService.getBooking(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Booking retrieved successfully");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Booking not found");
		};
	}

	@GetMapping("/table/booking/all")
	@RestaurantTableBookingGetAllApiDoc
	public CustomHttpResponse<AllRestaurantTableBookings> getAllBookings () {
		final Response<AllRestaurantTableBookings> response = this.restaurantTableService.getAllBookings();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All bookings are loaded") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@RestaurantTableBookingGetByTableApiDoc
	@GetMapping("/table/booking/by-table/{tableId}")
	public CustomHttpResponse<RestaurantBookingsByTable> getAllBookingByTable (@PathVariable("tableId") Long tableId) {
		if (tableId <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Id can't be zero or negative");

		final Response<RestaurantBookingsByTable> response = this.restaurantTableService.getAllBookingsByTableId(tableId);

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All bookings are loaded") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@RestaurantTableBookingDeleteApiDoc
	@DeleteMapping("/table/booking/{id}")
	public CustomHttpResponse<Object> deleteBooking (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.restaurantTableService.deleteBooking(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Booking deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Booking not deleted");
		};
	}

	@RestaurantTableBookingDeleteAllByTableApiDoc
	@DeleteMapping("/table/booking/by-table/{tableId}")
	public CustomHttpResponse<Object> deleteBookingByTable (@PathVariable("tableId") Long tableId) {
		if (tableId <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Table Id must non zero positive big-int for delete booking");

		final Response<Object> response = this.restaurantTableService.deleteAllBookingsByTableId(tableId);

		return response.getStatus() == ResponseType.DELETED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All booking deleted related table") :
			this.controllerResponseUtil.getServerErrorResponse();
	}
}
