package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.merchandise.*;
import edu.icet.ecom.dto.merchandise.MenuItem;
import edu.icet.ecom.dto.merchandise.SalesPackage;
import edu.icet.ecom.dto.merchandise.SalesPackageItem;
import edu.icet.ecom.dto.merchandise.SalesPackageRecord;
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
	public CustomHttpResponse<SalesPackage> addSalesPackage (@Valid @RequestBody SalesPackageRecord salesPackageRecord, BindingResult result) {
		if (result.hasErrors()) return this.controllerResponseUtil.getInvalidDetailsResponse(result);

		final Response<Boolean> salesPackageNameExistResponse = this.salesPackageService.isNameExist(salesPackageRecord.getName());

		if (salesPackageNameExistResponse.getStatus() == ResponseType.FOUND) return new CustomHttpResponse<>(HttpStatus.CONFLICT, null, "Sales package name is already exist");
		if (salesPackageNameExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<Boolean> allMenuItemsExistResponse = this.menuItemService.isAllMenuItemsExist(salesPackageRecord.getSalesPackageItems().stream().map(SalesPackageItem::getItemId).toList());

		if (allMenuItemsExistResponse.getStatus() == ResponseType.NOT_FOUND) return this.controllerResponseUtil.getInvalidDetailsResponse("There is one or more invalid menu item id found");
		if (allMenuItemsExistResponse.getStatus() == ResponseType.SERVER_ERROR) return this.controllerResponseUtil.getServerErrorResponse();

		final Response<SalesPackage> response = this.salesPackageService.add(salesPackageRecord);

		return response.getStatus() == ResponseType.CREATED ?
			new CustomHttpResponse<>(HttpStatus.OK, response.getData(), "Package added") :
			this.controllerResponseUtil.getServerErrorResponse();
	}
}
