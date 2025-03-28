package edu.icet.ecom.dto.restaurant;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTable {
	private Integer id;
	private Integer tableNumber;
	private Integer capacity;
	private LocalDateTime lastBooked;
	private Boolean isAvailable;
}
