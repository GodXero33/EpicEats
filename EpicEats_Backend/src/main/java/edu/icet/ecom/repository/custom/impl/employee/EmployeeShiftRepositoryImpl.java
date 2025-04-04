package edu.icet.ecom.repository.custom.impl.employee;

import edu.icet.ecom.entity.employee.AllShiftsEntity;
import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.entity.employee.EmployeeShiftEntity;
import edu.icet.ecom.entity.employee.EmployeeShiftLiteEntity;
import edu.icet.ecom.repository.custom.employee.EmployeeRepository;
import edu.icet.ecom.repository.custom.employee.EmployeeShiftRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class EmployeeShiftRepositoryImpl implements EmployeeShiftRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;
	private final EmployeeRepository employeeRepository;

	@Override
	public Response<EmployeeShiftEntity> add (EmployeeShiftEntity entity) {
		return new Response<>(null, ResponseType.SERVER_ERROR);
	}

	@Override
	public Response<EmployeeShiftEntity> update (EmployeeShiftEntity entity) {
		return new Response<>(null, ResponseType.SERVER_ERROR);
	}

	@Override
	public Response<EmployeeShiftEntity> add (EmployeeShiftLiteEntity entity) {
		try {
			final Response<EmployeeEntity> employeeGetResponse = this.employeeRepository.get(entity.getEmployeeId());

			if (employeeGetResponse.getStatus() == ResponseType.SERVER_ERROR) return new Response<>(null, ResponseType.SERVER_ERROR);
			if (employeeGetResponse.getStatus() == ResponseType.NOT_FOUND) return new Response<>(null, ResponseType.NOT_CREATED);

			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"INSERT INTO employee_shift (employee_id, shift_date, start_time, end_time) VALUES (?, ?, ?, ?)",
				entity.getEmployeeId(),
				entity.getShiftDate(),
				entity.getStartTime(),
				entity.getEndTime()
			);

			return new Response<>(EmployeeShiftEntity.builder()
				.id(generatedId)
				.employee(employeeGetResponse.getData())
				.shiftDate(entity.getShiftDate())
				.startTime(entity.getStartTime())
				.endTime(entity.getEndTime())
				.build(), ResponseType.CREATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<EmployeeShiftEntity> update (EmployeeShiftLiteEntity entity) {
		try {
			final Response<EmployeeEntity> employeeGetResponse = this.employeeRepository.get(entity.getEmployeeId());

			if (employeeGetResponse.getStatus() == ResponseType.SERVER_ERROR) return new Response<>(null, ResponseType.SERVER_ERROR);
			if (employeeGetResponse.getStatus() == ResponseType.NOT_FOUND) return new Response<>(null, ResponseType.NOT_CREATED);

			return (Integer) this.crudUtil.execute(
				"UPDATE employee_shift SET employee_id = ?, shift_date = ?, start_time = ?, end_time = ? WHERE is_deleted = FALSE AND id = ?",
				entity.getEmployeeId(),
				entity.getShiftDate(),
				entity.getStartTime(),
				entity.getEndTime(),
				entity.getId()) == 0 ?
				new Response<>(null, ResponseType.NOT_UPDATED) :
				new Response<>(EmployeeShiftEntity.builder()
					.id(entity.getId())
					.employee(employeeGetResponse.getData())
					.shiftDate(entity.getShiftDate())
					.startTime(entity.getStartTime())
					.endTime(entity.getEndTime())
					.build(), ResponseType.UPDATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<AllShiftsEntity> getAllStructured () {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT employee_shift.id, employee_shift.shift_date, employee_shift.start_time, employee_shift.end_time,
			employee.id, employee.name, employee.phone, employee.email, employee.address, employee.salary, employee.role, employee.dob, employee.employee_since
			FROM employee_shift
			JOIN employee ON employee.id = employee_shift.employee_id
			WHERE employee_shift.is_deleted = FALSE AND employee.is_terminated = FALSE
			""")) {
			final Map<Long, EmployeeEntity> employeeMap = new HashMap<>();
			final List<EmployeeShiftLiteEntity> shiftLiteEntities = new ArrayList<>();

			while (resultSet.next()) {
				final long employeeId = resultSet.getLong(5);

				shiftLiteEntities.add(EmployeeShiftLiteEntity.builder()
					.id(resultSet.getLong(1))
					.shiftDate(DateTimeUtil.parseDate(resultSet.getString(2)))
					.startTime(DateTimeUtil.parseTime(resultSet.getString(3)))
					.endTime(DateTimeUtil.parseTime(resultSet.getString(4)))
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

			return new Response<>(AllShiftsEntity.builder()
				.employees(employeeMap.values().stream().toList())
				.shifts(shiftLiteEntities)
				.build(), ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Object> delete (Long id) {
		try {
			return (Integer) this.crudUtil.execute("UPDATE employee_shift SET is_deleted = TRUE WHERE is_deleted = FALSE AND id = ?", id) == 0 ?
				new Response<>(null, ResponseType.NOT_DELETED) :
				new Response<>(null, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<EmployeeShiftEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT es.shift_date, es.start_time, es.end_time, e.id, e.name, e.phone, e.email, e.address, e.salary, e.role, e.dob, e.employee_since FROM employee_shift es JOIN employee e ON e.id = es.employee_id WHERE es.id = ? AND es.is_deleted = FALSE AND e.is_terminated = FALSE", id)) {
			return resultSet.next() ?
				new Response<>(EmployeeShiftEntity.builder()
					.id(id)
					.shiftDate(DateTimeUtil.parseDate(resultSet.getString(1)))
					.startTime(DateTimeUtil.parseTime(resultSet.getString(2)))
					.endTime(DateTimeUtil.parseTime(resultSet.getString(3)))
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
	public Response<List<EmployeeShiftEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT es.id, es.shift_date, es.start_time, es.end_time, e.id, e.name, e.phone, e.email, e.address, e.salary, e.role, e.dob, e.employee_since FROM employee_shift es JOIN employee e ON e.id = es.employee_id WHERE es.is_deleted = FALSE AND e.is_terminated = FALSE")) {
			final List<EmployeeShiftEntity> employeeShiftEntities = new ArrayList<>();

			while (resultSet.next()) employeeShiftEntities.add(EmployeeShiftEntity.builder()
				.id(resultSet.getLong(1))
				.shiftDate(DateTimeUtil.parseDate(resultSet.getString(2)))
				.startTime(DateTimeUtil.parseTime(resultSet.getString(3)))
				.endTime(DateTimeUtil.parseTime(resultSet.getString(4)))
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

			return new Response<>(employeeShiftEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	/**
	 * No need to send employee data. If frontend has employee id that means frontend also have employee data.
	 */
	@Override
	public Response<List<EmployeeShiftEntity>> getAllByEmployeeId (Long employeeId) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT id, employee_id, shift_date, start_time, end_time FROM employee_shift WHERE is_deleted = FALSE AND employee_id = ?", employeeId)) {
			final List<EmployeeShiftEntity> employeeShiftEntities = new ArrayList<>();

			while (resultSet.next()) employeeShiftEntities.add(EmployeeShiftEntity.builder()
				.id(resultSet.getLong(1))
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
	public Response<Object> deletedByEmployeeId (Long employeeId) {
		try {
			return (Integer) this.crudUtil.execute("UPDATE employee_shift SET is_deleted = TRUE WHERE is_deleted = FALSE AND employee_id = ?", employeeId) == 0 ?
				new Response<>(null, ResponseType.NOT_DELETED) :
				new Response<>(null, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
