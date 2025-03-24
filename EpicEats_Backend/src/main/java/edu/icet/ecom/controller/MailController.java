package edu.icet.ecom.controller;

import edu.icet.ecom.config.apidoc.misc.mail.SendEmailApiDoc;
import edu.icet.ecom.dto.misc.Email;
import edu.icet.ecom.service.custom.misc.MailService;
import edu.icet.ecom.util.CustomHttpResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
@Tag(name = "Mail sender", description = "APIs for send mails")
public class MailController {
	private final MailService mailService;

	@SendEmailApiDoc
	@PostMapping("/send-email")
	public CustomHttpResponse<Object> sendEmail (@RequestBody Email email) {
		final int mailSentStatus = this.mailService.sendEmail(
			email.getTo(),
			email.getSubject(),
			email.getMessage()
		);

		if (mailSentStatus == 200) {
			return new CustomHttpResponse<>(HttpStatus.OK, null, "Email sent successfully!");
		} else {
			return new CustomHttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, "Failed to send email");
		}
	}
}
