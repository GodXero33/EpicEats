package edu.icet.ecom.repository.custom.restaurant;

import edu.icet.ecom.dto.restaurant.TimeRange;
import edu.icet.ecom.entity.restaurant.AllRestaurantTableBookingsEntity;
import edu.icet.ecom.entity.restaurant.RestaurantTableBookingEntity;
import edu.icet.ecom.entity.restaurant.RestaurantTableBookingLiteEntity;
import edu.icet.ecom.entity.restaurant.RestaurantTableEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantTableRepository extends CrudRepository<RestaurantTableEntity> {
	Response<Boolean> isTableExist (Long tableId);
	Response<Boolean> isTableAvailable (Long tableId);
	Response<Boolean> isTableNumberExist (Integer tableNumber);
	Response<Boolean> isTableNumberExist (Integer tableNumber, Long tableId);
	Response<List<RestaurantTableEntity>> getAllByIDs (List<Long> ids);
	Response<RestaurantTableBookingEntity> addBooking (RestaurantTableBookingLiteEntity restaurantTableBookingLiteEntity);
	Response<RestaurantTableBookingEntity> updateBooking (RestaurantTableBookingLiteEntity restaurantTableBookingLiteEntity);
	Response<RestaurantTableBookingEntity> getBooking (Long id);
	Response<AllRestaurantTableBookingsEntity> getAllBookings ();
	Response<AllRestaurantTableBookingsEntity> getAllBookingsByTableId (Long tableId);
	Response<Object> deleteBooking (Long id);
	Response<Object> deleteAllBookingsByTableId (Long tableId);
	Response<List<TimeRange>> getTimeSlotsForTargetTableInTargetDate (Long tableId, LocalDate date, Long bookingId);
}
