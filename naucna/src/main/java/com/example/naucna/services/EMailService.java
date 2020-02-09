package com.example.naucna.services;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.example.naucna.model.Text;
import com.example.naucna.model.User;
import com.example.naucna.security.TokenUtils;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

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
	@Autowired
	public TokenUtils tokenUtils;

	@Autowired
	TaskService taskService;
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
	
	@Async
	public void sendNotificaitionAutor(User user,String procesId) throws MailException, InterruptedException, MessagingException {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

		String htmlMsg = "<h3>Pozdrav "+user.getIme()+"</h3><br> <p> Vas rad je prijavljen  u sistem.</p>";
		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(user.getEmail());
		helper.setSubject("Informacije o prilozenom radu");
		helper.setFrom(env.getProperty("spring.mail.username"));
		javaMailSender.send(mimeMessage);
		System.out.println("Email poslat autoru");


	}	
	@Async
	public void sendNotificaitionTemaNeodobrena(Text rad,String procesId) throws MailException, InterruptedException, MessagingException {
		com.example.naucna.model.User autor = new com.example.naucna.model.User();
		autor = rad.getAutor();
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

		String htmlMsg = "<h3>"+autor.getIme()+",</h3><br> <p> tema za vas rad "+rad.getNaslov()+" nije prihvacena. Pozdrav!</p>";
		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(autor.getEmail());
		helper.setSubject("Informacije o odbijanju prilozenog rada");
		helper.setFrom(env.getProperty("spring.mail.username"));
		javaMailSender.send(mimeMessage);
		System.out.println("Email poslat autoru za odbijanje");


	}	
	
	@Async
	public void sendNotificaitionAutorNijeDobarSadrzaj(User user,String procesId) throws MailException, InterruptedException, MessagingException {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

		String htmlMsg = "<h3>Pozdrav "+user.getIme()+"</h3><br> <p> Prijavljeni rad je potrebno izmjeniti. Molimo vas da izmjenite pdf ili prilozite novi.</p>";
		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(user.getEmail());
		helper.setSubject("Informacije o prilozenom radu");
		helper.setFrom(env.getProperty("spring.mail.username"));
		javaMailSender.send(mimeMessage);
		System.out.println("Email poslat autoru");


	}
	
	@Async
	public void sendNotificaitionAutorRadOdbijen(User user,String procesId) throws MailException, InterruptedException, MessagingException {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");

		String htmlMsg = "<h3>Pozdrav "+user.getIme()+",</h3><br> <p> Vas je odbijen iz tehnickih razloga jer niste izmjenili na vrijeme pdf fajl.</p>";
		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(user.getEmail());
		helper.setSubject("Informacije o odbijanju prilozenog rada");
		helper.setFrom(env.getProperty("spring.mail.username"));
		javaMailSender.send(mimeMessage);
		System.out.println("Email poslat autoru");


	}
	@Async
	public void sendNotificaitionUrednik(User urednik,String procesId) throws MailException, InterruptedException, MessagingException {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		String htmlMsg = "<h3>Pozdrav "+urednik.getIme()+"</h3><br> <p>Novi rad je prijavljen u sistem. Molimo Vas da izvrsite obradu istog.</p>";
		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(urednik.getEmail());
		helper.setSubject("Informacije o prilozenom radu");
		helper.setFrom(env.getProperty("spring.mail.username"));

		System.out.println("urednik mejl "+urednik.getEmail());
		javaMailSender.send(mimeMessage);
		System.out.println("Email poslat uredniku");
	}
	
	@Async
	public void sendNoviRadUrednik(User urednik,String procesId) throws MailException, InterruptedException, MessagingException {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		String htmlMsg = "<h3>Pozdrav "+urednik.getIme()+"</h3><br> <p>Novi rad je prijavljen u sistem. Molimo Vas da izvrsite izbor recenzenata.</p>";
		mimeMessage.setContent(htmlMsg, "text/html");
		helper.setTo(urednik.getEmail());
		helper.setSubject("Informacije o prilozenom radu");
		helper.setFrom(env.getProperty("spring.mail.username"));

		System.out.println("urednik mejl "+urednik.getEmail());
		javaMailSender.send(mimeMessage);
		System.out.println("Email poslat uredniku");
	}
}
