package digital.rj.apileadsandemails.Security.Jwt.Service;

import digital.rj.apileadsandemails.Security.Entity.Users;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class JwtService {

    private final Key key;
    private final long expirationMs; //Quando expira

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs
            ){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    //Geracao do token
    public String generateToken(Users user){
        Date now = new Date(); //Data atual
        Date exp = new Date(now.getTime() + expirationMs); //Data de expiracao
        return Jwts.builder()
                .setSubject(user.getUsername()) //Quem está gerando
                .setIssuedAt(now) //Emitido quando (Feito quando)
                .setExpiration(exp) //Expira em
                .signWith(key, SignatureAlgorithm.ES256)
                .compact();

    }

    //Retorna o emissor ou como está definido o Username
    public String extractUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(token)
                .build()
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }

    //Validacao
    public boolean isValid(String token, Users user){
        try{
            String username = extractUsername(token);
            return username.equalsIgnoreCase(user.getUsername());
        }catch(JwtException | IllegalArgumentException e){
            return false;
        }
    }

    private boolean isExpired(String token) {
        Date exp = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return exp.before(new Date());
    }
}

