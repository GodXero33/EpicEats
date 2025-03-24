package edu.icet.ecom.entity;

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
	private Long id;
	private Integer tableId;
	private LocalDate bookingDate;
	private LocalTime startTime;
	private LocalTime endTime;
}
