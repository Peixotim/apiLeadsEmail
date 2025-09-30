package digital.rj.apileadsandemails.Leads.DTOs.Request;

import digital.rj.apileadsandemails.Leads.Model.Foco;
import lombok.Builder;


@Builder
public record LeadsRequest(String name, String email ,String phone ,Foco foco){
}
