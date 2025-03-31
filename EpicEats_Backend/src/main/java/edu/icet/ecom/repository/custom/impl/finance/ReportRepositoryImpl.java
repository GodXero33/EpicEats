package edu.icet.ecom.repository.custom.impl.finance;

import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.entity.finance.ReportEntity;
import edu.icet.ecom.repository.custom.finance.ReportRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.DateTimeUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.EmployeeRole;
import edu.icet.ecom.util.enumaration.ReportType;
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
public class ReportRepositoryImpl implements ReportRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	@Override
	public Response<ReportEntity> add (ReportEntity entity) {
		try {
			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"INSERT INTO report (report_type, start_date, end_date, generated_by, title, description) VALUES (?, ?, ?, ?, ?, ?)",
				entity.getType().name(),
				entity.getStartDate(),
				entity.getEndDate(),
				entity.getGeneratedBy().getId(),
				entity.getTitle(),
				entity.getDescription()
			);

			entity.setId(generatedId);

			return new Response<>(entity, ResponseType.CREATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<ReportEntity> update (ReportEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"UPDATE report SET report_type = ?, start_date = ?, end_date = ?, generated_by = ?, title = ?, description = ? WHERE is_deleted = FALSE AND id = ?",
				entity.getType().name(),
				entity.getStartDate(),
				entity.getEndDate(),
				entity.getGeneratedBy().getId(),
				entity.getTitle(),
				entity.getDescription(),
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
			return (Integer) this.crudUtil.execute("UPDATE report SET is_deleted = TRUE WHERE is_deleted = FALSE AND id = ?", id) == 0 ?
				new Response<>(null, ResponseType.NOT_DELETED) :
				new Response<>(null, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<ReportEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT r.generated_at, r.report_type, r.start_date, r.end_date, r.title, r.description, e.id FROM report r JOIN employee e ON r.generated_by = e.id WHERE r.is_deleted = FALSE AND e.is_terminated = FALSE AND r.id = ?", id)) {
			return resultSet.next() ?
				new Response<>(ReportEntity.builder()
					.id(id)
					.generatedAt(DateTimeUtil.parseDateTime(resultSet.getString(1)))
					.type(ReportType.fromName(resultSet.getString(2)))
					.startDate(DateTimeUtil.parseDate(resultSet.getString(3)))
					.endDate(DateTimeUtil.parseDate(resultSet.getString(4)))
					.title(resultSet.getString(5))
					.description(resultSet.getString(6))
					.generatedBy(EmployeeEntity.builder().id(resultSet.getLong(7)).build())
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<ReportEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT r.id, r.generated_at, r.report_type, r.start_date, r.end_date, r.title, r.description, e.id FROM report r JOIN employee e ON r.generated_by = e.id WHERE r.is_deleted = FALSE AND e.is_terminated = FALSE")) {
			final List<ReportEntity> reportEntities = new ArrayList<>();

			while (resultSet.next()) reportEntities.add(ReportEntity.builder()
				.id(resultSet.getLong(1))
				.generatedAt(DateTimeUtil.parseDateTime(resultSet.getString(2)))
				.type(ReportType.fromName(resultSet.getString(3)))
				.startDate(DateTimeUtil.parseDate(resultSet.getString(4)))
				.endDate(DateTimeUtil.parseDate(resultSet.getString(5)))
				.title(resultSet.getString(6))
				.description(resultSet.getString(7))
				.generatedBy(EmployeeEntity.builder().id(resultSet.getLong(8)).build())
				.build());

			return new Response<>(reportEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<ReportEntity> getFull (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT r.generated_at, r.report_type, r.start_date, r.end_date, r.title, r.description, e.id, e.name, e.phone, e.email, e.address, e.salary, e.role, e.dob, e.employee_since FROM report r JOIN employee e ON r.generated_by = e.id WHERE r.is_deleted = FALSE AND e.is_terminated = FALSE AND r.id = ?", id)) {
			return resultSet.next() ?
				new Response<>(ReportEntity.builder()
					.id(id)
					.generatedAt(DateTimeUtil.parseDateTime(resultSet.getString(1)))
					.type(ReportType.fromName(resultSet.getString(2)))
					.startDate(DateTimeUtil.parseDate(resultSet.getString(3)))
					.endDate(DateTimeUtil.parseDate(resultSet.getString(4)))
					.title(resultSet.getString(5))
					.description(resultSet.getString(6))
					.generatedBy(EmployeeEntity.builder()
						.id(resultSet.getLong(7))
						.name(resultSet.getString(8))
						.phone(resultSet.getString(9))
						.email(resultSet.getString(10))
						.address(resultSet.getString(11))
						.salary(resultSet.getDouble(12))
						.role(EmployeeRole.fromName(resultSet.getString(13)))
						.dob(DateTimeUtil.parseDate(resultSet.getString(14)))
						.employeeSince(DateTimeUtil.parseDate(resultSet.getString(15)))
						.build())
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<ReportEntity>> getAllFull () {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT r.id, r.generated_at, r.report_type, r.start_date, r.end_date, r.title, r.description, e.id, e.name, e.phone, e.email, e.address, e.salary, e.role, e.dob, e.employee_since FROM report r JOIN employee e ON r.generated_by = e.id WHERE r.is_deleted = FALSE AND e.is_terminated = FALSE")) {
			final List<ReportEntity> reportEntities = new ArrayList<>();

			while (resultSet.next()) reportEntities.add(ReportEntity.builder()
				.id(resultSet.getLong(1))
				.generatedAt(DateTimeUtil.parseDateTime(resultSet.getString(2)))
				.type(ReportType.fromName(resultSet.getString(3)))
				.startDate(DateTimeUtil.parseDate(resultSet.getString(4)))
				.endDate(DateTimeUtil.parseDate(resultSet.getString(5)))
				.title(resultSet.getString(6))
				.description(resultSet.getString(7))
				.generatedBy(EmployeeEntity.builder()
					.id(resultSet.getLong(8))
					.name(resultSet.getString(9))
					.phone(resultSet.getString(10))
					.email(resultSet.getString(11))
					.address(resultSet.getString(12))
					.salary(resultSet.getDouble(13))
					.role(EmployeeRole.fromName(resultSet.getString(14)))
					.dob(DateTimeUtil.parseDate(resultSet.getString(15)))
					.employeeSince(DateTimeUtil.parseDate(resultSet.getString(16)))
					.build())
				.build());

			return new Response<>(reportEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Object> deleteByEmployeeId (Long employeeId) {
		try {
			return (Integer) this.crudUtil.execute("UPDATE report SET is_deleted = TRUE WHERE is_deleted = FALSE AND generated_by = ?", employeeId) == 0 ?
				new Response<>(null, ResponseType.NOT_DELETED) :
				new Response<>(null, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<ReportEntity>> getAllByEmployeeId (Long employeeId) { // If frontend has employee id that means other employee's data also there. No need to send again.
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT id, generated_at, report_type, start_date, end_date, title, description FROM report WHERE is_deleted = FALSE AND generated_by = ?", employeeId)) {
			final List<ReportEntity> reportEntities = new ArrayList<>();

			while (resultSet.next()) reportEntities.add(ReportEntity.builder()
				.id(resultSet.getLong(1))
				.generatedAt(DateTimeUtil.parseDateTime(resultSet.getString(2)))
				.type(ReportType.fromName(resultSet.getString(3)))
				.startDate(DateTimeUtil.parseDate(resultSet.getString(4)))
				.endDate(DateTimeUtil.parseDate(resultSet.getString(5)))
				.title(resultSet.getString(6))
				.description(resultSet.getString(7))
				.build());

			return new Response<>(reportEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
