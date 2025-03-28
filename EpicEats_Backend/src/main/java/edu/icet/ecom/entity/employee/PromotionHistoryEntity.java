package edu.icet.ecom.entity.employee;

import edu.icet.ecom.util.enumaration.EmployeeRole;
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
	private Long employeeId;
	private EmployeeRole oldRole;
	private EmployeeRole newRole;
	private LocalDate promotionDate;
}
