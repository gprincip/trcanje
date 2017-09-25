package glavni.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
class MailConfig {
	
	 @Bean(name="mailSender")
	    public MailSender javaMailService() {
	        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
	        javaMailSender.setHost("smtp.gmail.com");
	        javaMailSender.setPort(587);
	        javaMailSender.setProtocol("smtp");
	        javaMailSender.setUsername("gprincip132@gmail.com");
	        javaMailSender.setPassword("pw");
	        Properties mailProperties = new Properties();
	        mailProperties.put("mail.smtp.auth", "true");
	        mailProperties.put("mail.smtp.starttls.enable", "true");
	        mailProperties.put("mail.smtp.debug", "true");
	        mailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	        javaMailSender.setJavaMailProperties(mailProperties);
	        return javaMailSender;
	    }

}

