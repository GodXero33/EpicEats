package edu.icet.ecom.entity;

import edu.icet.ecom.util.MenuItemCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemEntity {
	private Long id;
	private String name;
	private Double price;
	private String img;
	private MenuItemCategory category;
}
