package edu.icet.ecom.entity.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTableBookingEntity {
	private Integer tableId;
	private Long customerId;
	private LocalDate bookingDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private Boolean isClosed;
}
