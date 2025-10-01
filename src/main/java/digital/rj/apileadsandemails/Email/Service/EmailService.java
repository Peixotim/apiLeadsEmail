package digital.rj.apileadsandemails.Email.Service;

import digital.rj.apileadsandemails.Email.Model.EmailModel;
import digital.rj.apileadsandemails.Leads.DTOs.Request.LeadsRequest;
import digital.rj.apileadsandemails.Leads.DTOs.Response.LeadsResponse;
import digital.rj.apileadsandemails.Leads.Service.LeadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final LeadService service;

    public EmailService(JavaMailSender mailSender,LeadService service){
        this.mailSender = mailSender;
        this.service = service;
    }

    public ResponseEntity<?> SendEmail(EmailModel email, LeadsRequest request){
        if(email != null && request != null){
        var saving = service.create(request);

        var message = new SimpleMailMessage();
        message.setFrom("pedropeixotovz@gmail.com"); //Quem vai enviar a mensagem
        message.setSubject(String.valueOf(request.foco())); //Assunto do texto transforma o enum em foco
        message.setTo(email.getTo()); //Para quem vai enviar
        message.setReplyTo(request.email()); //Quem vai ficar fixado ou respondido
        message.setText(email.getBody()); //Texto do email ou seja o corpo
            mailSender.send(message);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }else{
            throw new IllegalArgumentException("Email ou Lead não preenchido!");
        }


    }

}
