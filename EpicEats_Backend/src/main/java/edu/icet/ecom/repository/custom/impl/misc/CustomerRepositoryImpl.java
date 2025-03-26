package edu.icet.ecom.repository.custom.impl.misc;

import edu.icet.ecom.entity.misc.CustomerEntity;
import edu.icet.ecom.repository.custom.misc.CustomerRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	private Response<Boolean> getExistence (String query, Object ...binds) {
		try (final ResultSet resultSet = this.crudUtil.execute(query, binds)) {
			return resultSet.next() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<CustomerEntity> add (CustomerEntity entity) {
		try {
			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"INSERT INTO customer (name, phone, email, address) VALUES (?, ?, ?, ?)",
				entity.getName(),
				entity.getPhone(),
				entity.getEmail(),
				entity.getAddress()
			);

			entity.setId(generatedId);

			return new Response<>(entity, ResponseType.CREATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<CustomerEntity> update (CustomerEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"UPDATE customer SET name = ?, phone = ?, email = ?, address = ? WHERE is_deleted = FALSE AND id = ?",
				entity.getName(),
				entity.getPhone(),
				entity.getEmail(),
				entity.getAddress(),
				entity.getId()
			) == 0 ?
				new Response<>(null, ResponseType.NOT_UPDATED) :
				new Response<>(entity, ResponseType.UPDATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> delete (Long id) {
		try {
			return (Integer) this.crudUtil.execute("UPDATE customer SET is_deleted = TRUE WHERE is_deleted = FALSE AND id = ?", id) == 0 ?
				new Response<>(false, ResponseType.NOT_DELETED) :
				new Response<>(true, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<CustomerEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT name, phone, email, address FROM customer WHERE is_deleted = FALSE AND id = ?", id)) {
			return resultSet.next() ?
				new Response<>(CustomerEntity.builder()
					.name(resultSet.getString(1))
					.phone(resultSet.getString(2))
					.email(resultSet.getString(3))
					.address(resultSet.getString(4))
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<CustomerEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT id, name, phone, email, address FROM customer WHERE is_deleted = FALSE")) {
			final List<CustomerEntity> customerEntities = new ArrayList<>();

			while (resultSet.next()) customerEntities.add(CustomerEntity.builder()
				.id(resultSet.getLong(1))
				.name(resultSet.getString(2))
				.phone(resultSet.getString(3))
				.email(resultSet.getString(4))
				.address(resultSet.getString(5))
				.build());

			return new Response<>(customerEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> isPhoneExist (String phone) {
		return this.getExistence("SELECT 1 FROM customer WHERE phone = ?", phone);
	}

	@Override
	public Response<Boolean> isPhoneExist (String phone, Long customerId) {
		return this.getExistence("SELECT 1 FROM customer WHERE id != ? AND phone = ?", customerId, phone);
	}

	@Override
	public Response<Boolean> isEmailExist (String email) {
		return this.getExistence("SELECT 1 FROM customer WHERE email = ?", email);
	}

	@Override
	public Response<Boolean> isEmailExist (String email, Long customerId) {
		return this.getExistence("SELECT 1 FROM customer WHERE id != ? AND email = ?", customerId, email);
	}
}
