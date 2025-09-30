package digital.rj.apileadsandemails.Leads.Controller;

import digital.rj.apileadsandemails.Leads.DTOs.Request.LeadsRequest;
import digital.rj.apileadsandemails.Leads.DTOs.Response.LeadsResponse;
import digital.rj.apileadsandemails.Leads.Service.LeadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/leads")
public class LeadsController {
    private final LeadService service;

    public LeadsController(LeadService service){
        this.service = service;
    }

    //Criar um novo objeto
    @PostMapping
    public ResponseEntity<?> create(@RequestBody LeadsRequest request){
        var create = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    //Listar todos os Leads
    @GetMapping
    public ResponseEntity<List<LeadsResponse>> listAll() {

        var listToResponse = service.listAll();

        return ResponseEntity.status(HttpStatus.OK).body(listToResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadsResponse> listById(@PathVariable UUID id){
        var listById = service.searchById(id);
        return ResponseEntity.ok(listById);
    }

    @GetMapping("/search")
    public ResponseEntity<LeadsResponse> searchByName(@RequestParam String name){
        var searchByName = service.searchByName(name);
        return ResponseEntity.ok(searchByName);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LeadsResponse> deleteId(@PathVariable UUID id){
        var deleteById = service.delete(id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(deleteById);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<LeadsResponse> deleteByName(@PathVariable String name){
        var deleteByName = service.deleteByName(name);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleteByName);
    }
}
