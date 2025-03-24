package edu.icet.ecom.entity.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTableEntity {
	private Integer id;
	private Integer tableNumber;
	private Integer capacity;
	private LocalDateTime lastBooked;
	private Boolean isAvailable;
}
