package edu.icet.ecom.entity.finance;

import edu.icet.ecom.util.enums.ReportType;
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
public class ReportLiteEntity {
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
