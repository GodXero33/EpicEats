package edu.icet.ecom.service.custom.finance;

import edu.icet.ecom.dto.finance.AllReports;
import edu.icet.ecom.dto.finance.Report;
import edu.icet.ecom.dto.finance.ReportLite;
import edu.icet.ecom.dto.finance.ReportsByEmployee;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface ReportService extends SuperService<Report> {
	Response<Report> add (ReportLite report);
	Response<Report> update (ReportLite report);
	Response<AllReports> getAllStructured ();
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<ReportsByEmployee> getAllByEmployeeId (Long employeeId);
}
