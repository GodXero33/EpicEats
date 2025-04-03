package edu.icet.ecom.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeShiftLite {
	private Long id;
	@NotNull(message = "Employee can't be null")
	private Long employeeId;
	private LocalDate shiftDate;
	private LocalTime startTime;
	private LocalTime endTime;
}
