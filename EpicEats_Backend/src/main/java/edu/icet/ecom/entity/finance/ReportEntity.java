package edu.icet.ecom.entity.finance;

import edu.icet.ecom.entity.employee.EmployeeEntity;
import edu.icet.ecom.util.enums.ReportType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity {
	private Long id;
	private LocalDateTime generatedAt;
	private ReportType type;
	private LocalDate startDate;
	private LocalDate endDate;
	private EmployeeEntity generatedBy;
	private String title;
	private String description;
}
