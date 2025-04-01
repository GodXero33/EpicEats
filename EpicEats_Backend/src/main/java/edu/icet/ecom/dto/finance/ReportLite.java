package edu.icet.ecom.dto.finance;

import edu.icet.ecom.util.enumaration.ReportType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportLite {
	private Long id;
	private LocalDateTime generatedAt;
	private ReportType type;
	private LocalDate startDate;
	private LocalDate endDate;
	@NotNull(message = "Generated employee can't be null")
	private Long generatedBy;
	private String title;
	private String description;
}
