package br.com.auth_core_api.security;

import br.com.auth_core_api.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // O Spring vai injetar o valor do "secret" que configurarmos no application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            // O algoritmo de criptografia do token. O "secret" é a senha que só nossa API conhece
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("auth-api") // Quem está emitindo o token (nossa API)
                    .withSubject(user.getLogin()) // Quem é o dono do token (o usuário logado)
                    .withExpiresAt(genExpirationDate()) // Tempo de expiração
                    .sign(algorithm); // Assina e gera o token
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token) // Verifica se o token é válido, se não expirou, e se a assinatura bate
                    .getSubject(); // Pega o dono do token (o login do usuário)
        } catch (JWTVerificationException exception) {
            return ""; // Retorna string vazia se der erro (token inválido/expirado)
        }
    }

    // Método auxiliar para definir o tempo de validade do token (Ex: 2 horas)
    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
