package edu.icet.ecom.entity.finance;

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
public class ExpenseEntity {
	private Long id;
	private ExpenseType expenseType;
	private Double amount;
	private LocalDate expenseDate;
	private String description;
}
