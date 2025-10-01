package digital.rj.apileadsandemails.Security.Entity;

import jakarta.validation.constraints.NotBlank;

public record SingupRequest(

        @NotBlank String username,
        @NotBlank String password
) {
}
