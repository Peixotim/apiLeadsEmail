package digital.rj.apileadsandemails;

import digital.rj.apileadsandemails.Leads.Service.LeadService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class ApiLeadsAndEmailsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiLeadsAndEmailsApplication.class, args);
    }

}
