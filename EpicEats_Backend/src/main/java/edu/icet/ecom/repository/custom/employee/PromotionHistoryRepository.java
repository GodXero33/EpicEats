package edu.icet.ecom.repository.custom.employee;

import edu.icet.ecom.entity.employee.PromotionHistoryEntity;
import edu.icet.ecom.repository.FullDataRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface PromotionHistoryRepository extends FullDataRepository<PromotionHistoryEntity> {
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<List<PromotionHistoryEntity>> getAllByEmployeeId (Long employeeId);
}
