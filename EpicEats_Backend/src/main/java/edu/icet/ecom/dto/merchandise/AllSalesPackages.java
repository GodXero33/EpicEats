package edu.icet.ecom.dto.merchandise;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllSalesPackages implements SuperSalesPackage {
	private List<MenuItem> menuItems;
	private List<SalesPackageLite> salesPackages;
}
