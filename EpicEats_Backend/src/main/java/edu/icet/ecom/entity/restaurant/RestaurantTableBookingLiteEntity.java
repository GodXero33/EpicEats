package edu.icet.ecom.entity.restaurant;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTableBookingLiteEntity {
	private Long id;
	private Long tableId;
	private Long customerId;
	private LocalDate bookingDate;
	private LocalTime startTime;
	private LocalTime endTime;
}
