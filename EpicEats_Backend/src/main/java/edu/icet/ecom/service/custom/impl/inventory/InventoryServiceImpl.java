package edu.icet.ecom.service.custom.impl.inventory;

import edu.icet.ecom.dto.inventory.Inventory;
import edu.icet.ecom.entity.inventory.InventoryEntity;
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
	public Response<List<Inventory>> getAllBySupplier (Long supplierId) {
		final Response<List<InventoryEntity>> response = this.inventoryRepository.getAllBySupplier(supplierId);

		return response.getStatus() == ResponseType.FOUND ?
			new Response<>(response.getData().stream().map(inventoryEntity -> this.mapper.map(inventoryEntity, Inventory.class)).toList(), response.getStatus()) :
			new Response<>(null, response.getStatus());
	}
}
