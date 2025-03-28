package edu.icet.ecom.entity.merchandise;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalesPackageEntity {
	private Long id;
	private String name;
	private String description;
	private Double discountPercentage;
}
