package edu.icet.ecom.entity.restaurant;

import edu.icet.ecom.entity.misc.CustomerEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTableBookingEntity {
	private RestaurantTableEntity table;
	private CustomerEntity customer;
	private LocalDate bookingDate;
	private LocalTime startTime;
	private LocalTime endTime;
}
