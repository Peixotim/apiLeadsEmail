package digital.rj.apileadsandemails.Email.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity(name = "tb_email")
@Table(name = "tb_email")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class EmailModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "to_Email",nullable = false)
    private String to;

    @Column(name = "subject_email",nullable = false)
    private String subject;

    @Column(name = "body_email",nullable = false)
    private String body;
}
