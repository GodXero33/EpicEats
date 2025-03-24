package edu.icet.ecom.dto.merchandise;

import edu.icet.ecom.util.enumaration.MenuItemCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
	private Long id;
	private String name;
	private Double price;
	private String img;
	private MenuItemCategory category;
}
