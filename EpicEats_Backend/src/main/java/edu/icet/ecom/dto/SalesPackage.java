package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesPackage {
	private Long id;
	private String name;
	private String description;
	private Double discountPercentage;
}
