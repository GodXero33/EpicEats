package edu.icet.ecom.service.custom.employee;

import edu.icet.ecom.dto.employee.PromotionHistory;
import edu.icet.ecom.service.FullDataService;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface PromotionHistoryService extends FullDataService<PromotionHistory> {
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<List<PromotionHistory>> getAllByEmployeeId (Long employeeId);
}
