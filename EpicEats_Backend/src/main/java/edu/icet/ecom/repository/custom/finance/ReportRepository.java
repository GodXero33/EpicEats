package edu.icet.ecom.repository.custom.finance;

import edu.icet.ecom.entity.finance.AllReportsEntity;
import edu.icet.ecom.entity.finance.ReportLiteEntity;
import edu.icet.ecom.entity.finance.ReportEntity;
import edu.icet.ecom.entity.finance.ReportsByEmployeeEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface ReportRepository extends CrudRepository<ReportEntity> {
	Response<ReportEntity> add (ReportLiteEntity reportLite);
	Response<ReportEntity> update (ReportLiteEntity reportLite);
	Response<AllReportsEntity> getAllStructured ();
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<ReportsByEmployeeEntity> getAllByEmployeeId (Long employeeId);
}
