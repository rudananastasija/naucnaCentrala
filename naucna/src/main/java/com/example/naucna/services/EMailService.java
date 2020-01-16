package com.example.naucna.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.example.naucna.model.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
@Service
public class EMailService {
	@Autowired
	private JavaMailSender javaMailSender;

	/*
	 * Koriscenje klase za ocitavanje vrednosti iz application.properties fajla
	 */
	@Autowired
	private Environment env;

	/*
	 * Anotacija za oznacavanje asinhronog zadatka
	 * Vise informacija na: https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#scheduling
	 */
	@Async
	public void sendNotificaitionAsync(User user,String procesId) throws MailException, InterruptedException, MessagingException {

		System.out.println("Slanje emaila...");
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

		String htmlMsg = "<h3>Dobrodosli "+user.getIme()+"</h3><br> <p>Da biste aktivirali korisnicki  nalog posjetite  <a href=\"http://localhost:4200/aktivacija/"+procesId+"\">link</a></p>";
		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(user.getEmail());
		helper.setSubject("Verifikacija clanstva");
		helper.setFrom(env.getProperty("spring.mail.username"));
		javaMailSender.send(mimeMessage);
	
		System.out.println("Email poslat!");
	}
}
