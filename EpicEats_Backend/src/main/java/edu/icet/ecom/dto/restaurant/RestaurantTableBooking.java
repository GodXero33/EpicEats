package edu.icet.ecom.dto.restaurant;

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
public class RestaurantTableBooking {
	private Integer tableId;
	private Long customerId;
	private LocalDate bookingDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private Boolean isClosed;
}
