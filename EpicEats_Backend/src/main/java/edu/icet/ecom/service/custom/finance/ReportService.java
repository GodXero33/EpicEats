package edu.icet.ecom.service.custom.finance;

import edu.icet.ecom.dto.finance.Report;
import edu.icet.ecom.dto.finance.ReportCreate;
import edu.icet.ecom.service.FullDataService;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface ReportService extends FullDataService<Report> {
	Response<Report> add (ReportCreate reportCreate);
	Response<Report> update (ReportCreate reportCreate);
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<List<Report>> getAllByEmployeeId (Long employeeId);
}
