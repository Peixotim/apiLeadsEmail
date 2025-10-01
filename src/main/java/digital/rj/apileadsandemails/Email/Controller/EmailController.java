package digital.rj.apileadsandemails.Email.Controller;

import digital.rj.apileadsandemails.Email.Model.EmailModel;
import digital.rj.apileadsandemails.Email.Service.EmailService;
import digital.rj.apileadsandemails.Leads.DTOs.Request.LeadsRequest;
import digital.rj.apileadsandemails.Leads.DTOs.Response.LeadsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class EmailController {

    private final EmailService service;

    public EmailController(EmailService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> sendMail(@RequestBody EmailModel model , @RequestBody LeadsRequest request){
        return ResponseEntity.ok(service.SendEmail(model,request));
    }

}
