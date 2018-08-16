package com.bridgelabz.fundoo.user.rabbitmq;

import javax.mail.MessagingException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.fundoo.user.models.Email;
import com.bridgelabz.fundoo.user.services.EmailService;


@Component
public class ConsumerImpl implements Consumer {
	@Autowired
	private WebApplicationContext context;

	@RabbitListener(queues = "${jsa.rabbitmq.queue}")
	public void recievedMessage(Email email) throws MessagingException {
		System.out.println("Recieved Message: " + email);
		EmailService emailService = context.getBean(EmailService.class);
		emailService.sendEmail(email);
	}

}
