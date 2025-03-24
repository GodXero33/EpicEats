package edu.icet.ecom.entity;

import edu.icet.ecom.util.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionHistoryEntity {
	private Long id;
	private Long employeeId;
	private EmployeeRole oldRole;
	private EmployeeRole newRole;
	private LocalDate promotionDate;
}
