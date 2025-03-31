package edu.icet.ecom.repository.custom.finance;

import edu.icet.ecom.entity.finance.ReportEntity;
import edu.icet.ecom.repository.FullDataRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface ReportRepository extends FullDataRepository<ReportEntity> {
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<List<ReportEntity>> getAllByEmployeeId (Long employeeId);
}
