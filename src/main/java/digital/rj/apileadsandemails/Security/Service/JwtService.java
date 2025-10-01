package digital.rj.apileadsandemails.Security.Service;

import digital.rj.apileadsandemails.Security.Entity.Users;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Data
@Service
public class JwtService {

    private final Key key; //Secret Key
    private final long expiration; // Tempo de expiracao
    private final String issuer; // Emissor

    public JwtService(
            @Value("${app.jwt.secret}") String secretB64,
            @Value("${app.jwt.expiration-ms}") Long expiration,
            @Value("${app.jwt.issuer}") String issuer
    ){
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretB64));
        this.expiration = expiration;
        this.issuer = issuer;
    }


    /// Funcao de gerar o token
    public String generateToken(UserDetails user){
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setIssuer(issuer) // "Emissora"
                .setSubject(user.getUsername()) //"Quem fez"
                .setIssuedAt(now) // "Feito quando"
                .setExpiration(exp) //"Quando vai se expirar"
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /// Funcao de extrair o nome do usuario
    public String extractUsername(String token){
        var parser = Jwts.parserBuilder().setSigningKey(key).build();
        var claims = parser.parseClaimsJws(token).getBody();


        if(issuer != null && !issuer.isBlank()){
            String iss = claims.getIssuer();
            if(iss == null && !iss.equalsIgnoreCase(issuer)){
                throw new JwtException("Issuer invalido !");
            }
        }

//        Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJwt(token)
//                .getBody()
//                .getSubject();
        return claims.getSubject();
    }


    /// Funcao de validacao do usuario
    public boolean isValid(String token,UserDetails user){
        try{
            var parser = Jwts.parserBuilder().setSigningKey(key).build();
            var claims =  parser.parseClaimsJws(token).getBody();

            //Verifica o Issuer
            if (issuer != null && !issuer.isBlank()) {
                String iss = claims.getIssuer();
                if (iss == null || !iss.equals(issuer)) return false;
            }

            String username = claims.getSubject();
            Date exp = claims.getExpiration();

            return username != null
                    && username.equals(user.getUsername())
                    && exp != null
                    && exp.after(new Date());

        }catch(JwtException | IllegalArgumentException e){
            return false;
        }
    }

    /// Funcao para verificar se está expirado

    public boolean isExpired(String token){
        Date exp = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(token)
                .getBody()
                .getExpiration();
        return exp.before(new Date());
    }

}
