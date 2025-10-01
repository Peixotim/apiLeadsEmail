package digital.rj.apileadsandemails.Security.Entity;

public record TokenResponse(
        String token,
        String type,
        long expiresIn
) {
}
