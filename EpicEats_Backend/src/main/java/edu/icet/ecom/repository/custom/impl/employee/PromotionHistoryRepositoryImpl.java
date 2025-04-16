package edu.icet.ecom.repository.custom.impl.employee;

import edu.icet.ecom.entity.employee.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PromotionHistoryRepositoryImpl implements PromotionHistoryRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;
	private final EmployeeRepository employeeRepository;

	public PromotionHistoryRepositoryImpl (Logger logger, CrudUtil crudUtil, @Lazy EmployeeRepository employeeRepository) {
		this.logger = logger;
		this.crudUtil = crudUtil;
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Response<PromotionHistoryEntity> add (PromotionHistoryEntity entity) {
		try {
			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"""
				INSERT INTO promotion_history (employee_id, old_role, new_role, promotion_date)
				VALUES (?, ?, ?, ?)
				""",
				entity.getEmployee().getId(),
				entity.getOldRole().name(),
				entity.getNewRole().name(),
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
	public Response<PromotionHistoryEntity> update (PromotionHistoryEntity entity) {
		return new Response<>(null, ResponseType.SERVER_ERROR);
	}

	@Override
	public Response<PromotionHistoryEntity> update (PromotionHistoryLiteEntity entity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final boolean isUpdated = (Integer) this.crudUtil.execute(
				"""
					UPDATE promotion_history
					SET employee_id = ?, old_role = ?, new_role = ?, promotion_date = ?
					WHERE id = ?
					""",
				entity.getEmployeeId(),
				entity.getOldRole().name(),
				entity.getNewRole().name(),
				entity.getPromotionDate() == null ? DateTimeUtil.getCurrentDate() : entity.getPromotionDate(),
				entity.getId()
			) != 0;

			if (!isUpdated) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			final Response<EmployeeEntity> employeGetResponse = this.employeeRepository.get(entity.getEmployeeId());

			if (employeGetResponse.getStatus() == ResponseType.SERVER_ERROR) {
				connection.rollback();
				return new Response<>(null, employeGetResponse.getStatus());
			}

			connection.commit();

			return new Response<>(PromotionHistoryEntity.builder()
					.id(entity.getId())
					.employee(employeGetResponse.getData())
					.oldRole(entity.getOldRole())
					.newRole(entity.getNewRole())
					.promotionDate(entity.getPromotionDate())
					.build(), ResponseType.UPDATED);
		} catch (SQLException exception) {
			try {
				connection.rollback();
			} catch (SQLException rollbackException) {
				this.logger.error(rollbackException.getMessage());
			}

			this.logger.error(exception.getMessage());
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
	public Response<AllPromotionsEntity> getAllStructured () {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT promotion_history.id, promotion_history.old_role, promotion_history.new_role, promotion_history.promotion_date,
			employee.id, employee.name, employee.phone, employee.email, employee.address, employee.salary, employee.role, employee.dob, employee.employee_since
			FROM promotion_history JOIN employee ON employee.id = promotion_history.employee_id
			WHERE promotion_history.is_deleted = FALSE AND employee.is_terminated = FALSE
			""")) {
			final Map<Long, EmployeeEntity> employeeMap = new HashMap<>();
			final List<PromotionHistoryLiteEntity> promotionHistoryLiteEntities = new ArrayList<>();

			while (resultSet.next()) {
				final long employeeId = resultSet.getLong(5);

				promotionHistoryLiteEntities.add(PromotionHistoryLiteEntity.builder()
					.id(resultSet.getLong(1))
					.oldRole(EmployeeRole.fromName(resultSet.getString(2)))
					.newRole(EmployeeRole.fromName(resultSet.getString(3)))
					.promotionDate(DateTimeUtil.parseDate(resultSet.getString(4)))
					.employeeId(employeeId)
					.build());

				employeeMap.putIfAbsent(employeeId, EmployeeEntity.builder()
					.id(employeeId)
					.name(resultSet.getString(6))
					.phone(resultSet.getString(7))
					.email(resultSet.getString(8))
					.address(resultSet.getString(9))
					.salary(resultSet.getDouble(10))
					.role(EmployeeRole.fromName(resultSet.getString(11)))
					.dob(DateTimeUtil.parseDate(resultSet.getString(12)))
					.employeeSince(DateTimeUtil.parseDate(resultSet.getString(13)))
					.build());
			}

			return new Response<>(AllPromotionsEntity.builder()
				.employees(employeeMap.values().stream().toList())
				.promotions(promotionHistoryLiteEntities)
				.build(), ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Object> delete (Long id) {
		try {
			return (Integer) this.crudUtil.execute("""
				UPDATE promotion_history
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
	public Response<PromotionHistoryEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT
			promotion_history.old_role, promotion_history.new_role, promotion_history.promotion_date,
			employee.id, employee.name, employee.phone, employee.email, employee.address, employee.salary, employee.role, employee.dob, employee.employee_since
			FROM promotion_history
			JOIN employee ON employee.id = promotion_history.employee_id
			WHERE promotion_history.is_deleted = FALSE AND employee.is_terminated = FALSE AND promotion_history.id = ?
			""", id)) {
			return resultSet.next() ?
				new Response<>(PromotionHistoryEntity.builder()
					.id(id)
					.oldRole(EmployeeRole.fromName(resultSet.getString(1)))
					.newRole(EmployeeRole.fromName(resultSet.getString(2)))
					.promotionDate(DateTimeUtil.parseDate(resultSet.getString(3)))
					.employee(EmployeeEntity.builder()
						.id(resultSet.getLong(4))
						.name(resultSet.getString(5))
						.phone(resultSet.getString(6))
						.email(resultSet.getString(7))
						.address(resultSet.getString(8))
						.salary(resultSet.getDouble(9))
						.role(EmployeeRole.fromName(resultSet.getString(10)))
						.dob(DateTimeUtil.parseDate(resultSet.getString(11)))
						.employeeSince(DateTimeUtil.parseDate(resultSet.getString(12)))
						.build())
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<PromotionHistoryEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT
			promotion_history.id, promotion_history.old_role, promotion_history.new_role, promotion_history.promotion_date,
			employee.id, employee.name, employee.phone, employee.email, employee.address, employee.salary, employee.role, employee.dob, employee.employee_since
			FROM promotion_history
			JOIN employee ON employee.id = promotion_history.employee_id
			WHERE promotion_history.is_deleted = FALSE AND employee.is_terminated = FALSE
			""")) {
			final List<PromotionHistoryEntity> promotionHistoryEntities = new ArrayList<>();

			while (resultSet.next()) promotionHistoryEntities.add(PromotionHistoryEntity.builder()
				.id(resultSet.getLong(1))
				.oldRole(EmployeeRole.fromName(resultSet.getString(2)))
				.newRole(EmployeeRole.fromName(resultSet.getString(3)))
				.promotionDate(DateTimeUtil.parseDate(resultSet.getString(4)))
				.employee(EmployeeEntity.builder()
					.id(resultSet.getLong(5))
					.name(resultSet.getString(6))
					.phone(resultSet.getString(7))
					.email(resultSet.getString(8))
					.address(resultSet.getString(9))
					.salary(resultSet.getDouble(10))
					.role(EmployeeRole.fromName(resultSet.getString(11)))
					.dob(DateTimeUtil.parseDate(resultSet.getString(12)))
					.employeeSince(DateTimeUtil.parseDate(resultSet.getString(13)))
					.build())
				.build());

			return new Response<>(promotionHistoryEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Object> deleteByEmployeeId (Long employeeId) {
		try {
			return (Integer) this.crudUtil.execute("""
				UPDATE promotion_history
				SET is_deleted = TRUE
				WHERE is_deleted = FALSE AND employee_id = ?
				""", employeeId) == 0 ?
				new Response<>(null, ResponseType.NOT_DELETED) :
				new Response<>(null, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<PromotionHistoryEntity>> getAllByEmployeeId (Long employeeId) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT id, old_role, new_role, promotion_date FROM promotion_history
			WHERE is_deleted = FALSE AND employee_id = ?
			""", employeeId)) {
			final List<PromotionHistoryEntity> promotionHistoryEntities = new ArrayList<>();

			while (resultSet.next()) promotionHistoryEntities.add(PromotionHistoryEntity.builder()
				.id(resultSet.getLong(1))
				.oldRole(EmployeeRole.fromName(resultSet.getString(2)))
				.newRole(EmployeeRole.fromName(resultSet.getString(3)))
				.promotionDate(DateTimeUtil.parseDate(resultSet.getString(4)))
				.build());

			return new Response<>(promotionHistoryEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
