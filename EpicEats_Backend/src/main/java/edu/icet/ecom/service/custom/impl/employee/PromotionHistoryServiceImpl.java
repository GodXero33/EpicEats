package edu.icet.ecom.service.custom.impl.employee;

import edu.icet.ecom.dto.employee.PromotionHistory;
import edu.icet.ecom.entity.employee.PromotionHistoryEntity;
import edu.icet.ecom.repository.custom.employee.PromotionHistoryRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.employee.PromotionHistoryService;
import edu.icet.ecom.util.Response;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class PromotionHistoryServiceImpl implements PromotionHistoryService {
	private final SuperServiceHandler<PromotionHistory, PromotionHistoryEntity> serviceHandler;

	public PromotionHistoryServiceImpl (PromotionHistoryRepository promotionHistoryRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(promotionHistoryRepository, mapper, PromotionHistory.class, PromotionHistoryEntity.class);
	}

	@Override
	public Response<PromotionHistory> get (Long id) {
		return this.serviceHandler.get(id);
	}

	@Override
	public Response<List<PromotionHistory>> getAll () {
		return this.serviceHandler.getAll();
	}

	@Override
	public Response<PromotionHistory> add (PromotionHistory dto) {
		return this.serviceHandler.add(dto);
	}

	@Override
	public Response<PromotionHistory> update (PromotionHistory dto) {
		return this.serviceHandler.update(dto);
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return this.serviceHandler.delete(id);
	}
}
