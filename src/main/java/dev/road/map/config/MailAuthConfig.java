package dev.road.map.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//@Configuration
//public class MailAuthConfig {
//	
//	// 숨김처리
//	@Value("mail.sender")
//	String senderMail;
//	
//	@Value("mail.password")
//	String password;
//	
//	@Bean
//	public JavaMailSender getJavaMailSender() {
//		Properties properties = new Properties(); 
//		properties.put("mail.smtp.auth", true);
//		properties.put("mail.transport.protocol", "smtp");
//		properties.put("mail.smtp.starttls.enable", true);
//		properties.put("mail.smtp.starttls.required", true);
//		properties.put("mail.debug", true);
//		
//		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//		mailSender.setHost("smtp.gmail.com");
//		mailSender.setPort(587);
//		mailSender.setUsername(senderMail);
//		mailSender.setPassword(password);
//		mailSender.setDefaultEncoding("utf-8");
//		mailSender.setJavaMailProperties(properties);
//		
//		return mailSender;
//	}
//}