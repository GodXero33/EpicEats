package edu.icet.ecom.repository.custom.impl.misc;

import edu.icet.ecom.entity.misc.CustomerEntity;
import edu.icet.ecom.repository.custom.misc.CustomerRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {
	@Override
	public Response<CustomerEntity> add (CustomerEntity entity) {
		return null;
	}

	@Override
	public Response<CustomerEntity> update (CustomerEntity entity) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<CustomerEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<CustomerEntity>> getAll () {
		return null;
	}
}
