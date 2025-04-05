package edu.icet.ecom.entity.merchandise;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllSalesPackagesEntity implements SuperSalesPackageEntity {
	private List<MenuItemEntity> menuItems;
	private List<SalesPackageLiteEntity> salesPackages;
}
