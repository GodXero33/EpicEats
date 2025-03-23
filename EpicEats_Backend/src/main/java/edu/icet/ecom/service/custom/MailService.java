package edu.icet.ecom.service.custom;

public interface MailService {
	int sendEmail (String to, String subject, String text);
}
