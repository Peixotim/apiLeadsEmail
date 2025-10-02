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
        message.setSubject(String.valueOf(model.foco())); //Assunto do texto transforma o enum em foco
        message.setTo("pedropeixotovz@gmail.com"); //Para quem vai enviar (Alterar depois para rjglobal)
        message.setReplyTo(lead.email()); //Quem vai ficar fixado ou respondido
        message.setText(buildBody(model)); //Texto do email ou seja o corpo
            mailSender.send(message);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }else{
            throw new IllegalArgumentException("Email ou Lead não preenchido!");
        }


    }

    private String buildBody(ContactModel req) {
        var empresa = req.enterprise() == null ? "" : ("\nEmpresa: " + req.enterprise());
        return """
               Nova mensagem do site:

               Nome: %s
               E-mail: %s
               Telefone: %s
               Assunto: %s%s

               Mensagem:
               %s
               """.formatted(
                req.name(),
                req.email(),
                req.phone() == null ? "-" : req.phone(),
                req.foco(),
                empresa,
                req.body() == null ? "-" : req.body()
        );
    }

}
