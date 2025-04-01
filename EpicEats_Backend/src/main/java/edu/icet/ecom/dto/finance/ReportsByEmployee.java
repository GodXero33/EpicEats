package edu.icet.ecom.dto.finance;

import edu.icet.ecom.dto.employee.Employee;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportsByEmployee {
	private List<ReportLite> reports;
	private Employee generatedBy;
}
