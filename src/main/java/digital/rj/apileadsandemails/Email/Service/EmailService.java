package digital.rj.apileadsandemails.Email.Service;

import digital.rj.apileadsandemails.Email.Model.Email;
import digital.rj.apileadsandemails.Exceptions.ResourceNotFoundException;
import digital.rj.apileadsandemails.Leads.DTOs.Response.LeadsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public ResponseEntity<?> SendEmail(Email email, LeadsResponse response){
        if(email != null && response != null){
        var message = new SimpleMailMessage();
        message.setFrom("pedropeixotovz@gmail.com"); //Quem vai enviar a mensagem
        message.setSubject(String.valueOf(response.foco())); //Assunto do texto transforma o enum em foco
        message.setTo(email.to()); //Para quem vai enviar
        message.setReplyTo(response.email());
        message.setText(email.body());
            mailSender.send(message);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }else{
            throw new IllegalArgumentException("Email ou Lead não preenchido!");
        }


    }

}
