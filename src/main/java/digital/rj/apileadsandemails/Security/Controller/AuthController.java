package digital.rj.apileadsandemails.Security.Controller;

import digital.rj.apileadsandemails.Security.Entity.LoginRequest;
import digital.rj.apileadsandemails.Security.Entity.SingupRequest;
import digital.rj.apileadsandemails.Security.Entity.TokenResponse;
import digital.rj.apileadsandemails.Security.Entity.Users;
import digital.rj.apileadsandemails.Security.Repository.UsersRepository;
import digital.rj.apileadsandemails.Security.Service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UsersRepository repository;
    private final PasswordEncoder passwordEnconder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SingupRequest request){
        if(repository.existsByUsername(request.username())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exist !");
        }

        var user = Users.builder()
                .username(request.username())
                .password(passwordEnconder.encode(request.password()))
                .build();

        repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(),request.password())
        );
        var user = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new TokenResponse(token,"Bearer",jwtService.getExpiration()));
    }
}
