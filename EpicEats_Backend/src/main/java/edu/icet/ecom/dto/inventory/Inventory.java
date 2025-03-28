package edu.icet.ecom.dto.inventory;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
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
