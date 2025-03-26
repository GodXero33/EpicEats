package edu.icet.ecom.repository.custom.impl.employee;

import edu.icet.ecom.entity.employee.PromotionHistoryEntity;
import edu.icet.ecom.repository.custom.employee.PromotionHistoryRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.DateTimeUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.EmployeeRole;
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
public class PromotionHistoryRepositoryImpl implements PromotionHistoryRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	@Override
	public Response<PromotionHistoryEntity> add (PromotionHistoryEntity entity) {
		try {
			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"INSERT INTO promotion_history (employee_id, old_role, new_role, promotion_date) VALUES (?, ?, ?, ?)",
				entity.getEmployeeId(),
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
		try {
			final boolean isUpdated = (Integer) this.crudUtil.execute(
				"UPDATE promotion_history SET employee_id = ?, old_role = ?, new_role = ?, promotion_date = ? WHERE id = ?",
				entity.getEmployeeId(),
				entity.getOldRole().name(),
				entity.getNewRole().name(),
				entity.getPromotionDate() == null ? DateTimeUtil.getCurrentDate() : entity.getPromotionDate(),
				entity.getId()
			) != 0;

			return isUpdated ?
				new Response<>(entity, ResponseType.UPDATED) :
				new Response<>(null, ResponseType.NOT_UPDATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> delete (Long id) {
		try {
			return (Integer) this.crudUtil.execute("UPDATE promotion_history SET is_deleted = TRUE WHERE is_deleted = FALSE AND id = ?", id) == 0 ?
				new Response<>(false, ResponseType.NOT_DELETED) :
				new Response<>(true, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<PromotionHistoryEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT employee_id, old_role, new_role, promotion_date FROM promotion_history WHERE is_deleted = FALSE AND id = ?", id)) {
			return resultSet.next() ?
				new Response<>(PromotionHistoryEntity.builder()
					.id(id)
					.employeeId(resultSet.getLong(1))
					.oldRole(EmployeeRole.fromName(resultSet.getString(2)))
					.newRole(EmployeeRole.fromName(resultSet.getString(3)))
					.promotionDate(DateTimeUtil.parseDate(resultSet.getString(4)))
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<PromotionHistoryEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT id, employee_id, old_role, new_role, promotion_date FROM promotion_history WHERE is_deleted = FALSE")) {
			final List<PromotionHistoryEntity> promotionHistoryEntities = new ArrayList<>();

			while (resultSet.next()) promotionHistoryEntities.add(PromotionHistoryEntity.builder()
					.id(resultSet.getLong(1))
					.employeeId(resultSet.getLong(2))
					.oldRole(EmployeeRole.fromName(resultSet.getString(3)))
					.newRole(EmployeeRole.fromName(resultSet.getString(4)))
					.promotionDate(DateTimeUtil.parseDate(resultSet.getString(5)))
					.build());

			return new Response<>(promotionHistoryEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> deleteByEmployeeId (Long employeeId) {
		try {
			return (Integer) this.crudUtil.execute("UPDATE promotion_history SET is_deleted = TRUE WHERE is_deleted = FALSE AND employee_id = ?", employeeId) == 0 ?
				new Response<>(false, ResponseType.NOT_DELETED) :
				new Response<>(true, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<PromotionHistoryEntity>> getAllByEmployeeId (Long employeeId) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT id, old_role, new_role, promotion_date FROM promotion_history WHERE is_deleted = FALSE AND employee_id = ?", employeeId)) {
			final List<PromotionHistoryEntity> promotionHistoryEntities = new ArrayList<>();

			while (resultSet.next()) promotionHistoryEntities.add(PromotionHistoryEntity.builder()
				.id(resultSet.getLong(1))
				.employeeId(employeeId)
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
