package edu.icet.ecom.service.custom.impl.inventory;

import edu.icet.ecom.dto.inventory.Inventory;
import edu.icet.ecom.dto.inventory.Supplier;
import edu.icet.ecom.dto.inventory.SupplierStockRecord;
import edu.icet.ecom.dto.inventory.SupplierStockRecordLite;
import edu.icet.ecom.entity.inventory.InventoryEntity;
import edu.icet.ecom.entity.inventory.SupplierStockRecordEntity;
import edu.icet.ecom.entity.inventory.SupplierStockRecordLiteEntity;
import edu.icet.ecom.repository.custom.inventory.InventoryRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.inventory.InventoryService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enums.ResponseType;
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
	public Response<Object> delete (Long id) {
		return serviceHandler.delete(id);
	}

	@Override
	public Response<List<Inventory>> getAllBySupplier (Long supplierId) {
		final Response<List<InventoryEntity>> response = this.inventoryRepository.getAllBySupplier(supplierId);

		return response.getStatus() == ResponseType.FOUND ?
			new Response<>(response.getData().stream().map(inventoryEntity -> this.mapper.map(inventoryEntity, Inventory.class)).toList(), response.getStatus()) :
			new Response<>(null, response.getStatus());
	}

	@Override
	public Response<SupplierStockRecord> add (SupplierStockRecordLite supplierStockRecord) {
		final Response<SupplierStockRecordEntity> response = this.inventoryRepository.add(
			new SupplierStockRecordLiteEntity(
				this.mapper.map(supplierStockRecord.getInventory(), InventoryEntity.class),
				supplierStockRecord.getQuantity(),
				supplierStockRecord.getSupplierId()
			)
		);

		return response.getStatus() == ResponseType.CREATED ?
			new Response<>(
				new SupplierStockRecord(
					this.mapper.map(response.getData().getInventory(), Inventory.class),
					response.getData().getQuantity(),
					this.mapper.map(response.getData().getSupplier(), Supplier.class)
				),
				response.getStatus()
			) :
			new Response<>(null, response.getStatus());
	}

	@Override
	public Response<SupplierStockRecord> updateStock (SupplierStockRecordLite supplierStockRecord) {
		final Response<SupplierStockRecordEntity> response = this.inventoryRepository.updateStock(
			new SupplierStockRecordLiteEntity(
				this.mapper.map(supplierStockRecord.getInventory(), InventoryEntity.class),
				supplierStockRecord.getQuantity(),
				supplierStockRecord.getSupplierId()
			)
		);

		return response.getStatus() == ResponseType.UPDATED ?
			new Response<>(
				new SupplierStockRecord(
					this.mapper.map(response.getData().getInventory(), Inventory.class),
					response.getData().getQuantity(),
					this.mapper.map(response.getData().getSupplier(), Supplier.class)
				),
				response.getStatus()
			) :
			new Response<>(null, response.getStatus());
	}

	@Override
	public Response<Boolean> isExist (Long id) {
		return this.inventoryRepository.isExist(id);
	}
}
