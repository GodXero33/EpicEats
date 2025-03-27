package edu.icet.ecom.service.custom.impl.inventory;

import edu.icet.ecom.dto.inventory.Supplier;
import edu.icet.ecom.service.custom.inventory.SupplierService;
import edu.icet.ecom.util.Response;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class SupplierServiceImpl implements SupplierService {
	@Override
	public Response<Supplier> get (Long id) {
		return null;
	}

	@Override
	public Response<List<Supplier>> getAll () {
		return null;
	}

	@Override
	public Response<Supplier> add (Supplier dto) {
		return null;
	}

	@Override
	public Response<Supplier> update (Supplier dto) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}
}
