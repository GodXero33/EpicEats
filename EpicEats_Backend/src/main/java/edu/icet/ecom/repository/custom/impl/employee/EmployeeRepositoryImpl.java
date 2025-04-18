package edu.icet.ecom.repository.custom.impl.employee;

import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.entity.employee.PromotionHistoryEntity;
import edu.icet.ecom.repository.custom.employee.EmployeeRepository;
import edu.icet.ecom.repository.custom.employee.PromotionHistoryRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.DateTimeUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enums.EmployeeRole;
import edu.icet.ecom.util.enums.ResponseType;
import org.slf4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;
	private final PromotionHistoryRepository promotionHistoryRepository;

	public EmployeeRepositoryImpl (Logger logger, CrudUtil crudUtil, @Lazy PromotionHistoryRepository promotionHistoryRepository) {
		this.logger = logger;
		this.crudUtil = crudUtil;
		this.promotionHistoryRepository = promotionHistoryRepository;
	}

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
	public Response<EmployeeEntity> add (EmployeeEntity entity) {
		try {
			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"""
				INSERT INTO employee (name, phone, email, address, salary, role, dob, employee_since)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?)
				""",
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
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try (final ResultSet resultSet = this.crudUtil.execute("SELECT role FROM employee WHERE is_terminated = FALSE AND id = ?", entity.getId())) {
			connection.setAutoCommit(false);

			if (!resultSet.next()) return new Response<>(null, ResponseType.NOT_UPDATED);

			final EmployeeRole oldRole = EmployeeRole.fromName(resultSet.getString(1));

			if (!oldRole.equals(entity.getRole())) {
				Response<PromotionHistoryEntity> response = this.promotionHistoryRepository.add(PromotionHistoryEntity.builder()
					.employee(entity)
					.promotionDate(DateTimeUtil.parseDate(DateTimeUtil.getCurrentDate()))
					.oldRole(oldRole)
					.newRole(entity.getRole())
					.build());

				if (response.getStatus() == ResponseType.SERVER_ERROR) {
					connection.rollback();
					this.logger.error("Failed to add new record to promotion history");
					return new Response<>(null, response.getStatus());
				}

				if (response.getStatus() == ResponseType.NOT_UPDATED) {
					connection.rollback();
					return new Response<>(null, response.getStatus());
				}
			}

			if ((Integer) this.crudUtil.execute(
				"""
				UPDATE employee
				SET name = ?, phone = ?, email = ?, address = ?, salary = ?, role = ?, dob = ?
				WHERE is_terminated = FALSE AND id = ?
				""",
				entity.getName(),
				entity.getPhone(),
				entity.getEmail(),
				entity.getAddress(),
				entity.getSalary(),
				entity.getRole().name(),
				entity.getDob(),
				entity.getId()
			) != 0) {
				connection.commit();
				return new Response<>(entity, ResponseType.UPDATED);
			}

			connection.rollback();

			return new Response<>(null, ResponseType.NOT_UPDATED);
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
	public Response<Object> delete (Long id) {
		return null;
	}

	@Override
	public Response<EmployeeEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT name, phone, email, address, salary, role, dob, employee_since
			FROM employee
			WHERE is_terminated = FALSE AND id = ?
			""", id)) {
			return resultSet.next() ?
				new Response<>(EmployeeEntity.builder()
					.id(id)
					.name(resultSet.getString(1))
					.phone(resultSet.getString(2))
					.email(resultSet.getString(3))
					.address(resultSet.getString(4))
					.salary(resultSet.getDouble(5))
					.role(EmployeeRole.fromName(resultSet.getString(6)))
					.dob(DateTimeUtil.parseDate(resultSet.getString(7)))
					.employeeSince(DateTimeUtil.parseDate(resultSet.getString(8)))
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<EmployeeEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT id, name, phone, email, address, salary, role, dob, employee_since
			FROM employee
			WHERE is_terminated = FALSE
			""")) {
			final List<EmployeeEntity> employeeEntities = new ArrayList<>();

			while (resultSet.next()) employeeEntities.add(EmployeeEntity.builder()
				.id(resultSet.getLong(1))
				.name(resultSet.getString(2))
				.phone(resultSet.getString(3))
				.email(resultSet.getString(4))
				.address(resultSet.getString(5))
				.salary(resultSet.getDouble(6))
				.role(EmployeeRole.fromName(resultSet.getString(7)))
				.dob(DateTimeUtil.parseDate(resultSet.getString(8)))
				.employeeSince(DateTimeUtil.parseDate(resultSet.getString(9)))
				.build());

			return new Response<>(employeeEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Object> terminate (Long employeeId) {
		try {
			return (Integer) this.crudUtil.execute("""
				UPDATE employee
				LEFT JOIN employee_shift ON employee.id = employee_shift.employee_id
				SET employee.is_terminated = TRUE, employee_shift.is_deleted = TRUE
				WHERE employee.is_terminated = FALSE AND employee.id = ?
				""", employeeId) == 0 ?
				new Response<>(null, ResponseType.FAILED) :
				new Response<>(null, ResponseType.SUCCESS);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> isExist (Long employeeId) {
		return this.getExistence("SELECT 1 FROM employee WHERE is_terminated = FALSE AND id = ?", employeeId);
	}

	@Override
	public Response<Boolean> isPhoneExist (String phone) {
		return this.getExistence("SELECT 1 FROM employee WHERE phone = ?", phone);
	}

	@Override
	public Response<Boolean> isPhoneExist (String phone, Long employeeId) {
		return this.getExistence("SELECT 1 FROM employee WHERE id != ? AND phone = ?", employeeId, phone);
	}

	@Override
	public Response<Boolean> isEmailExist (String email) {
		return this.getExistence("SELECT 1 FROM employee WHERE email = ?", email);
	}

	@Override
	public Response<Boolean> isEmailExist (String email, Long employeeId) {
		return this.getExistence("SELECT 1 FROM employee WHERE id != ? AND email = ?", employeeId, email);
	}

	@Override
	public Response<List<EmployeeEntity>> getAllByIDs (List<Long> ids) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT id, name, phone, email, address, salary, role, dob, employee_since
			FROM employee
			WHERE is_terminated = FALSE AND id IN (%s)
			""".formatted(String.join(", ", Collections.nCopies(ids.size(), "?"))),
			ids
		)) {
			final List<EmployeeEntity> employeeEntities = new ArrayList<>();

			while (resultSet.next()) employeeEntities.add(EmployeeEntity.builder()
				.id(resultSet.getLong(1))
				.name(resultSet.getString(2))
				.phone(resultSet.getString(3))
				.email(resultSet.getString(4))
				.address(resultSet.getString(5))
				.salary(resultSet.getDouble(6))
				.role(EmployeeRole.fromName(resultSet.getString(7)))
				.dob(DateTimeUtil.parseDate(resultSet.getString(8)))
				.employeeSince(DateTimeUtil.parseDate(resultSet.getString(9)))
				.build());

			return new Response<>(employeeEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
