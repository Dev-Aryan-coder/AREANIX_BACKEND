package com.example.Areanixx.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	// reads the actual authenticated account from application.properties -
	// Gmail SMTP rejects/misbehaves if 'from' doesn't match spring.mail.username
	@Value("${spring.mail.username}")
	private String fromAddress;

	// used by UserService.registerUser() to send the welcome email
	public void sendemail(String To, String Subject, String Msg) {
		SimpleMailMessage mail = new SimpleMailMessage();

		mail.setTo(To);
		mail.setText(Msg);
		mail.setSubject(Subject);
		mail.setFrom(fromAddress);
		mailSender.send(mail);
	}

}
