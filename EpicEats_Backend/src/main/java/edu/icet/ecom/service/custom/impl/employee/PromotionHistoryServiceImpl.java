package edu.icet.ecom.service.custom.impl.employee;

import edu.icet.ecom.dto.employee.AllPromotions;
import edu.icet.ecom.dto.employee.PromotionHistory;
import edu.icet.ecom.dto.employee.PromotionHistoryLite;
import edu.icet.ecom.entity.employee.AllPromotionsEntity;
import edu.icet.ecom.entity.employee.PromotionHistoryEntity;
import edu.icet.ecom.entity.employee.PromotionHistoryLiteEntity;
import edu.icet.ecom.repository.custom.employee.PromotionHistoryRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.employee.PromotionHistoryService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enums.ResponseType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class PromotionHistoryServiceImpl implements PromotionHistoryService {
	private final SuperServiceHandler<PromotionHistory, PromotionHistoryEntity> serviceHandler;
	private final PromotionHistoryRepository promotionHistoryRepository;
	private final ModelMapper mapper;

	public PromotionHistoryServiceImpl (PromotionHistoryRepository promotionHistoryRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(promotionHistoryRepository, mapper, PromotionHistory.class, PromotionHistoryEntity.class);
		this.promotionHistoryRepository = promotionHistoryRepository;
		this.mapper = mapper;
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
		return null;
	}

	@Override
	public Response<PromotionHistory> update (PromotionHistory dto) {
		return this.serviceHandler.update(dto);
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.serviceHandler.delete(id);
	}

	@Override
	public Response<Object> deleteByEmployeeId (Long employeeId) {
		return this.promotionHistoryRepository.deleteByEmployeeId(employeeId);
	}

	@Override
	public Response<List<PromotionHistory>> getAllByEmployeeId (Long employeeId) {
		final Response<List<PromotionHistoryEntity>> response = this.promotionHistoryRepository.getAllByEmployeeId(employeeId);

		return response.getStatus() == ResponseType.FOUND ?
			new Response<>(response.getData().stream().map(promotionHistoryEntity -> this.mapper.map(promotionHistoryEntity, PromotionHistory.class)).toList(), response.getStatus()) :
			new Response<>(null, response.getStatus());
	}

	@Override
	public Response<PromotionHistory> update (PromotionHistoryLite promotionHistory) {
		final Response<PromotionHistoryEntity> response = this.promotionHistoryRepository.update(this.mapper.map(promotionHistory, PromotionHistoryLiteEntity.class));

		return new Response<>(response.getStatus() == ResponseType.UPDATED ?
			this.mapper.map(response.getData(), PromotionHistory.class) :
			null
			, response.getStatus());
	}

	@Override
	public Response<AllPromotions> getAllStructured () {
		final Response<AllPromotionsEntity> response = this.promotionHistoryRepository.getAllStructured();

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			this.mapper.map(response.getData(), AllPromotions.class) :
			null
			, response.getStatus());
	}
}
