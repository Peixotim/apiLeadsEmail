package digital.rj.apileadsandemails.Leads.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
@Entity(name = "tb_leads")
@Table(name = "tb_leads")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeadsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 120,nullable = false)
    private String name;

    @Column(name = "phone", length = 120,nullable = false)
    private String phone;

    @Column(name = "Foco",nullable = false)
    @Enumerated(EnumType.STRING)
    private Foco foco;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "enterprise",nullable = true)
    private String enterprise;

}
