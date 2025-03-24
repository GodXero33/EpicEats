package edu.icet.ecom.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
	private Long id;
	private String name;
	private String description;
	private Integer quantity;
	private String unit;
	private LocalDateTime updatedAt;
}
