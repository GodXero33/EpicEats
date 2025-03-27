package edu.icet.ecom.repository.custom.finance;

import edu.icet.ecom.entity.finance.ReportEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface ReportRepository extends CrudRepository<ReportEntity> {
	Response<Boolean> deleteByEmployeeId (Long employeeId);
	Response<List<ReportEntity>> getAllByEmployeeId (Long employeeId);
}
