package edu.icet.ecom.entity.finance;

import edu.icet.ecom.entity.employee.EmployeeEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportsByEmployeeEntity {
	private List<ReportLiteEntity> reports;
	private EmployeeEntity generatedBy;
}
