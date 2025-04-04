package edu.icet.ecom.repository.custom.employee;

import edu.icet.ecom.entity.employee.AllPromotionsEntity;
import edu.icet.ecom.entity.employee.PromotionHistoryEntity;
import edu.icet.ecom.entity.employee.PromotionHistoryLiteEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface PromotionHistoryRepository extends CrudRepository<PromotionHistoryEntity> {
	Response<Object> deleteByEmployeeId (Long employeeId);
	Response<List<PromotionHistoryEntity>> getAllByEmployeeId (Long employeeId);
	Response<PromotionHistoryEntity> update (PromotionHistoryLiteEntity promotionHistory);
	Response<AllPromotionsEntity> getAllStructured ();
}
