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
public class AllReportsEntity {
	private List<ReportLiteEntity> reports;
	private List<EmployeeEntity> employees;
}
