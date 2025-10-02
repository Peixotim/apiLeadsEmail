package digital.rj.apileadsandemails.Email.Model;

import digital.rj.apileadsandemails.Leads.Model.Foco;

public record ContactModel (
        String name,
        String phone,
        String email,
        String enterprise,
        Foco foco,
        String body
){
}
