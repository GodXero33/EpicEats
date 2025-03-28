package edu.icet.ecom.dto.merchandise;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalesPackage {
	private Long id;
	private String name;
	private String description;
	private Double discountPercentage;
}
