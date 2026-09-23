package br.com.auth_core_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Ativa as configurações de segurança personalizadas
public class SecurityConfigurations {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Desativa a proteção CSRF, pois como vamos usar JWT (token), não somos vulneráveis a esse tipo de ataque
                .csrf(csrf -> csrf.disable())
                // Muda o gerenciamento de sessão de STATEFUL (guarda estado) para STATELESS (não guarda estado, comum em APIs REST)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura quais rotas são abertas e quais precisam de token
                .authorizeHttpRequests(authorize -> authorize
                        // Libera as rotas de login e cadastro para qualquer um acessar (afinal, o usuário ainda não tem token)
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        // Qualquer outra rota precisará estar autenticada
                        .anyRequest().authenticated()
                )
                .build();
    }

    // Esse Bean é necessário para podermos injetar o AuthenticationManager no nosso controller de login mais tarde
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Esse Bean diz ao Spring que as senhas no nosso banco estarão criptografadas com o algoritmo BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
