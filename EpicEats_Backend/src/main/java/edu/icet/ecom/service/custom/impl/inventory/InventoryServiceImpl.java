package edu.icet.ecom.service.custom.impl.inventory;

import edu.icet.ecom.dto.inventory.Inventory;
import edu.icet.ecom.dto.inventory.SupplierInventoryRecord;
import edu.icet.ecom.entity.inventory.InventoryEntity;
import edu.icet.ecom.entity.inventory.SupplierInventoryRecordEntity;
import edu.icet.ecom.repository.custom.inventory.InventoryRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.inventory.InventoryService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class InventoryServiceImpl implements InventoryService {
	private final SuperServiceHandler<Inventory, InventoryEntity> serviceHandler;
	private final InventoryRepository inventoryRepository;
	private final ModelMapper mapper;

	public InventoryServiceImpl (InventoryRepository inventoryRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(inventoryRepository, mapper, Inventory.class, InventoryEntity.class);
		this.inventoryRepository = inventoryRepository;
		this.mapper = mapper;
	}

	@Override
	public Response<Inventory> get (Long id) {
		return serviceHandler.get(id);
	}

	@Override
	public Response<List<Inventory>> getAll () {
		return serviceHandler.getAll();
	}

	@Override
	public Response<Inventory> add (Inventory dto) {
		return serviceHandler.add(dto);
	}

	@Override
	public Response<Inventory> update (Inventory dto) {
		return serviceHandler.update(dto);
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return serviceHandler.delete(id);
	}

	@Override
	public Response<List<SupplierInventoryRecord>> getAllBySupplier (Long supplierId) {
		final Response<List<SupplierInventoryRecordEntity>> response = this.inventoryRepository.getAllBySupplier(supplierId);

		return response.getStatus() == ResponseType.FOUND ?
			new Response<>(response.getData().stream().map(inventoryRecordEntity -> new SupplierInventoryRecord(
					this.mapper.map(inventoryRecordEntity.getInventory(), Inventory.class),
					inventoryRecordEntity.getQuantity(),
					inventoryRecordEntity.getSupplierId()
				)).toList(), response.getStatus()) :
			new Response<>(null, response.getStatus());
	}

	@Override
	public Response<SupplierInventoryRecord> add (SupplierInventoryRecord supplierInventoryRecord) {
		final Response<SupplierInventoryRecordEntity> response = this.inventoryRepository.add(
			new SupplierInventoryRecordEntity(
				this.mapper.map(supplierInventoryRecord.getInventory(), InventoryEntity.class),
				supplierInventoryRecord.getQuantity(),
				supplierInventoryRecord.getSupplierId()
			)
		);

		return response.getStatus() == ResponseType.CREATED ?
			new Response<>(
				new SupplierInventoryRecord(
					this.mapper.map(response.getData().getInventory(), Inventory.class),
					response.getData().getQuantity(),
					response.getData().getSupplierId()
				),
				response.getStatus()
			) :
			new Response<>(null, response.getStatus());
	}

	@Override
	public Response<SupplierInventoryRecord> updateStock (SupplierInventoryRecord supplierInventoryRecord) {
		final Response<SupplierInventoryRecordEntity> response = this.inventoryRepository.updateStock(
			new SupplierInventoryRecordEntity(
				this.mapper.map(supplierInventoryRecord.getInventory(), InventoryEntity.class),
				supplierInventoryRecord.getQuantity(),
				supplierInventoryRecord.getSupplierId()
			)
		);

		return response.getStatus() == ResponseType.UPDATED ?
			new Response<>(
				new SupplierInventoryRecord(
					this.mapper.map(response.getData().getInventory(), Inventory.class),
					response.getData().getQuantity(),
					response.getData().getSupplierId()
				),
				response.getStatus()
			) :
			new Response<>(null, response.getStatus());
	}
}
