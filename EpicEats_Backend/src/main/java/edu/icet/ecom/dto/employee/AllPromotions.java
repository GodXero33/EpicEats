package edu.icet.ecom.dto.employee;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllPromotions {
	private List<PromotionHistoryLite> promotions;
	private List<Employee> employees;
}
