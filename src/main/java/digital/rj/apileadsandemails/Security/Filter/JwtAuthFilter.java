package digital.rj.apileadsandemails.Security.Filter;

import digital.rj.apileadsandemails.Security.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 0) Ignora preflight CORS e rotas públicas
        String path = request.getServletPath();
        if (HttpMethod.OPTIONS.matches(request.getMethod())
                || path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1) Lê Authorization
        String auth = request.getHeader("Authorization");

        System.out.println("[JWT] Authorization : " + auth + " | path=" + request.getServletPath());
        if (StringUtils.hasText(auth)) {
            // tolera espaços e case
            String trimmed = auth.trim();
            boolean hasBearer = trimmed.regionMatches(true, 0, "Bearer ", 0, 7);
            if (hasBearer && trimmed.length() > 7) {
                String token = trimmed.substring(7).trim();
                try {
                    // 2) Extrai subject (username)
                    String username = jwtService.extractUsername(token);

                    // 3) Se ainda não autenticado no contexto, valida o token
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails user = userDetailsService.loadUserByUsername(username);

                        if (jwtService.isValid(token, user)) {
                            var authToken = new UsernamePasswordAuthenticationToken(
                                    user, null, user.getAuthorities()
                            );
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        } else {
                            // Opcional: logar motivo
                            // log.debug("JWT inválido para user {}", username);
                        }
                    }
                } catch (Exception e) {
                    // Opcional: logar para depuração
                    // log.debug("Falha ao processar JWT: {}", e.getMessage());
                }
            }
        }

        // 4) Segue a cadeia SEMPRE
        filterChain.doFilter(request, response);

    }
}