package digital.rj.apileadsandemails.Leads.Repository;
import digital.rj.apileadsandemails.Leads.Model.LeadsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;
public interface LeadsRepository extends JpaRepository<LeadsModel, UUID> {
    Optional<LeadsModel> findByName(String name);
    LeadsModel deleteLeadsModelByName(String name);
    boolean existsByName(String name);
}
