package digital.rj.apileadsandemails.Security.Configuration;

import digital.rj.apileadsandemails.Security.Filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthFilter authFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        var p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // HABILITA CORS no HttpSecurity (sem isso o Spring não aplica seu CorsConfigurationSource)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        // Preflight deve sempre passar
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Auth público
                        .requestMatchers("/auth/**").permitAll()

                        // E-mail público (POST /email). Ajuste se quiser proteger.
                        .requestMatchers(HttpMethod.POST, "/email/**").permitAll()

                        // (Exemplos) Proteções específicas
                        .requestMatchers(HttpMethod.GET, "/leads/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/leads/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/email/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/email/**").authenticated()

                        // Demais rotas
                        .anyRequest().permitAll()
                );

        // Filtro JWT (se aplicável) antes do UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configuração global de CORS
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Coloque aqui as origens do seu front
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://127.0.0.1:3000"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Permita todos os headers do preflight, incluindo Content-Type e Authorization
        config.setAllowedHeaders(List.of("*"));
        // Se precisar ler cabeçalhos no front
        config.setExposedHeaders(List.of("Location"));
        // Se usar cookies/credenciais entre domínios (não é o caso do fetch simples com Bearer)
        config.setAllowCredentials(true);
        // Tempo que o preflight pode ficar em cache no navegador
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica para todas as rotas
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}