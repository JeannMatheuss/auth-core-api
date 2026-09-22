package br.com.auth_core_api.user;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User() {}

    public User(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    // --- MÉTODOS DO SPRING SECURITY (UserDetails) ---

    // quais são as permissões desse usuário
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Se a role for ADMIN, ele tem permissão de ADMIN e também de USER comum.
        if(this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }
        // Se não, ele só tem permissão de USER comum.
        else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    // O Spring precisa saber qual campo é a senha
    @Override
    public String getPassword() {
        return this.password;
    }

    // O Spring precisa saber qual campo é o "login" do usuário
    @Override
    public String getUsername() {
        return this.login;
    }

    // Daqui para baixo, são configurações de bloqueio de conta.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() { return id; }
    public String getLogin() { return login; }
    public UserRole getRole() { return role; }

}