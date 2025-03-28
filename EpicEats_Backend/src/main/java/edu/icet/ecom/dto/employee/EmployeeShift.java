package edu.icet.ecom.dto.employee;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeShift {
	private Long id;
	private Long employeeId;
	private LocalDate shiftDate;
	private LocalTime startTime;
	private LocalTime endTime;
}
