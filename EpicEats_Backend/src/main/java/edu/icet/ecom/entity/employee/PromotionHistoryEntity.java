package edu.icet.ecom.entity.employee;

import edu.icet.ecom.util.enums.EmployeeRole;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PromotionHistoryEntity {
	private Long id;
	private EmployeeEntity employee;
	private EmployeeRole oldRole;
	private EmployeeRole newRole;
	private LocalDate promotionDate;
}
