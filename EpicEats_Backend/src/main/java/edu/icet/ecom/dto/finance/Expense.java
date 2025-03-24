package edu.icet.ecom.dto.finance;

import edu.icet.ecom.util.enumaration.ExpenseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
	private Long id;
	private ExpenseType expenseType;
	private Double amount;
	private LocalDate expenseDate;
	private String description;
}
