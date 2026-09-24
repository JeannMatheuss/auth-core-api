package br.com.auth_core_api.controller;

import br.com.auth_core_api.dto.AuthenticationDTO;
import br.com.auth_core_api.dto.LoginResponseDTO;
import br.com.auth_core_api.dto.RegisterDTO;
import br.com.auth_core_api.security.TokenService;
import br.com.auth_core_api.user.User;
import br.com.auth_core_api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        // Cria um "token" do Spring Security com o login e senha recebidos
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

        // O AuthenticationManager vai usar aquele nosso AuthorizationService para checar no banco se a senha bate
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // Se a senha bater, geramos o token JWT
        var token = tokenService.generateToken((User) auth.getPrincipal());

        // Devolvemos o token na resposta
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        // Verifica se já existe um usuário com esse login
        if (this.repository.findByLogin(data.login()) != null) {
            return ResponseEntity.badRequest().build(); // Retorna erro 400 se já existir
        }

        // Criptografa a senha antes de salvar no banco! (NUNCA salve a senha pura)
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        // Cria o novo usuário com a senha criptografada
        User newUser = new User(data.login(), encryptedPassword, data.role());

        // Salva no banco de dados
        this.repository.save(newUser);

        return ResponseEntity.ok().build(); // Retorna sucesso 200
    }
}
