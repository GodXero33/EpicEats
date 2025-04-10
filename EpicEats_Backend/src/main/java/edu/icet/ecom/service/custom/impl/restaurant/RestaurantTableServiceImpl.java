package edu.icet.ecom.service.custom.impl.restaurant;

import edu.icet.ecom.dto.restaurant.RestaurantTable;
import edu.icet.ecom.dto.restaurant.RestaurantTableBooking;
import edu.icet.ecom.dto.restaurant.RestaurantTableBookingLite;
import edu.icet.ecom.dto.restaurant.TimeRange;
import edu.icet.ecom.entity.restaurant.RestaurantTableBookingEntity;
import edu.icet.ecom.entity.restaurant.RestaurantTableBookingLiteEntity;
import edu.icet.ecom.entity.restaurant.RestaurantTableEntity;
import edu.icet.ecom.repository.custom.restaurant.RestaurantTableRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.restaurant.RestaurantTableService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class RestaurantTableServiceImpl implements RestaurantTableService {
	private final SuperServiceHandler<RestaurantTable, RestaurantTableEntity> serviceHandler;
	private final RestaurantTableRepository restaurantTableRepository;
	private final ModelMapper mapper;

	public RestaurantTableServiceImpl (RestaurantTableRepository restaurantTableRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(restaurantTableRepository, mapper, RestaurantTable.class, RestaurantTableEntity.class);
		this.restaurantTableRepository = restaurantTableRepository;
		this.mapper = mapper;
	}

	@Override
	public Response<RestaurantTable> get (Long id) {
		return this.serviceHandler.get(id);
	}

	@Override
	public Response<List<RestaurantTable>> getAll () {
		return this.serviceHandler.getAll();
	}

	@Override
	public Response<RestaurantTable> add (RestaurantTable dto) {
		return this.serviceHandler.add(dto);
	}

	@Override
	public Response<RestaurantTable> update (RestaurantTable dto) {
		return this.serviceHandler.update(dto);
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.serviceHandler.delete(id);
	}

	@Override
	public Response<Boolean> isTableExist (Long tableId) {
		return this.restaurantTableRepository.isTableExist(tableId);
	}

	@Override
	public Response<Boolean> isTableNumberExist (Integer tableNumber) {
		return this.restaurantTableRepository.isTableNumberExist(tableNumber);
	}

	@Override
	public Response<Boolean> isTableNumberExist (Integer tableNumber, Long tableId) {
		return this.restaurantTableRepository.isTableNumberExist(tableNumber, tableId);
	}

	@Override
	public Response<RestaurantTableBooking> addBooking (RestaurantTableBookingLite restaurantTableBookingLite) {
		final Response<RestaurantTableBookingEntity> response = this.restaurantTableRepository.addBooking(this.mapper.map(restaurantTableBookingLite, RestaurantTableBookingLiteEntity.class));

		return new Response<>(response.getStatus() == ResponseType.CREATED ?
			this.mapper.map(response.getData(), RestaurantTableBooking.class) :
			null
		, response.getStatus());
	}

	@Override
	public Response<RestaurantTableBooking> updateBooking (RestaurantTableBookingLite restaurantTableBookingLite) {
		final Response<RestaurantTableBookingEntity> response = this.restaurantTableRepository.updateBooking(this.mapper.map(restaurantTableBookingLite, RestaurantTableBookingLiteEntity.class));

		return new Response<>(response.getStatus() == ResponseType.UPDATED ?
			this.mapper.map(response.getData(), RestaurantTableBooking.class) :
			null
			, response.getStatus());
	}

	@Override
	public Response<RestaurantTableBooking> getBooking (Long id) {
		return null;
	}

	@Override
	public Response<RestaurantTableBooking> getAllBookings () {
		return null;
	}

	@Override
	public Response<RestaurantTableBooking> getAllBookingsByTableId (Long tableId) {
		return null;
	}

	@Override
	public Response<Object> deleteBooking (Long id) {
		return null;
	}

	@Override
	public Response<Object> deleteAllBookingsByTableId (Long tableId) {
		return null;
	}

	private boolean checkTimeSlotOverlapping (TimeRange target, List<TimeRange> slots) {
		return slots.stream().anyMatch(slot ->
			(slot.start().isBefore(target.start()) && slot.end().isAfter(target.start())) ||
			(slot.start().isBefore(target.end()) && slot.end().isAfter(target.end())) ||
			(slot.start().isBefore(target.start()) && slot.end().isAfter(target.end())) ||
			(slot.start().isAfter(target.start()) && slot.end().isBefore(target.end())) ||
			(slot.start().equals(target.start()) && slot.end().equals(target.end()))
		);
	}

	@Override
	public Response<Boolean> isTableBookingOverlaps (RestaurantTableBookingLite restaurantTableBookingLite, boolean isUpdate) {
		final Response<List<TimeRange>> targetTimeSlotsForTableResponse = this.restaurantTableRepository.getTimeSlotsForTargetTableInTargetDate(restaurantTableBookingLite.getTableId(), restaurantTableBookingLite.getBookingDate(), isUpdate ? restaurantTableBookingLite.getId() : null);

		if (targetTimeSlotsForTableResponse.getStatus() == ResponseType.SERVER_ERROR) return new Response<>(false, ResponseType.SERVER_ERROR);

		final boolean isOverlapping = this.checkTimeSlotOverlapping(
			new TimeRange(restaurantTableBookingLite.getStartTime(), restaurantTableBookingLite.getEndTime()),
			targetTimeSlotsForTableResponse.getData()
		);

		return new Response<>(isOverlapping, isOverlapping ? ResponseType.FOUND : ResponseType.NOT_FOUND);
	}
}
