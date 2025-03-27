package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.inventory.*;
import edu.icet.ecom.dto.inventory.Inventory;
import edu.icet.ecom.service.custom.inventory.InventoryPurchaseService;
import edu.icet.ecom.service.custom.inventory.InventoryService;
import edu.icet.ecom.service.custom.inventory.SupplierService;
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
@RequestMapping("/inventory")
@Tag(name = "Inventory Management", description = "APIs for managing inventory and suppliers")
public class InventoryController {
	private final InventoryService inventoryService;
	private final InventoryPurchaseService inventoryPurchaseService;
	private final SupplierService supplierService;
	private final ControllerResponseUtil controllerResponseUtil;

	private <T> CustomHttpResponse<T> getInvalidIdResponse () {
		return this.controllerResponseUtil.getInvalidDetailsResponse("Id can't be negative or zero.");
	}

	@InventoryGetAPiDoc
	@GetMapping("/get/{id}")
	public CustomHttpResponse<Inventory> get (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Inventory> response = this.inventoryService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Failed to find inventory");
		};
	}

	@InventoryGetAllApiDoc
	@GetMapping("/get-all")
	public CustomHttpResponse<List<Inventory>> getALl () {
		final Response<List<Inventory>> response = this.inventoryService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), Inventory.class, "All inventory loaded") :
			this.controllerResponseUtil.getServerErrorResponse(null);
	}

	@InventoryAddApiDoc
	@PostMapping("/add")
	public CustomHttpResponse<Inventory> add (@Valid @RequestBody Inventory inventory, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<Inventory> response = this.inventoryService.add(inventory);

		return response.getStatus() == ResponseType.CREATED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory added") :
			this.controllerResponseUtil.getServerErrorResponse(null);
	}

	@InventoryUpdateApiDoc
	@PutMapping("/update")
	public CustomHttpResponse<Inventory> update (@Valid @RequestBody Inventory inventory, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (inventory.getId() == null || inventory.getId() <= 0) this.getInvalidIdResponse();

		final Response<Inventory> response = this.inventoryService.update(inventory);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update inventory");
		};
	}

	@InventoryDeleteApiDoc
	@DeleteMapping("/delete/{id}")
	public CustomHttpResponse<Boolean> delete (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Boolean> response = this.inventoryService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, true, "Inventory deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(false);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, false, "Inventory delete failed");
		};
	}
}
