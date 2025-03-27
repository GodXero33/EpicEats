package edu.icet.ecom.service.custom.impl.inventory;

import edu.icet.ecom.dto.inventory.InventoryPurchase;
import edu.icet.ecom.service.custom.inventory.InventoryPurchaseService;
import edu.icet.ecom.util.Response;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class InventoryPurchaseServiceImpl implements InventoryPurchaseService {
	@Override
	public Response<InventoryPurchase> get (Long id) {
		return null;
	}

	@Override
	public Response<List<InventoryPurchase>> getAll () {
		return null;
	}

	@Override
	public Response<InventoryPurchase> add (InventoryPurchase dto) {
		return null;
	}

	@Override
	public Response<InventoryPurchase> update (InventoryPurchase dto) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}
}
