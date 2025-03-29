package edu.icet.ecom.entity.merchandise;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalesPackageRecordEntity {
	private String name;
	private String description;
	private Double discountPercentage;
	private List<SalesPackageItemEntity> salesPackageItems;
}
