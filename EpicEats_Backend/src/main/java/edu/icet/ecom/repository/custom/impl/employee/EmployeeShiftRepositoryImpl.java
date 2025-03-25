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
		return null;
	}

	@Override
	public Response<EmployeeShiftEntity> update (EmployeeShiftEntity entity) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<EmployeeShiftEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<EmployeeShiftEntity>> getAll () {
		return null;
	}

	@Override
	public Response<List<EmployeeShiftEntity>> getAllByEmployeeId (Long employeeId) {
		try {
			final List<EmployeeShiftEntity> employeeShiftEntities = new ArrayList<>();
			final ResultSet resultSet = this.crudUtil.execute("SELECT id, employee_id, shift_date, start_time, end_time FROM employee_shift WHERE is_deleted = FALSE AND employee_id = ?", employeeId);

			while (resultSet.next()) employeeShiftEntities.add(EmployeeShiftEntity.builder()
				.id(resultSet.getLong(1))
				.employeeId(employeeId)
				.shiftDate(DateTimeUtil.parseDate(resultSet.getString(2)))
				.startTime(DateTimeUtil.parseTime(resultSet.getString(3)))
				.endTime(DateTimeUtil.parseTime(resultSet.getString(4)))
				.build());

			return new Response<>(employeeShiftEntities, ResponseType.FOUND);
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
