package digital.rj.apileadsandemails.Leads.Mapper;

import digital.rj.apileadsandemails.Leads.DTOs.Entity.LeadDTO;
import digital.rj.apileadsandemails.Leads.DTOs.Request.LeadsRequest;
import digital.rj.apileadsandemails.Leads.DTOs.Response.LeadsResponse;
import digital.rj.apileadsandemails.Leads.Model.LeadsModel;
import org.springframework.stereotype.Component;

@Component
public class LeadsMapper {

    public static LeadDTO toLead(LeadsRequest request){
        var l =
                LeadDTO
                        .builder()
                        .phone(request.phone())
                        .name(request.name()).
                        foco(request.foco())
                        .email(request.email())
                        .enterprise(request.enterprise())
                        .build();
        return l ;
    }

    public LeadsResponse toResponse(LeadDTO dto){
        var l =
                LeadsResponse.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .foco(dto.getFoco())
                        .phone(dto.getPhone())
                        .enterprise(dto.getEnterprise())
                        .email(dto.getEmail())
                        .build();
        return l;
    }
}
