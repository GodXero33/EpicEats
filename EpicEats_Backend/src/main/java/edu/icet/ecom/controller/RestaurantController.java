package edu.icet.ecom.controller;

import edu.icet.ecom.dto.restaurant.RestaurantTable;
import edu.icet.ecom.service.custom.restaurant.RestaurantTableService;
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
	private final ControllerResponseUtil controllerResponseUtil;

	@PostMapping("/table")
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

	@GetMapping("/table/{id}")
	public CustomHttpResponse<RestaurantTable> getTable (@PathVariable("id") Long id) {
		if (id <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Id can't be negative or zero");

		final Response<RestaurantTable> response = this.restaurantTableService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Restaurant table has found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "No restaurant table has found");
		};
	}

	@GetMapping("/table/all")
	public CustomHttpResponse<List<RestaurantTable>> getALlTables () {
		final Response<List<RestaurantTable>> response = this.restaurantTableService.getAll();

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "all restaurant tables were found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Failed to get all restaurant tables");
		};
	}

	@DeleteMapping("/table/{id}")
	public CustomHttpResponse<Object> deleteTable (@PathVariable("id") Long id) {
		if (id <= 0) return this.controllerResponseUtil.getInvalidDetailsResponse("Id can't be zero or negative");

		final Response<Object> response = this.restaurantTableService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Restaurant table deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete restaurant table");
		};
	}
}
