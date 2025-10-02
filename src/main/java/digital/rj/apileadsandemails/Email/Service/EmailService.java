package digital.rj.apileadsandemails.Email.Service;

import digital.rj.apileadsandemails.Email.Model.ContactModel;
import digital.rj.apileadsandemails.Email.Model.EmailResponse;
import digital.rj.apileadsandemails.Leads.DTOs.Request.LeadsRequest;
import digital.rj.apileadsandemails.Leads.Service.LeadService;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final LeadService service;

    public EmailService(JavaMailSender mailSender, LeadService service) {
        this.mailSender = mailSender;
        this.service = service;
    }

    public ResponseEntity<?> SendEmail(ContactModel model) {
        if (model == null) throw new IllegalArgumentException("Payload do contato não enviado.");

        // 1) tenta salvar (mas segue se já existir)
        boolean created = false;
        try {
            created = service.createIfNotExists(new LeadsRequest(
                    model.name(),
                    model.enterprise(),
                    model.phone(),
                    model.email(),
                    model.foco()
            ));
        } catch (RuntimeException ex) {
            // Falha de validação de lead não deve impedir e-mail.
            log.warn("Não foi possível salvar lead (seguindo com e-mail): {}", ex.getMessage());
        }

        // 2) monta e envia e-mail SEM depender do salvamento
        var msg = new SimpleMailMessage();
        msg.setFrom("pedroshisuiff020@gmail.com");           // remetente
        msg.setTo("pedroghost74@gmail.com");                 // destino RFC5322
        msg.setSubject("[RJGLOBAL] Novo contato – " + model.foco());

        String reply = safeReplyTo(model.email());
        if (reply != null) msg.setReplyTo(reply);

        msg.setText(buildBody(model));

        try {
            mailSender.send(msg);
        } catch (MailException ex) {
            log.error("Falha ao enviar e-mail: {}", ex.getMessage(), ex);
            // aqui você decide: quer sinalizar 502 para o front ou 202 otimista?
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Falha ao enviar e-mail.");
        }

        // 3) resposta clara para o front
        // 202 = aceito/processado; você também pode retornar 201 quando created=true
        return created
                ? ResponseEntity.status(HttpStatus.CREATED).body("lead_criado_e_email_enviado")
                : ResponseEntity.status(HttpStatus.ACCEPTED).body("lead_ja_existia_email_enviado");
    }

    private String safeReplyTo(String email) {
        if (email == null) return null;
        String e = email.trim();
        if (e.isBlank()) return null;
        try {
            var ia = new InternetAddress(e);
            ia.validate();
            return ia.getAddress(); // normaliza
        } catch (AddressException ex) {
            log.warn("Reply-To inválido: {}", e);
            return null;
        }
    }

    private String buildBody(ContactModel c) {
        var empresa = isBlank(c.enterprise()) ? "-" : c.enterprise().trim();
        var fone = isBlank(c.phone()) ? "-" : c.phone().trim();
        var body = isBlank(c.body()) ? "-" : c.body().trim();

        return """
               Nova mensagem do site RJGLOBAL

               Nome: %s
               E-mail: %s
               Telefone: %s
               Empresa: %s
               Assunto (foco): %s

               Mensagem:
               %s
               """.formatted(
                c.name(), c.email(), fone, empresa, c.foco(), body
        );
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}