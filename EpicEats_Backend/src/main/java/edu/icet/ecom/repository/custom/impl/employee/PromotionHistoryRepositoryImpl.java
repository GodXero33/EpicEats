package edu.icet.ecom.repository.custom.impl.employee;

import edu.icet.ecom.entity.employee.PromotionHistoryEntity;
import edu.icet.ecom.repository.custom.employee.PromotionHistoryRepository;
import edu.icet.ecom.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PromotionHistoryRepositoryImpl implements PromotionHistoryRepository {
	@Override
	public Response<PromotionHistoryEntity> add (PromotionHistoryEntity entity) {
		return null;
	}

	@Override
	public Response<PromotionHistoryEntity> update (PromotionHistoryEntity entity) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<PromotionHistoryEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<PromotionHistoryEntity>> getAll () {
		return null;
	}

	@Override
	public Response<Boolean> deleteByEmployeeId (Long employeeId) {
		return null;
	}
}
