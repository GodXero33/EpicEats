package edu.icet.ecom.service.custom.employee;

import edu.icet.ecom.dto.employee.AllPromotions;
import edu.icet.ecom.dto.employee.PromotionHistory;
import edu.icet.ecom.dto.employee.PromotionHistoryLite;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface PromotionHistoryService extends SuperService<PromotionHistory> {
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<List<PromotionHistory>> getAllByEmployeeId (Long employeeId);
	Response<PromotionHistory> update (PromotionHistoryLite promotionHistory);
	Response<AllPromotions> getAllStructured ();
}
