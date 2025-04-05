package edu.icet.ecom.dto.merchandise;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllSalesPackages {
	private List<MenuItem> menuItems;
	private List<SalesPackageLite> salesPackages;
}
