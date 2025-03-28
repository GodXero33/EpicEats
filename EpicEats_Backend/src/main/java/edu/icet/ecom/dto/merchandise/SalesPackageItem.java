package edu.icet.ecom.dto.merchandise;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalesPackageItem {
	private Long packageId;
	private Long itemId;
	private Integer quantity;
}
