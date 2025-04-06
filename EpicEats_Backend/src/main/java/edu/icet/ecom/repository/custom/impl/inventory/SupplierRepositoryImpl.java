package edu.icet.ecom.repository.custom.impl.inventory;

import edu.icet.ecom.entity.inventory.SupplierEntity;
import edu.icet.ecom.repository.custom.inventory.SupplierRepository;
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
public class SupplierRepositoryImpl implements SupplierRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	private Response<Boolean> getExistence (String query, Object ...binds) {
		try (final ResultSet resultSet = this.crudUtil.execute(query, binds)) {
			return resultSet.next() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<SupplierEntity> add (SupplierEntity entity) {
		try {
			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"""
					INSERT INTO supplier (name, phone, email, address)
					VALUES (?, ?, ?, ?)
					""",
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
	public Response<SupplierEntity> update (SupplierEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"""
				UPDATE supplier
				SET name = ?, phone = ?, email = ?, address = ?
				WHERE is_deleted = FALSE AND id = ?
				""",
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
	public Response<Object> delete (Long id) {
		try {
			return (Integer) this.crudUtil.execute("""
				UPDATE supplier
				SET is_deleted = TRUE
				WHERE is_deleted = FALSE AND id = ?
				""", id) == 0 ?
				new Response<>(null, ResponseType.NOT_DELETED) :
				new Response<>(null, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<SupplierEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT name, phone, email, address
			FROM supplier
			WHERE is_deleted = FALSE AND id = ?
			""", id)) {
			return resultSet.next() ?
				new Response<>(SupplierEntity.builder()
					.id(id)
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
	public Response<List<SupplierEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT id, name, phone, email, address
			FROM supplier
			WHERE is_deleted = FALSE
			""")) {
			final List<SupplierEntity> supplierEntities = new ArrayList<>();

			while (resultSet.next()) supplierEntities.add(SupplierEntity.builder()
				.id(resultSet.getLong(1))
				.name(resultSet.getString(2))
				.phone(resultSet.getString(3))
				.email(resultSet.getString(4))
				.address(resultSet.getString(5))
				.build());

			return new Response<>(supplierEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> isExist (Long id) {
		return this.getExistence("SELECT 1 FROM supplier WHERE is_deleted = FALSE AND id = ?", id);
	}

	@Override
	public Response<Boolean> isPhoneExist (String phone) {
		return this.getExistence("SELECT 1 FROM supplier WHERE phone = ?", phone);
	}

	@Override
	public Response<Boolean> isPhoneExist (String phone, Long supplierId) {
		return this.getExistence("SELECT 1 FROM supplier WHERE id != ? AND phone = ?", supplierId, phone);
	}

	@Override
	public Response<Boolean> isEmailExist (String email) {
		return this.getExistence("SELECT 1 FROM supplier WHERE email = ?", email);
	}

	@Override
	public Response<Boolean> isEmailExist (String email, Long supplierId) {
		return this.getExistence("SELECT 1 FROM supplier WHERE id != ? AND email = ?", supplierId, email);
	}
}
