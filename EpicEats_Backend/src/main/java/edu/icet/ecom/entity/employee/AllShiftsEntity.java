package edu.icet.ecom.entity.employee;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllShiftsEntity {
	private List<EmployeeShiftLiteEntity> shifts;
	private List<EmployeeEntity> employees;
}
