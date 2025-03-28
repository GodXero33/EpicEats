package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.inventory.*;
import edu.icet.ecom.dto.inventory.Inventory;
import edu.icet.ecom.dto.inventory.InventoryPurchase;
import edu.icet.ecom.dto.inventory.Supplier;
import edu.icet.ecom.dto.inventory.SupplierInventoryRecord;
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

	private <T> CustomHttpResponse<T> getSupplierNotFoundResponse () {
		return this.controllerResponseUtil.getInvalidDetailsResponse("No supplier found with given supplierId");
	}

	@InventoryGetAPiDoc
	@GetMapping("/{id}")
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
	@GetMapping("/all")
	public CustomHttpResponse<List<Inventory>> getALl () {
		final Response<List<Inventory>> response = this.inventoryService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), Inventory.class, "All inventory loaded") :
			this.controllerResponseUtil.getServerErrorResponse(null);
	}

	@InventoryGetAllBySupplierApiDoc
	@GetMapping("/by-supplier/{supplierId}")
	public CustomHttpResponse<List<SupplierInventoryRecord>> getAllBySupplier (@PathVariable("supplierId") Long supplierId) {
		if (supplierId <= 0) return this.getInvalidIdResponse();

		final Response<Boolean> supplierExistResponse = this.supplierService.isExist(supplierId);

		if (supplierExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getSupplierNotFoundResponse();
		if (supplierExistResponse.getStatus() == ResponseType.SERVER_ERROR) this.controllerResponseUtil.getServerErrorResponse(null);

		final Response<List<SupplierInventoryRecord>> response = this.inventoryService.getAllBySupplier(supplierId);

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), SupplierInventoryRecord.class, "All inventory loaded related to supplier") :
			this.controllerResponseUtil.getServerErrorResponse(null);
	}

	@InventoryAddApiDoc
	@PostMapping("/")
	public CustomHttpResponse<SupplierInventoryRecord> add (@Valid @RequestBody SupplierInventoryRecord supplierInventoryRecord, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (supplierInventoryRecord.getSupplierId() == null || supplierInventoryRecord.getSupplierId() <= 0) this.getInvalidIdResponse();

		final Response<Boolean> supplierExistResponse = this.supplierService.isExist(supplierInventoryRecord.getSupplierId());

		if (supplierExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getSupplierNotFoundResponse();
		if (supplierExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse(null);

		final Response<SupplierInventoryRecord> response = this.inventoryService.add(supplierInventoryRecord);

		return switch (response.getStatus()) {
			case CREATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory added");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to add inventory");
		};
	}

	@InventoryUpdateApiDoc
	@PutMapping("/")
	public CustomHttpResponse<Inventory> update (@Valid @RequestBody Inventory inventory, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (inventory.getId() <= 0) return this.getInvalidIdResponse();

		final Response<Inventory> response = this.inventoryService.update(inventory);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update inventory");
		};
	}

	@InventoryUpdateStockApiDoc
	@PatchMapping("/stock")
	public CustomHttpResponse<SupplierInventoryRecord> updateStock (@Valid @RequestBody SupplierInventoryRecord supplierInventoryRecord, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		if (supplierInventoryRecord.getInventory().getId() == null ||
			supplierInventoryRecord.getInventory().getId() <= 0 ||
			supplierInventoryRecord.getSupplierId() == null ||
			supplierInventoryRecord.getSupplierId() <= 0) this.getInvalidIdResponse();

		final Response<Boolean> supplierExistResponse = this.supplierService.isExist(supplierInventoryRecord.getSupplierId());

		if (supplierExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getSupplierNotFoundResponse();
		if (supplierExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse(null);

		final Response<SupplierInventoryRecord> response = this.inventoryService.updateStock(supplierInventoryRecord);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update inventory");
		};
	}

	@InventoryDeleteApiDoc
	@DeleteMapping("/{id}")
	public CustomHttpResponse<Boolean> delete (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Boolean> response = this.inventoryService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, true, "Inventory deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(false);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, false, "Inventory delete failed");
		};
	}

	@InventoryPurchaseGetApiDoc
	@GetMapping("/purchase/{id}")
	public CustomHttpResponse<InventoryPurchase> getInventoryPurchase (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<InventoryPurchase> response = this.inventoryPurchaseService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory purchase found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Inventory purchase failed to find");
		};
	}

	@InventoryPurchaseGetAllApiDoc
	@GetMapping("/purchase/all")
	public CustomHttpResponse<List<InventoryPurchase>> getALlInventoryPurchase () {
		final Response<List<InventoryPurchase>> response = this.inventoryPurchaseService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), InventoryPurchase.class, "ALl inventory purchases loaded successfully") :
			this.controllerResponseUtil.getServerErrorResponse(null);
	}

	@InventoryPurchaseAddApiDoc
	@PostMapping("/purchase/")
	public CustomHttpResponse<InventoryPurchase> addInventoryPurchase (@Valid @RequestBody InventoryPurchase inventoryPurchase, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (inventoryPurchase.getInventoryId() != null && inventoryPurchase.getMenuItemId() != null) return this.controllerResponseUtil.getInvalidDetailsResponse("Can't have both inventoryId and menuItemId fields. Only one value allowed"); // An inventory purchase either menu item purchase or inventory purchase

		final Response<Boolean> inventoryExistResponse = this.inventoryService.isExist(inventoryPurchase.getInventoryId());

		if (inventoryExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.controllerResponseUtil.getInvalidDetailsResponse("No inventory found with given inventoryId");
		if (inventoryExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse(null);

		final Response<Boolean> supplierExistResponse = this.supplierService.isExist(inventoryPurchase.getSupplierId());

		if (supplierExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getSupplierNotFoundResponse();
		if (supplierExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse(null);

		final Response<InventoryPurchase> response = this.inventoryPurchaseService.add(inventoryPurchase);

		return switch (response.getStatus()) {
			case CREATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory purchase added");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to add inventory purchase");
		};
	}

	@InventoryPurchaseUpdateApiDoc
	@PutMapping("/purchase/")
	public CustomHttpResponse<InventoryPurchase> updateInventoryPurchase (@Valid @RequestBody InventoryPurchase inventoryPurchase, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (inventoryPurchase.getId() == null || inventoryPurchase.getId() <= 0) return this.getInvalidIdResponse();
		if (inventoryPurchase.getInventoryId() != null && inventoryPurchase.getMenuItemId() != null) return this.controllerResponseUtil.getInvalidDetailsResponse("Can't have both inventoryId and menuItemId fields. Only one value allowed"); // An inventory purchase either menu item purchase or inventory purchase

		final Response<Boolean> inventoryExistResponse = this.inventoryService.isExist(inventoryPurchase.getInventoryId());

		if (inventoryExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.controllerResponseUtil.getInvalidDetailsResponse("No inventory found with given inventoryId");
		if (inventoryExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse(null);

		final Response<Boolean> supplierExistResponse = this.supplierService.isExist(inventoryPurchase.getSupplierId());

		if (supplierExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getSupplierNotFoundResponse();
		if (supplierExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse(null);

		final Response<InventoryPurchase> response = this.inventoryPurchaseService.update(inventoryPurchase);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory purchase updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Inventory purchase not updated");
		};
	}

	@InventoryPurchaseDeleteApiDoc
	@DeleteMapping("/purchase/{id}")
	public CustomHttpResponse<Boolean> deleteInventoryPurchase (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Boolean> response = this.inventoryPurchaseService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, true, "Inventory purchase update");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(false);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, false, "Failed to delete inventory purchase");
		};
	}

	@SupplierGetApiDoc
	@GetMapping("/supplier/{id}")
	public CustomHttpResponse<Supplier> getSupplier (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Supplier> response = this.supplierService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Supplier found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse(null);
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Supplier not found");
		};
	}

	@SupplierGetAllApiDoc
	@GetMapping("/supplier/all")
	public CustomHttpResponse<List<Supplier>> getAllSuppliers () {
		final Response<List<Supplier>> response = this.supplierService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), Supplier.class, "All suppliers are loaded") :
			this.controllerResponseUtil.getServerErrorResponse(null);
	}
}
