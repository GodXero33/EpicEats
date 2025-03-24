package edu.icet.ecom.entity.merchandise;

import edu.icet.ecom.util.enumaration.MenuItemCategory;
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
	private Integer quantity;
}
