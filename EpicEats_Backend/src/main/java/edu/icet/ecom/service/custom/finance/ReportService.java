package edu.icet.ecom.service.custom.finance;

import edu.icet.ecom.dto.finance.Report;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface ReportService extends SuperService<Report> {
	Response<Report> getFull (Long id);
	Response<List<Report>> getAllFull ();
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<List<Report>> getAllByEmployeeId (Long employeeId);
}
