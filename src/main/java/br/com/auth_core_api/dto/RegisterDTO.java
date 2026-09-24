package br.com.auth_core_api.dto;

import br.com.auth_core_api.user.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
