package edu.icet.ecom.service.custom.misc;

public interface MailService {
	int sendEmail (String to, String subject, String text);
}
