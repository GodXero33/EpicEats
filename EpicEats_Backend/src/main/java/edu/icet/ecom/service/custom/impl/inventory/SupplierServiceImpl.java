package edu.icet.ecom.service.custom.impl.inventory;

import edu.icet.ecom.dto.inventory.Supplier;
import edu.icet.ecom.entity.inventory.SupplierEntity;
import edu.icet.ecom.repository.custom.inventory.SupplierRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.inventory.SupplierService;
import edu.icet.ecom.util.Response;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class SupplierServiceImpl implements SupplierService {
	private final SuperServiceHandler<Supplier, SupplierEntity> serviceHandler;
	private final SupplierRepository supplierRepository;

	public SupplierServiceImpl (SupplierRepository supplierRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(supplierRepository, mapper, Supplier.class, SupplierEntity.class);
		this.supplierRepository = supplierRepository;
	}

	@Override
	public Response<Supplier> get (Long id) {
		return this.serviceHandler.get(id);
	}

	@Override
	public Response<List<Supplier>> getAll () {
		return this.serviceHandler.getAll();
	}

	@Override
	public Response<Supplier> add (Supplier dto) {
		return this.serviceHandler.add(dto);
	}

	@Override
	public Response<Supplier> update (Supplier dto) {
		return this.serviceHandler.update(dto);
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.serviceHandler.delete(id);
	}

	@Override
	public Response<Boolean> isExist (Long id) {
		return supplierRepository.isExist(id);
	}

	@Override
	public Response<Boolean> isPhoneExist (String phone) {
		return this.supplierRepository.isPhoneExist(phone);
	}

	@Override
	public Response<Boolean> isPhoneExist (String phone, Long supplierId) {
		return this.supplierRepository.isPhoneExist(phone, supplierId);
	}

	@Override
	public Response<Boolean> isEmailExist (String email) {
		return this.supplierRepository.isEmailExist(email);
	}

	@Override
	public Response<Boolean> isEmailExist (String email, Long supplierId) {
		return this.supplierRepository.isEmailExist(email, supplierId);
	}
}
