package edu.icet.ecom.service.custom.restaurant;

import edu.icet.ecom.dto.restaurant.*;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

public interface RestaurantTableService extends SuperService<RestaurantTable> {
	Response<Boolean> isTableExist (Long tableId);
	Response<Boolean> isTableAvailable (Long tableId);
	Response<Boolean> isTableNumberExist (Integer tableNumber);
	Response<Boolean> isTableNumberExist (Integer tableNumber, Long tableId);
	Response<RestaurantTableBooking> addBooking (RestaurantTableBookingLite restaurantTableBookingLite);
	Response<RestaurantTableBooking> updateBooking (RestaurantTableBookingLite restaurantTableBookingLite);
	Response<RestaurantTableBooking> getBooking (Long id);
	Response<AllRestaurantTableBookings> getAllBookings ();
	Response<RestaurantBookingsByTable> getAllBookingsByTableId (Long tableId);
	Response<Object> deleteBooking (Long id);
	Response<Object> deleteAllBookingsByTableId (Long tableId);
	Response<Boolean> isTableBookingOverlaps (RestaurantTableBookingLite restaurantTableBookingLite, boolean isUpdate);
}
