package edu.icet.ecom.entity.employee;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeShiftEntity {
	private Long id;
	private Long employeeId;
	private LocalDate shiftDate;
	private LocalTime startTime;
	private LocalTime endTime;
}
