package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeShift {
	private Long id;
	private Long employeeId;
	private LocalDate shiftDate;
	private LocalTime startTime;
	private LocalTime endTime;
}
