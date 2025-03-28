package edu.icet.ecom.entity.inventory;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntity {
	private Long id;
	private String name;
	private String description;
	private Integer quantity;
	private String unit;
	private LocalDateTime updatedAt;
}
