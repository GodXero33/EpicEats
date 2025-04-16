package edu.icet.ecom.dto.employee;

import edu.icet.ecom.util.enums.EmployeeRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PromotionHistoryLite {
	private Long id;
	@NotNull(message = "employeeId can't be null")
	private Long employeeId;
	private EmployeeRole oldRole;
	private EmployeeRole newRole;
	private LocalDate promotionDate;
}
