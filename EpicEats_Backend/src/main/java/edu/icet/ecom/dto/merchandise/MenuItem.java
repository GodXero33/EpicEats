package edu.icet.ecom.dto.merchandise;

import edu.icet.ecom.util.enumaration.MenuItemCategory;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
	private Long id;
	private String name;
	private Double price;
	private String img;
	private MenuItemCategory category;
	private Integer quantity;
}
