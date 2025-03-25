package edu.icet.ecom.repository.custom.finance;

import edu.icet.ecom.entity.finance.ReportEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

public interface ReportRepository extends CrudRepository<ReportEntity> {
	Response<Boolean> deleteByEmployeeId (Long employeeId);
}
