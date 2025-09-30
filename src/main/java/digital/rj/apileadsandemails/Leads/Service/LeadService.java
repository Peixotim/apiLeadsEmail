package digital.rj.apileadsandemails.Leads.Service;
import digital.rj.apileadsandemails.Exceptions.ResourceNotFoundException;
import digital.rj.apileadsandemails.Leads.DTOs.Entity.LeadDTO;
import digital.rj.apileadsandemails.Leads.DTOs.Request.LeadsRequest;
import digital.rj.apileadsandemails.Leads.DTOs.Response.LeadsResponse;
import digital.rj.apileadsandemails.Leads.Mapper.LeadUnityMapper;
import digital.rj.apileadsandemails.Leads.Mapper.LeadsMapper;
import digital.rj.apileadsandemails.Leads.Model.LeadsModel;
import digital.rj.apileadsandemails.Leads.Repository.LeadsRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class LeadService {

    private final LeadsRepository repository;
    private final LeadUnityMapper entitymapper;
    private final LeadsMapper mapper;

    public LeadService(LeadsRepository repository , LeadUnityMapper mp ,LeadsMapper mapper){
        this.repository = repository;
        this.entitymapper = mp;
        this.mapper = mapper;

    }


    // Cria um novo Lead
    public LeadsResponse create(LeadsRequest request){
        if(request == null ){
            throw new RuntimeException("It is null fill in !");
        }else if(request.name() == null){
            throw new RuntimeException("The name is missing");
        } else if (request.phone() == null) {
            throw new RuntimeException("The phone is unfilled");
        }else if(request.foco() == null){
            throw new RuntimeException("The Focus is unfilled");
        }

        var requestFromLead = mapper.toLead(request); //Mapeia a request
        var mapperModel = entitymapper.map(requestFromLead);
        repository.save(mapperModel);
        var response = mapper.toResponse(requestFromLead);
        return response;
    }


    //Faz a listagem de todos os Leads
    public List<LeadsResponse> listAll(){
      List<LeadsModel> find = repository.findAll(); //Passa uma lista de Leads
      List<LeadDTO> dto = find.stream().map(entitymapper::map).toList(); //Transforma em uma lista de
      List<LeadsResponse> toResponse = dto.stream().map(mapper :: toResponse).toList();

      return toResponse;

    }


    //Procura o lead pelo id
    public LeadsResponse searchById(UUID id){


           var findLead =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Error Lead Not Found !"));
           var mapEntity = entitymapper.map(findLead); //Mapeia o lead para um DTO
           var mapperToResponse = mapper.toResponse(mapEntity); //Mapeia o DTO para um Response DTO

            return mapperToResponse;

    }

    //Procura o lead pelo nome
    public LeadsResponse searchByName(String name){
        var findLead = repository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Error Lead Not Found !"));
        var mapEntity = entitymapper.map(findLead);
        var mapToResponse = mapper.toResponse(mapEntity);

        return mapToResponse;
    }

    //Deleta um Lead com id
    public LeadsResponse delete(UUID id){
        if(repository.existsById(id)){
            var find = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Error Lead Not Found !"));
            var mapEntity = entitymapper.map(find);
            var mapperToResponse = mapper.toResponse(mapEntity);
            repository.deleteById(id);

            return mapperToResponse;
        }else{
            throw new ResourceNotFoundException("Error Lead Not Found !");
        }
    }

    public LeadsResponse deleteByName(String name){

        if(repository.existsByName(name)){

            var find = repository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Error Lead Not Found !"));
            var mapEntity = entitymapper.map(find);
            var mapperToResponse = mapper.toResponse(mapEntity);
            repository.deleteLeadsModelByName(name);

            return mapperToResponse;
        }else{
            throw new ResourceNotFoundException("Error Lead Not Found !");
        }
    }
}
