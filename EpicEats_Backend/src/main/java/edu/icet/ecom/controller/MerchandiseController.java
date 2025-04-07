package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.merchandise.*;
import edu.icet.ecom.dto.merchandise.*;
import edu.icet.ecom.service.custom.merchandise.MenuItemService;
import edu.icet.ecom.service.custom.merchandise.SalesPackageService;
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
@RequestMapping("/merchandise")
@Tag(name = "Merchandise Management", description = "APIs for managing merchandises")
public class MerchandiseController {
	private final MenuItemService menuItemService;
	private final SalesPackageService salesPackageService;
	private final ControllerResponseUtil controllerResponseUtil;

	private <T> CustomHttpResponse<T> getInvalidIdResponse () {
		return this.controllerResponseUtil.getInvalidDetailsResponse("Id can't be negative or zero.");
	}

	@MenuItemGetApiDoc
	@GetMapping("/menu-item/{id}")
	public CustomHttpResponse<MenuItem> getMenuItem (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<MenuItem> response = this.menuItemService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Menu item found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Failed to find menu item");
		};
	}

	@MenuItemGetAllApiDoc
	@GetMapping("/menu-item/all")
	public CustomHttpResponse<List<MenuItem>> getAllMenuItems () {
		final Response<List<MenuItem>> response = this.menuItemService.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "All menu items are found") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@MenuItemAddApiDoc
	@PostMapping("/menu-item")
	public CustomHttpResponse<MenuItem> addMenuItem (@Valid @RequestBody MenuItem menuItem, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<MenuItem> response = this.menuItemService.add(menuItem);

		return response.getStatus() == ResponseType.CREATED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Menu item added") :
			this.controllerResponseUtil.getServerErrorResponse();
	}

	@MenuItemUpdateApiDoc
	@PutMapping("/menu-item")
	public CustomHttpResponse<MenuItem> updateMenuItem (@Valid @RequestBody MenuItem menuItem, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (menuItem.getId() == null || menuItem.getId() <= 0) return this.getInvalidIdResponse();

		final Response<MenuItem> response = this.menuItemService.update(menuItem);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Menu item updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update menu item");
		};
	}

	@MenuItemDeleteApiDoc
	@DeleteMapping("/menu-item/{id}")
	public CustomHttpResponse<Object> deleteMenuItem (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.menuItemService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Menu item deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete menu item");
		};
	}

	@SalesPackageAddApiDoc
	@PostMapping("/sales-package")
	public CustomHttpResponse<SalesPackage> addSalesPackage (@Valid @RequestBody SalesPackageLite salesPackageLite, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (salesPackageLite.getMenuItemQuantities().size() != salesPackageLite.getMenuItemIDs().size()) return this.controllerResponseUtil.getInvalidDetailsResponse("Both menuItemIds and getMenuItemQuantities length must be same");

		final Response<Boolean> salesPackageNameExistResponse = this.salesPackageService.isNameExist(salesPackageLite.getName());

		if (salesPackageNameExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Sales package name is already exist");
		if (salesPackageNameExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<SuperSalesPackage> response = this.salesPackageService.add(salesPackageLite);

		return switch (response.getStatus()) {
			case CREATED -> new CustomHttpResponse<>(HttpStatus.OK, (SalesPackage) response.getData(), "Sales package added");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to add sales package");
		};
	}

	@SalesPackageUpdateApiDoc
	@PutMapping("/sales-package")
	public CustomHttpResponse<SalesPackage> updateSalesPackage (@Valid @RequestBody SalesPackageLite salesPackageLite, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);
		if (salesPackageLite.getMenuItemQuantities().size() != salesPackageLite.getMenuItemIDs().size()) return this.controllerResponseUtil.getInvalidDetailsResponse("Both menuItemIds and getMenuItemQuantities length must be same");

		final Response<Boolean> salesPackageNameExistResponse = this.salesPackageService.isNameExist(salesPackageLite.getName(), salesPackageLite.getId());

		if (salesPackageNameExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Sales package name is already exist");
		if (salesPackageNameExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<SuperSalesPackage> response = this.salesPackageService.update(salesPackageLite);

		return switch (response.getStatus()) {
			case UPDATED -> new CustomHttpResponse<>(HttpStatus.OK, (SalesPackage) response.getData(), "Sales package updated");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to update sales package");
		};
	}

	@SalesPackageDeleteApiDoc
	@DeleteMapping("/sales-package/{id}")
	public CustomHttpResponse<Object> deleteSalesPackage (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<Object> response = this.salesPackageService.delete(id);

		return switch (response.getStatus()) {
			case DELETED -> new CustomHttpResponse<>(HttpStatus.OK, null, "Sales package deleted");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_MODIFIED, null, "Failed to delete sales package");
		};
	}

	@SalesPackageGetApiDoc
	@GetMapping("/sales-package/{id}")
	public CustomHttpResponse<SalesPackage> getSalesPackages (@PathVariable("id") Long id) {
		if (id <= 0) return this.getInvalidIdResponse();

		final Response<SuperSalesPackage> response = this.salesPackageService.get(id);

		return switch (response.getStatus()) {
			case FOUND -> new CustomHttpResponse<>(HttpStatus.OK, (SalesPackage) response.getData(), "Menu item found");
			case SERVER_ERROR -> this.controllerResponseUtil.getServerErrorResponse();
			default -> new CustomHttpResponse<>(HttpStatus.NOT_FOUND, null, "Failed to find menu item");
		};
	}

	@SalesPackageGetAllApiDoc
	@GetMapping("sales-package/all")
	public CustomHttpResponse<AllSalesPackages> getAllSalesPackages () {
		final Response<SuperSalesPackage> response = this.salesPackageService.getAllStructured();

		return response.getStatus() == ResponseType.FOUND ?
			new CustomHttpResponse<>(HttpStatus.OK, (AllSalesPackages) response.getData(), "All sales packages are retrieved successfully") :
			this.controllerResponseUtil.getServerErrorResponse();
	}
}
