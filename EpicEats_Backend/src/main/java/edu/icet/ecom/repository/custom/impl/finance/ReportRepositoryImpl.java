package edu.icet.ecom.repository.custom.impl.finance;

import edu.icet.ecom.entity.finance.ReportEntity;
import edu.icet.ecom.repository.custom.finance.ReportRepository;
import edu.icet.ecom.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepository {
	@Override
	public Response<ReportEntity> add (ReportEntity entity) {
		return null;
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
		return null;
	}

	@Override
	public Response<List<ReportEntity>> getAll () {
		return null;
	}

	@Override
	public Response<Boolean> deleteByEmployeeId (Long employeeId) {
		return null;
	}
}
