package edu.icet.ecom.dto.merchandise;

import lombok.*;

import java.util.List;

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
	private List<MenuItem> menuItems;
}
