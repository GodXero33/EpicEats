package edu.icet.ecom.service.custom.impl;

import edu.icet.ecom.service.custom.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
	private final JavaMailSender mailSender;
	private final Logger logger;
	@Value("${spring.from}")
	private String fromEmail;

	@Override
	public int sendEmail (String to, String subject, String text) {
		try {
			final MimeMessage message = this.mailSender.createMimeMessage();
			final MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text, true);
			helper.setFrom(this.fromEmail);

			this.mailSender.send(message);
			logger.info("Email sent successfully to {}", to);
			return 200;
		} catch (MessagingException | MailException mailException) {
			logger.info("Failed to send email to {}: {}", to, mailException.getMessage());
			return 500;
		}
	}
}
