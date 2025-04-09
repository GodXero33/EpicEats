package edu.icet.ecom.dto.restaurant;

import edu.icet.ecom.dto.misc.Customer;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTableBooking {
	private Long id;
	private RestaurantTable table;
	private Customer customer;
	private LocalDate bookingDate;
	private LocalTime startTime;
	private LocalTime endTime;
}
