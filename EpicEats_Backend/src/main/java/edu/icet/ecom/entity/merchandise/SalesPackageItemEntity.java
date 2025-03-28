package edu.icet.ecom.entity.merchandise;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalesPackageItemEntity {
	private Long packageId;
	private Long itemId;
	private Integer quantity;
}
