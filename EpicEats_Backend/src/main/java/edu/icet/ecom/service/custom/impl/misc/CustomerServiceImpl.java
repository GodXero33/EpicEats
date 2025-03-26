package edu.icet.ecom.service.custom.impl.misc;

import edu.icet.ecom.dto.misc.Customer;
import edu.icet.ecom.entity.misc.CustomerEntity;
import edu.icet.ecom.repository.custom.misc.CustomerRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.misc.CustomerService;
import edu.icet.ecom.util.Response;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class CustomerServiceImpl implements CustomerService {
	private final SuperServiceHandler<Customer, CustomerEntity> serviceHandler;
	private final CustomerRepository customerRepository;

	public CustomerServiceImpl (CustomerRepository customerRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(customerRepository, mapper, Customer.class, CustomerEntity.class);
		this.customerRepository = customerRepository;
	}

	@Override
	public Response<Customer> get (Long id) {
		return this.serviceHandler.get(id);
	}

	@Override
	public Response<List<Customer>> getAll () {
		return this.serviceHandler.getAll();
	}

	@Override
	public Response<Customer> add (Customer dto) {
		return this.serviceHandler.add(dto);
	}

	@Override
	public Response<Customer> update (Customer dto) {
		return this.serviceHandler.update(dto);
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return this.serviceHandler.delete(id);
	}

	@Override
	public Response<Boolean> isPhoneExist (String phone) {
		return this.customerRepository.isPhoneExist(phone);
	}

	@Override
	public Response<Boolean> isPhoneExist (String phone, Long customerId) {
		return this.customerRepository.isPhoneExist(phone, customerId);
	}

	@Override
	public Response<Boolean> isEmailExist (String email) {
		return this.customerRepository.isEmailExist(email);
	}

	@Override
	public Response<Boolean> isEmailExist (String email, Long customerId) {
		return this.customerRepository.isEmailExist(email, customerId);
	}
}
