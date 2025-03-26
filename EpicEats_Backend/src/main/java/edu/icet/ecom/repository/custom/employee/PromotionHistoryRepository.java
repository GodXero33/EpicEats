package edu.icet.ecom.repository.custom.employee;

import edu.icet.ecom.entity.employee.PromotionHistoryEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface PromotionHistoryRepository extends CrudRepository<PromotionHistoryEntity> {
	Response<Boolean> deleteByEmployeeId (Long employeeId);
	Response<List<PromotionHistoryEntity>> getAllByEmployeeId (Long employeeId);
}
