package edu.icet.ecom.service.custom.impl.inventory;

import edu.icet.ecom.dto.inventory.InventoryPurchase;
import edu.icet.ecom.entity.inventory.InventoryPurchaseEntity;
import edu.icet.ecom.repository.custom.inventory.InventoryPurchaseRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.inventory.InventoryPurchaseService;
import edu.icet.ecom.util.Response;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class InventoryPurchaseServiceImpl implements InventoryPurchaseService {
	private final SuperServiceHandler<InventoryPurchase, InventoryPurchaseEntity> serviceHandler;

	public InventoryPurchaseServiceImpl (InventoryPurchaseRepository inventoryPurchaseRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(inventoryPurchaseRepository, mapper, InventoryPurchase.class, InventoryPurchaseEntity.class);
	}

	@Override
	public Response<InventoryPurchase> get (Long id) {
		return this.serviceHandler.get(id);
	}

	@Override
	public Response<List<InventoryPurchase>> getAll () {
		return this.serviceHandler.getAll();
	}

	@Override
	public Response<InventoryPurchase> add (InventoryPurchase dto) {
		return this.serviceHandler.add(dto);
	}

	@Override
	public Response<InventoryPurchase> update (InventoryPurchase dto) {
		return this.serviceHandler.update(dto);
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return this.serviceHandler.delete(id);
	}
}
