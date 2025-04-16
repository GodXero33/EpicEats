package edu.icet.ecom.dto.finance;

import edu.icet.ecom.util.enums.ExpenseType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
	private Long id;
	private ExpenseType expenseType;
	private Double amount;
	private LocalDate expenseDate;
	private String description;
}
