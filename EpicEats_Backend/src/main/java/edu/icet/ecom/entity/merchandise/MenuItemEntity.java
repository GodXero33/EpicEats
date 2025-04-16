package edu.icet.ecom.entity.merchandise;

import edu.icet.ecom.util.enums.MenuItemCategory;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
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
