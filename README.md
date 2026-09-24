# API de Autenticação e Autorização com Spring Security e JWT

Este projeto é uma API RESTful desenvolvida em Java com Spring Boot, focada em implementar um sistema robusto de autenticação e autorização utilizando **Spring Security** e **JSON Web Tokens (JWT)**. 

Foi construído como um projeto de portfólio para demonstrar boas práticas de segurança, criptografia de senhas e proteção de rotas (Stateless) em aplicações backend.

## 🚀 Tecnologias Utilizadas

*   **Java 17+**
*   **Spring Boot 3+** (Web, Security, Data JPA)
*   **JWT (java-jwt da Auth0)** para geração e validação de tokens
*   **H2 Database** (Banco de dados em memória para testes e desenvolvimento rápido)
*   **Maven** para gerenciamento de dependências

## ⚙️ Funcionalidades

*   **Cadastro de Usuário:** Registro de novos usuários com criptografia de senha utilizando `BCryptPasswordEncoder`.
*   **Login e Autenticação:** Validação de credenciais e geração de token JWT.
*   **Autorização Baseada em Roles:** Suporte a perfis de acesso (`ADMIN` e `USER`).
*   **Proteção de Rotas:** Filtro customizado (`SecurityFilter`) que intercepta requisições, valida o token via cabeçalho `Authorization` e libera ou bloqueia o acesso aos endpoints.

## 🛠️ Como executar o projeto

1. Clone este repositório:

```bash
git clone https://github.com/JeannMatheuss/auth-core-api
```

2. Entre na pasta do projeto:

```Bash
cd auth-core-api
```

3. Execute a aplicação usando o Maven:

```Bash
./mvnw spring-boot:run
```
(A API estará disponível em http://localhost:8080)

## 🔗 Endpoints da API

### 1. Registrar Usuário (Público)

- Rota: POST /auth/register

- Corpo da Requisição (JSON):

```json
JSON
{
  "login": "usuario@email.com",
  "password": "senha123",
  "role": "ADMIN" 
}
```

Respostas: 200 OK (Sucesso) ou 400 Bad Request (Usuário já existe).

### 2. Login (Público)

- Rota: POST /auth/login

- Corpo da Requisição (JSON):

```json
JSON
{
  "login": "usuario@email.com",
  "password": "senha123"
}
```

- Resposta (JSON):

```json
JSON
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3. Acessando Rotas Protegidas

Para acessar qualquer outra rota da API que não seja de autenticação, você deve incluir o token JWT no cabeçalho (Header) da requisição:

*   **Key:** `Authorization`
*   **Value:** `Bearer SEU_TOKEN_AQUI`

## 🔮 Próximos Passos (Melhorias Futuras)

- [ ] Migração do banco de dados H2 para PostgreSQL.

- [ ] Documentação da API com Swagger/OpenAPI.

- [ ] Implementação de testes unitários com JUnit e Mockito.

- [ ] Adição de Refresh Tokens.

Desenvolvido com ☕ e código por [Jean Matheus](https://www.linkedin.com/in/jeanmatheusmoliveira/).
