package digital.rj.apileadsandemails.Email.Service;

import digital.rj.apileadsandemails.Email.Model.ContactModel;
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

    public ResponseEntity<?> SendEmail(ContactModel model){
        if(model != null){
        var lead = new LeadsRequest(
                model.name(),
                model.enterprise(),
                model.phone(),
                model.email(),
                model.foco()
        );
        var saving = service.create(lead);

        var message = new SimpleMailMessage();
        message.setFrom("pedropeixotovz@gmail.com"); //Quem vai enviar a mensagem
        message.setSubject(String.valueOf(lead.foco())); //Assunto do texto transforma o enum em foco
        message.setTo(email.getTo()); //Para quem vai enviar
        message.setReplyTo(lead.email()); //Quem vai ficar fixado ou respondido
        message.setText(model.body()); //Texto do email ou seja o corpo
            mailSender.send(message);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }else{
            throw new IllegalArgumentException("Email ou Lead não preenchido!");
        }


    }

}
