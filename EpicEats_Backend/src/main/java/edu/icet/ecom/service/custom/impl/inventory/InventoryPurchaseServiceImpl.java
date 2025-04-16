package edu.icet.ecom.service.custom.impl.inventory;

import edu.icet.ecom.dto.inventory.AllInventoryPurchases;
import edu.icet.ecom.dto.inventory.InventoryPurchase;
import edu.icet.ecom.dto.inventory.SuperInventoryPurchase;
import edu.icet.ecom.entity.inventory.InventoryPurchaseLiteEntity;
import edu.icet.ecom.entity.inventory.SuperInventoryPurchaseEntity;
import edu.icet.ecom.repository.custom.inventory.InventoryPurchaseRepository;
import edu.icet.ecom.service.custom.inventory.InventoryPurchaseService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enums.ResponseType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class InventoryPurchaseServiceImpl implements InventoryPurchaseService {
	private final InventoryPurchaseRepository inventoryPurchaseRepository;
	private final ModelMapper mapper;

	public InventoryPurchaseServiceImpl (InventoryPurchaseRepository inventoryPurchaseRepository, ModelMapper mapper) {
		this.inventoryPurchaseRepository = inventoryPurchaseRepository;
		this.mapper = mapper;
	}

	@Override
	public Response<SuperInventoryPurchase> get (Long id) {
		final Response<SuperInventoryPurchaseEntity> response = this.inventoryPurchaseRepository.get(id);

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			this.mapper.map(response.getData(), InventoryPurchase.class) :
			null
			, response.getStatus());
	}

	@Override
	public Response<List<SuperInventoryPurchase>> getAll () {
		final Response<List<SuperInventoryPurchaseEntity>> response = this.inventoryPurchaseRepository.getAll();

		return response.getStatus() == ResponseType.FOUND ?
			new Response<>(response.getData().stream().map(inventoryPurchaseEntity -> (SuperInventoryPurchase) this.mapper.map(inventoryPurchaseEntity, InventoryPurchase.class)).toList(), response.getStatus()) :
			new Response<>(null, response.getStatus());
	}

	@Override
	public Response<SuperInventoryPurchase> add (SuperInventoryPurchase dto) {
		final Response<SuperInventoryPurchaseEntity> response = this.inventoryPurchaseRepository.add(this.mapper.map(dto, InventoryPurchaseLiteEntity.class));

		return new Response<>(response.getStatus() == ResponseType.CREATED ?
			this.mapper.map(response.getData(), InventoryPurchase.class) :
			null
		, response.getStatus());
	}

	@Override
	public Response<SuperInventoryPurchase> update (SuperInventoryPurchase dto) {
		final Response<SuperInventoryPurchaseEntity> response = this.inventoryPurchaseRepository.update(this.mapper.map(dto, InventoryPurchaseLiteEntity.class));

		return new Response<>(response.getStatus() == ResponseType.UPDATED ?
			this.mapper.map(response.getData(), InventoryPurchase.class) :
			null
			, response.getStatus());
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.inventoryPurchaseRepository.delete(id);
	}

	@Override
	public Response<SuperInventoryPurchase> getAllStructured () {
		final Response<SuperInventoryPurchaseEntity> response = this.inventoryPurchaseRepository.getAllStructured();

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			this.mapper.map(response.getData(), AllInventoryPurchases.class) :
			null
			, response.getStatus());
	}
}
