package edu.icet.ecom.repository.custom.impl.finance;

import edu.icet.ecom.entity.finance.ReportEntity;
import edu.icet.ecom.repository.custom.finance.ReportRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.DateTimeUtil;
import edu.icet.ecom.util.Response;
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
				"INSERT INTO report (report_type, start_date, end_date, generated_by, title, description) VALUES (?, ?, ?, ?, ?, ?, ?)",
				entity.getStartDate(),
				entity.getEndDate(),
				entity.getGeneratedBy(),
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
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<ReportEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT generated_at, report_type, start_date, end_date, generated_by, title, description FROM report WHERE is_deleted = FALSE AND id = ?", id)) {
			return resultSet.next() ?
				new Response<>(ReportEntity.builder()
					.id(id)
					.generatedAt(DateTimeUtil.parseDateTime(resultSet.getString(1)))
					.type(ReportType.fromName(resultSet.getString(2)))
					.startDate(DateTimeUtil.parseDate(resultSet.getString(3)))
					.endDate(DateTimeUtil.parseDate(resultSet.getString(4)))
					.generatedBy(resultSet.getLong(5))
					.title(resultSet.getString(6))
					.description(resultSet.getString(7))
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<ReportEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT id, generated_at, report_type, start_date, end_date, generated_by, title, description FROM report WHERE is_deleted = FALSE")) {
			final List<ReportEntity> reportEntities = new ArrayList<>();

			while (resultSet.next()) reportEntities.add(ReportEntity.builder()
				.id(resultSet.getLong(1))
				.generatedAt(DateTimeUtil.parseDateTime(resultSet.getString(2)))
				.type(ReportType.fromName(resultSet.getString(3)))
				.startDate(DateTimeUtil.parseDate(resultSet.getString(4)))
				.endDate(DateTimeUtil.parseDate(resultSet.getString(5)))
				.generatedBy(resultSet.getLong(6))
				.title(resultSet.getString(7))
				.description(resultSet.getString(8))
				.build());

			return new Response<>(reportEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> deleteByEmployeeId (Long employeeId) {
		return null;
	}
}
