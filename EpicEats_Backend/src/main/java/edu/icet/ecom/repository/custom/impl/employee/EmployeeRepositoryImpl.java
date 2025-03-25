package edu.icet.ecom.repository.custom.impl.employee;

import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.repository.custom.employee.EmployeeRepository;
import edu.icet.ecom.repository.custom.security.UserRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.DateTimeUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.EmployeeRole;
import edu.icet.ecom.util.enumaration.ResponseType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;
	private final UserRepository userRepository;

	@Override
	public Response<EmployeeEntity> add (EmployeeEntity entity) {
		try {
			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"INSERT INTO employee (name, phone, email, address, salary, role, dob, employee_since) VALUES (?, ?, ?, ?, ?, ?, ?)",
				entity.getName(),
				entity.getPhone(),
				entity.getEmail(),
				entity.getAddress(),
				entity.getSalary(),
				entity.getRole().name(),
				entity.getDob(),
				DateTimeUtil.getCurrentDate()
			);

			entity.setId(generatedId);

			return new Response<>(entity, ResponseType.CREATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<EmployeeEntity> update (EmployeeEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"UPDATE employee SET name = ?, phone = ?, email = ?, address = ?, salary = ?, role = ?, dob = ? WHERE is_deleted = FALSE AND id = ?",
				entity.getName(),
				entity.getPhone(),
				entity.getEmail(),
				entity.getAddress(),
				entity.getSalary(),
				entity.getRole().name(),
				entity.getDob()
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
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final boolean isEmployeeDeleted = (Integer) this.crudUtil.execute("UPDATE employee SET is_deleted = TRUE WHERE is_deleted = FALSE AND id = ?", id) != 0;
			final boolean isUserDeleted = isEmployeeDeleted && this.userRepository.delete(id).getStatus() != ResponseType.SERVER_ERROR; // If employee deleted and user deleted. consider user not deleted only when server error happens because employee may not be a user.

			if (isUserDeleted) connection.commit();

			return new Response<>(isUserDeleted, isUserDeleted ? ResponseType.DELETED : ResponseType.NOT_DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());

			try {
				connection.rollback();
			} catch (SQLException rollbackException) {
				this.logger.error(rollbackException.getMessage());
			}

			return new Response<>(null, ResponseType.SERVER_ERROR);
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException exception) {
				this.logger.error(exception.getMessage());
			}
		}
	}

	@Override
	public Response<EmployeeEntity> get (Long id) {
		try {
			final ResultSet resultSet = this.crudUtil.execute("SELECT name, phone, email, address, role, dob, employee_since FROM employee WHERE is_deleted = FALSE AND id = ?", id);

			return resultSet.next() ?
				new Response<>(EmployeeEntity.builder()
					.id(id)
					.name(resultSet.getString(1))
					.phone(resultSet.getString(2))
					.email(resultSet.getString(3))
					.address(resultSet.getString(4))
					.role(EmployeeRole.fromName(resultSet.getString(5)))
					.dob(DateTimeUtil.parseDate(resultSet.getString(6)))
					.employeeSince(DateTimeUtil.parseDate(resultSet.getString(7)))
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<EmployeeEntity>> getAll () {
		try {
			final List<EmployeeEntity> employeeEntities = new ArrayList<>();
			final ResultSet resultSet = this.crudUtil.execute("SELECT id, name, phone, email, address, role, dob, employee_since FROM employee WHERE is_deleted = FALSE");

			while (resultSet.next()) employeeEntities.add(EmployeeEntity.builder()
				.id(resultSet.getLong(1))
				.name(resultSet.getString(2))
				.phone(resultSet.getString(3))
				.email(resultSet.getString(4))
				.address(resultSet.getString(5))
				.role(EmployeeRole.fromName(resultSet.getString(6)))
				.dob(DateTimeUtil.parseDate(resultSet.getString(7)))
				.employeeSince(DateTimeUtil.parseDate(resultSet.getString(8)))
				.build());

			return new Response<>(employeeEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
