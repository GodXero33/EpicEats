package edu.icet.ecom.repository.custom.finance;

import edu.icet.ecom.entity.finance.ReportLiteEntity;
import edu.icet.ecom.entity.finance.ReportEntity;
import edu.icet.ecom.entity.finance.ReportsByEmployeeEntity;
import edu.icet.ecom.repository.FullDataRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface ReportRepository extends FullDataRepository<ReportEntity> {
	Response<ReportEntity> add (ReportLiteEntity reportLite);
	Response<ReportEntity> update (ReportLiteEntity reportLite);
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<ReportsByEmployeeEntity> getAllByEmployeeId (Long employeeId);
}
