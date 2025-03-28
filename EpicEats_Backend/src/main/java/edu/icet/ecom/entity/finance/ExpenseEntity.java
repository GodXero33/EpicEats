package edu.icet.ecom.entity.finance;

import edu.icet.ecom.util.enumaration.ExpenseType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseEntity {
	private Long id;
	private ExpenseType expenseType;
	private Double amount;
	private LocalDate expenseDate;
	private String description;
}
