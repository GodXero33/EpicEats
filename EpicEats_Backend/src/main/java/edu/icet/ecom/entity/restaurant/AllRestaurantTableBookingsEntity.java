package edu.icet.ecom.entity.restaurant;

import edu.icet.ecom.entity.misc.CustomerEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllRestaurantTableBookingsEntity {
	private List<RestaurantTableBookingLiteEntity> bookings;
	private List<CustomerEntity> customers;
	private List<RestaurantTableEntity> tables;
}
