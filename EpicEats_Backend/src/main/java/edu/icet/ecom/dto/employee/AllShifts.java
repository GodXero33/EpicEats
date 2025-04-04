package edu.icet.ecom.dto.employee;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllShifts {
	private List<EmployeeShiftLite> shifts;
	private List<Employee> employees;
}
