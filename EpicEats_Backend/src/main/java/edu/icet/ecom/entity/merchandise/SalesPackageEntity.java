package edu.icet.ecom.entity.merchandise;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalesPackageEntity implements SuperSalesPackageEntity {
	private Long id;
	private String name;
	private String description;
	private Double discountPercentage;
	private List<MenuItemEntity> menuItems;
}
