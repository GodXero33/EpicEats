package edu.icet.ecom.entity;

import edu.icet.ecom.util.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity {
	private Long id;
	private LocalDateTime generatedAt;
	private ReportType type;
	private LocalDate startDate;
	private LocalDate endDate;
	private Long generatedBy;
	private String title;
	private String description;
}
