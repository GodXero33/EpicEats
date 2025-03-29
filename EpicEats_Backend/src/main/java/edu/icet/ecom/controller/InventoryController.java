package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.inventory.*;
import edu.icet.ecom.dto.inventory.Inventory;
import edu.icet.ecom.dto.inventory.InventoryPurchase;
import edu.icet.ecom.dto.inventory.Supplier;
import edu.icet.ecom.dto.inventory.SupplierStockRecord;
import edu.icet.ecom.service.custom.inventory.InventoryPurchaseService;
import edu.icet.ecom.service.custom.inventory.InventoryService;
import edu.icet.ecom.service.custom.inventory.SupplierService;
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
@RequestMapping("/inventory")
@Tag(name = "Inventory Management", description = "APIs for managing inventory and suppliers")
public class InventoryController {
	private final InventoryService inventoryService;
	private final InventoryPurchaseService inventoryPurchaseService;
	private final SupplierService supplierService;
	private final ControllerResponseUtil controllerResponseUtil;
	private final CustomHttpResponseMap customHttpResponseMap;

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
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Failed to find inventory");
		};
	}

	@InventoryGetAllApiDoc
	@GetMapping("/all")
	public CustomHttpResponse<List<Inventory>> getALl () {
		final Response<List<Inventory>> response = this.inventoryService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All inventory loaded") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@InventoryGetAllBySupplierApiDoc
	@GetMapping("/by-supplier/{supplierId}")
	public CustomHttpResponse<Map<String, Object>> getAllBySupplier (@PathVariable("supplierId") Long supplierId) {
		if (supplierId <= 0) return this.getInvalidIdResponse();

		final Response<Boolean> supplierExistResponse = this.supplierService.isExist(supplierId);

		if (supplierExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getSupplierNotFoundResponse();
		if (supplierExistResponse.getStatus() == ResponseType.SERVER_ERROR) this.controllerResponseUtil.getServerErrorResponse();

		final Response<List<Inventory>> response = this.inventoryService.getAllBySupplier(supplierId);

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(
				HttpStatus.OK,
				this.customHttpResponseMap.builder()
					.keys("supplier", "inventories")
					.values(supplierId, response.getData())
					.build(),
				"All inventory loaded related to supplier"
			) :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@InventoryAddApiDoc
	@PostMapping("/")
	public CustomHttpResponse<SupplierStockRecord> add (@Valid @RequestBody SupplierStockRecord supplierStockRecord, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (supplierStockRecord.getSupplierId() == null || supplierStockRecord.getSupplierId() <= 0) this.getInvalidIdResponse();

		final Response<Boolean> supplierExistResponse = this.supplierService.isExist(supplierStockRecord.getSupplierId());

		if (supplierExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getSupplierNotFoundResponse();
		if (supplierExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<SupplierStockRecord> response = this.inventoryService.add(supplierStockRecord);

		return switch (response.getStatus()) {
			case CREATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory added");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
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
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update inventory");
		};
	}

	@InventoryUpdateStockApiDoc
	@PatchMapping("/stock")
	public CustomHttpResponse<SupplierStockRecord> updateStock (@Valid @RequestBody SupplierStockRecord supplierStockRecord, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		if (supplierStockRecord.getInventory().getId() == null ||
			supplierStockRecord.getInventory().getId() <= 0 ||
			supplierStockRecord.getSupplierId() == null ||
			supplierStockRecord.getSupplierId() <= 0) this.getInvalidIdResponse();

		final Response<Boolean> supplierExistResponse = this.supplierService.isExist(supplierStockRecord.getSupplierId());

		if (supplierExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getSupplierNotFoundResponse();
		if (supplierExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<SupplierStockRecord> response = this.inventoryService.updateStock(supplierStockRecord);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update inventory");
		};
	}

	@InventoryDeleteApiDoc
	@DeleteMapping("/{id}")
	public CustomHttpResponse<Object> delete (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.inventoryService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Inventory deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Inventory delete failed");
		};
	}

	@InventoryPurchaseGetApiDoc
	@GetMapping("/purchase/{id}")
	public CustomHttpResponse<InventoryPurchase> getInventoryPurchase (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<InventoryPurchase> response = this.inventoryPurchaseService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory purchase found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Inventory purchase failed to find");
		};
	}

	@InventoryPurchaseGetAllApiDoc
	@GetMapping("/purchase/all")
	public CustomHttpResponse<List<InventoryPurchase>> getALlInventoryPurchase () {
		final Response<List<InventoryPurchase>> response = this.inventoryPurchaseService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "ALl inventory purchases loaded successfully") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@InventoryPurchaseAddApiDoc
	@PostMapping("/purchase/")
	public CustomHttpResponse<InventoryPurchase> addInventoryPurchase (@Valid @RequestBody InventoryPurchase inventoryPurchase, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (inventoryPurchase.getInventoryId() != null && inventoryPurchase.getMenuItemId() != null) return this.controllerResponseUtil.getInvalidDetailsResponse("Can't have both inventoryId and menuItemId fields. Only one value allowed"); // An inventory purchase either menu item purchase or inventory purchase

		final Response<Boolean> inventoryExistResponse = this.inventoryService.isExist(inventoryPurchase.getInventoryId());

		if (inventoryExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.controllerResponseUtil.getInvalidDetailsResponse("No inventory found with given inventoryId");
		if (inventoryExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<Boolean> supplierExistResponse = this.supplierService.isExist(inventoryPurchase.getSupplierId());

		if (supplierExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getSupplierNotFoundResponse();
		if (supplierExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<InventoryPurchase> response = this.inventoryPurchaseService.add(inventoryPurchase);

		return switch (response.getStatus()) {
			case CREATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory purchase added");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
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
		if (inventoryExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<Boolean> supplierExistResponse = this.supplierService.isExist(inventoryPurchase.getSupplierId());

		if (supplierExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.getSupplierNotFoundResponse();
		if (supplierExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<InventoryPurchase> response = this.inventoryPurchaseService.update(inventoryPurchase);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Inventory purchase updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Inventory purchase not updated");
		};
	}

	@InventoryPurchaseDeleteApiDoc
	@DeleteMapping("/purchase/{id}")
	public CustomHttpResponse<Object> deleteInventoryPurchase (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.inventoryPurchaseService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Inventory purchase update");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete inventory purchase");
		};
	}

	@SupplierGetApiDoc
	@GetMapping("/supplier/{id}")
	public CustomHttpResponse<Supplier> getSupplier (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Supplier> response = this.supplierService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Supplier found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Supplier not found");
		};
	}

	@SupplierGetAllApiDoc
	@GetMapping("/supplier/all")
	public CustomHttpResponse<List<Supplier>> getAllSuppliers () {
		final Response<List<Supplier>> response = this.supplierService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All suppliers are loaded") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@SupplierAddApiDoc
	@PostMapping("/supplier")
	public CustomHttpResponse<Supplier> addSupplier (@Valid @RequestBody Supplier supplier, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<Boolean> supplierPhoneExistResponse = this.supplierService.isPhoneExist(supplier.getPhone());

		if (supplierPhoneExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Supplier phone number already taken");
		if (supplierPhoneExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<Boolean> supplierEmailExistResponse = this.supplierService.isEmailExist(supplier.getEmail());

		if (supplierEmailExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Supplier email address already taken");
		if (supplierEmailExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<Supplier> response = this.supplierService.add(supplier);

		return response.getStatus() == ResponseType.CREATED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Supplier added") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@SupplierUpdateApiDoc
	@PutMapping("/supplier")
	public CustomHttpResponse<Supplier> updateSupplier (@Valid @RequestBody Supplier supplier, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<Boolean> supplierPhoneExistResponse = this.supplierService.isPhoneExist(supplier.getPhone(), supplier.getId());

		if (supplierPhoneExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Supplier phone number already taken");
		if (supplierPhoneExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<Boolean> supplierEmailExistResponse = this.supplierService.isEmailExist(supplier.getEmail(), supplier.getId());

		if (supplierEmailExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Supplier email address already taken");
		if (supplierEmailExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<Supplier> response = this.supplierService.add(supplier);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Supplier updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update supplier");
		};
	}

	@SupplierDeleteApiDoc
	@DeleteMapping("/supplier/{id}")
	public CustomHttpResponse<Object> deleteSupplier (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.supplierService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Supplier deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete supplier");
		};
	}
}
