package edu.icet.ecom.entity.employee;

import edu.icet.ecom.util.enumaration.EmployeeRole;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {
	private Long id;
	private String name;
	private String phone;
	private String email;
	private String address;
	private Double salary;
	private EmployeeRole role;
	private LocalDate dob;
	private LocalDate employeeSince;
}
