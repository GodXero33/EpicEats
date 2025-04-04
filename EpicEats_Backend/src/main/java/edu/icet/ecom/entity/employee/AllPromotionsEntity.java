package edu.icet.ecom.entity.employee;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllPromotionsEntity {
	private List<PromotionHistoryLiteEntity> promotions;
	private List<EmployeeEntity> employees;
}
