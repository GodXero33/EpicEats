package edu.icet.ecom.dto.misc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Email {
	private String to;
	private String subject;
	private String message;
}
