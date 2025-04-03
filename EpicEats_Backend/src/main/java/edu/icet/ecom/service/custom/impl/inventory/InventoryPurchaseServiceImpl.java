package edu.icet.ecom.service.custom.impl.inventory;

import edu.icet.ecom.dto.inventory.InventoryPurchase;
import edu.icet.ecom.dto.inventory.InventoryPurchaseLite;
import edu.icet.ecom.entity.inventory.InventoryPurchaseEntity;
import edu.icet.ecom.entity.inventory.InventoryPurchaseLiteEntity;
import edu.icet.ecom.repository.custom.inventory.InventoryPurchaseRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.inventory.InventoryPurchaseService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class InventoryPurchaseServiceImpl implements InventoryPurchaseService {
	private final SuperServiceHandler<InventoryPurchase, InventoryPurchaseEntity> serviceHandler;
	private final InventoryPurchaseRepository inventoryPurchaseRepository;
	private final ModelMapper mapper;

	public InventoryPurchaseServiceImpl (InventoryPurchaseRepository inventoryPurchaseRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(inventoryPurchaseRepository, mapper, InventoryPurchase.class, InventoryPurchaseEntity.class);
		this.inventoryPurchaseRepository = inventoryPurchaseRepository;
		this.mapper = mapper;
	}

	@Override
	public Response<InventoryPurchase> get (Long id) {
		final Response<InventoryPurchaseEntity> response = this.inventoryPurchaseRepository.get(id);

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			this.mapper.map(response.getData(), InventoryPurchase.class) :
			null
			, response.getStatus());
	}

	@Override
	public Response<List<InventoryPurchase>> getAll () {
		final Response<List<InventoryPurchaseEntity>> response = this.inventoryPurchaseRepository.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new Response<>(response.getData().stream().map(inventoryPurchaseEntity -> this.mapper.map(inventoryPurchaseEntity, InventoryPurchase.class)).toList(), response.getStatus()) :
			new Response<>(null, response.getStatus());
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
	public Response<Object> delete (Long id) {
		return this.serviceHandler.delete(id);
	}

	@Override
	public Response<InventoryPurchase> add (InventoryPurchaseLite inventoryPurchase) {
		final Response<InventoryPurchaseEntity> response = this.inventoryPurchaseRepository.add(this.mapper.map(inventoryPurchase, InventoryPurchaseLiteEntity.class));

		return new Response<>(response.getStatus() == ResponseType.CREATED ?
			this.mapper.map(response.getData(), InventoryPurchase.class) :
			null
			, response.getStatus());
	}

	@Override
	public Response<InventoryPurchase> update (InventoryPurchaseLite inventoryPurchase) {
		final Response<InventoryPurchaseEntity> response = this.inventoryPurchaseRepository.update(this.mapper.map(inventoryPurchase, InventoryPurchaseLiteEntity.class));

		return new Response<>(response.getStatus() == ResponseType.UPDATED ?
			this.mapper.map(response.getData(), InventoryPurchase.class) :
			null
			, response.getStatus());
	}
}
