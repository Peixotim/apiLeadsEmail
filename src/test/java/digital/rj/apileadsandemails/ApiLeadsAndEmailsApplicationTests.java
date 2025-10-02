package digital.rj.apileadsandemails;

import digital.rj.apileadsandemails.Leads.Service.LeadService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class ApiLeadsAndEmailsApplicationTests {




    @MockBean
    JavaMailSender mailSender;

    @MockBean
    LeadService leadService;

    @Test
    void contextLoads() {
        // smoke test: apenas garante que o contexto sobe
    }
}
