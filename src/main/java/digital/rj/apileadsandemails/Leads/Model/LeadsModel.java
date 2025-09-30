package digital.rj.apileadsandemails.Leads.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
@Entity(name = "tb_leads")
@Table(name = "tb_leads")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@RequiredArgsConstructor
public class LeadsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "name", length = 120)
    private String name;

    @Column(name = "phone", length = 120)
    private String phone;

    @Column(name = "Foco")
    @Enumerated(EnumType.STRING)
    private Foco foco;

    @Column(name = "email")
    private String email;


}
