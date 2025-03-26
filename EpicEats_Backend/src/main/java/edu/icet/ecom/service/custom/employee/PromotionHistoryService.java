package edu.icet.ecom.service.custom.employee;

import edu.icet.ecom.dto.employee.PromotionHistory;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface PromotionHistoryService extends SuperService<PromotionHistory> {
	Response<Boolean> deleteByEmployeeId (Long employeeId);
	Response<List<PromotionHistory>> getAllByEmployeeId (Long employeeId);
}
