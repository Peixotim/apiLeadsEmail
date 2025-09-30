package digital.rj.apileadsandemails.Leads.Mapper;

import digital.rj.apileadsandemails.Leads.DTOs.Entity.LeadDTO;
import digital.rj.apileadsandemails.Leads.Model.LeadsModel;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LeadUnityMapper {

    public LeadsModel map(LeadDTO dto){
        LeadsModel m = new LeadsModel();
        m.setId(dto.getId());
        m.setFoco(dto.getFoco());
        m.setPhone(dto.getPhone());
        m.setName(dto.getName());
        return m;
    }

    public LeadDTO map(LeadsModel model){
        LeadDTO dto = new LeadDTO();
        dto.setFoco(model.getFoco());
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setPhone(model.getPhone());
        return dto;
    }

}
