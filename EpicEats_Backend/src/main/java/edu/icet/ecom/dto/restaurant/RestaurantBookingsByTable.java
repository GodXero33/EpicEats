package edu.icet.ecom.dto.restaurant;

import edu.icet.ecom.dto.misc.Customer;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBookingsByTable {
	private List<RestaurantTableBookingLite> bookings;
	private List<Customer> customers;
	private RestaurantTable table;
}
