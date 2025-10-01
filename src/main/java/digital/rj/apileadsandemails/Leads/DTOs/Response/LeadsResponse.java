package digital.rj.apileadsandemails.Leads.DTOs.Response;
import digital.rj.apileadsandemails.Leads.Model.Foco;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
@Builder

public record LeadsResponse(String name, String email , UUID id, String phone,String enterprise, Foco foco){
}
