package edu.icet.ecom.dto;

import edu.icet.ecom.util.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
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
