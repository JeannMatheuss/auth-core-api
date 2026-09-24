package br.com.auth_core_api.security;

import br.com.auth_core_api.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Transforma a classe num componente gerenciado pelo Spring
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Pega o token da requisição
        var token = this.recoverToken(request);

        if (token != null) {
            // 2. Valida o token e pega o login do dono dele
            var login = tokenService.validateToken(token);

            // 3. Busca o usuário no banco de dados
            UserDetails user = userRepository.findByLogin(login);

            // 4. Se o usuário existir, dizemos ao Spring Security que ele está autenticado
            if (user != null) {
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 5. Continua o fluxo da requisição (seja para o controller ou para bloquear o acesso)
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extrair o token do cabeçalho "Authorization"
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;

        // O padrão web é enviar o token assim: "Bearer dkjhaskdjhaskjdh..."
        // Então nós tiramos a palavra "Bearer " para ficar só com o código do token
        return authHeader.replace("Bearer ", "");
    }
}
