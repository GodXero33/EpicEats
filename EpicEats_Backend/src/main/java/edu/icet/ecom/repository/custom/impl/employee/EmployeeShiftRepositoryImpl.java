package edu.icet.ecom.repository.custom.impl.employee;

import edu.icet.ecom.entity.employee.EmployeeShiftEntity;
import edu.icet.ecom.repository.custom.employee.EmployeeShiftRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.DateTimeUtil;
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
public class EmployeeShiftRepositoryImpl implements EmployeeShiftRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	@Override
	public Response<EmployeeShiftEntity> add (EmployeeShiftEntity entity) {
		try {
			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"INSERT INTO employee_shift (employee_id, shift_date, start_time, end_time) VALUES (?, ?, ?, ?)",
				entity.getEmployeeId(),
				entity.getShiftDate(),
				entity.getStartTime(),
				entity.getEndTime()
			);

			entity.setId(generatedId);

			return new Response<>(entity, ResponseType.CREATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<EmployeeShiftEntity> update (EmployeeShiftEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"UPDATE employee_shift SET employee_id = ?, shift_date = ?, start_time = ?, end_time = ? WHERE is_deleted = FALSE AND id = ?",
				entity.getEmployeeId(),
				entity.getShiftDate(),
				entity.getStartTime(),
				entity.getEndTime(),
				entity.getId()) == 0 ?
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
			return (Integer) this.crudUtil.execute("UPDATE employee_shift SET is_deleted = TRUE WHERE is_deleted = FALSE AND id = ?", id) == 0 ?
				new Response<>(false, ResponseType.NOT_DELETED) :
				new Response<>(true, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<EmployeeShiftEntity> get (Long id) {
		try {
			final ResultSet resultSet = this.crudUtil.execute("SELECT employee_id, shift_date, start_time, end_time FROM employee_shift WHERE is_deleted = FALSE AND id = ?", id);

			return resultSet.next() ?
				new Response<>(EmployeeShiftEntity.builder()
					.id(id)
					.employeeId(resultSet.getLong(1))
					.shiftDate(DateTimeUtil.parseDate(resultSet.getString(2)))
					.startTime(DateTimeUtil.parseTime(resultSet.getString(3)))
					.endTime(DateTimeUtil.parseTime(resultSet.getString(4)))
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<EmployeeShiftEntity>> getAll () {
		try {
			final List<EmployeeShiftEntity> employeeShiftEntities = new ArrayList<>();
			final ResultSet resultSet = this.crudUtil.execute("SELECT id, employee_id, shift_date, start_time, end_time FROM employee_shift WHERE is_deleted = FALSE");

			while (resultSet.next()) employeeShiftEntities.add(EmployeeShiftEntity.builder()
				.id(resultSet.getLong(1))
				.employeeId(resultSet.getLong(2))
				.shiftDate(DateTimeUtil.parseDate(resultSet.getString(3)))
				.startTime(DateTimeUtil.parseTime(resultSet.getString(4)))
				.endTime(DateTimeUtil.parseTime(resultSet.getString(5)))
				.build());

			return new Response<>(employeeShiftEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<EmployeeShiftEntity>> getAllByEmployeeId (Long employeeId) {
		try {
			final List<EmployeeShiftEntity> employeeShiftEntities = new ArrayList<>();
			final ResultSet resultSet = this.crudUtil.execute("SELECT id, employee_id, shift_date, start_time, end_time FROM employee_shift WHERE is_deleted = FALSE AND employee_id = ?", employeeId);

			while (resultSet.next()) employeeShiftEntities.add(EmployeeShiftEntity.builder()
				.id(resultSet.getLong(1))
				.employeeId(employeeId)
				.shiftDate(DateTimeUtil.parseDate(resultSet.getString(3)))
				.startTime(DateTimeUtil.parseTime(resultSet.getString(4)))
				.endTime(DateTimeUtil.parseTime(resultSet.getString(5)))
				.build());

			return new Response<>(employeeShiftEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> deletedByEmployeeId (Long employeeId) {
		try {
			return (Integer) this.crudUtil.execute("UPDATE employee_shift SET is_deleted = TRUE WHERE is_deleted = FALSE AND employee_id = ?", employeeId) == 0 ?
				new Response<>(false, ResponseType.NOT_DELETED) :
				new Response<>(true, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
