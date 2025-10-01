package digital.rj.apileadsandemails.Leads.DTOs.Entity;
import digital.rj.apileadsandemails.Leads.Model.Foco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LeadDTO {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String enterprise;
    private Foco foco;
}
