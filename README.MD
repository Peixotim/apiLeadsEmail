# 📧 API Leads and Emails - RJGLOBAL

Este projeto é uma API construída com **Spring Boot 3** e **Java 17**, responsável pelo gerenciamento de **leads** e pelo **envio de e-mails** da plataforma RJGLOBAL.  
Ela fornece endpoints para autenticação, cadastro e consulta de leads, envio de mensagens e integração com serviços externos.

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Security (JWT)**
- **Spring Data JPA**
- **PostgreSQL**
- **Supabase** (autenticação e persistência)
- **Lombok**
- **Maven**

---

## 📂 Estrutura do Projeto

```
src/main/java/digital/rj/apileadsandemails
│
├── Security/                  # Configuração de segurança (JWT, filtros, CORS)
│   ├── Configuration/          # Beans de configuração (SecurityConfig, etc.)
│   ├── Filter/                 # JwtAuthFilter e outros filtros
│
├── Leads/                      # Domínio de leads
│   ├── Controller/             # Controllers REST para leads
│   ├── Model/                  # Entidades e enums (Lead, Foco, etc.)
│   ├── Repository/             # Repositórios JPA
│   ├── Service/                # Regras de negócio
│
├── Email/                      # Domínio de e-mails
│   ├── Controller/             # Controllers REST para envio de e-mails
│   ├── Model/                  # DTOs de requisição e resposta
│   ├── Service/                # Serviços para envio de e-mails
│
└── ApiLeadsAndEmailsApplication.java   # Classe principal da aplicação
```

---

## 🔑 Autenticação

A API utiliza **JWT (JSON Web Token)** para autenticação e autorização.

### Fluxo básico:
1. O usuário autentica via `/auth/login` com email/senha.
2. Recebe um token JWT válido.
3. Deve incluir o token no header `Authorization: Bearer <token>` para acessar rotas protegidas.

Rotas públicas como `/auth/**` e `POST /email` estão liberadas.

---

## 📡 Endpoints Principais

### Autenticação
- `POST /auth/register` → Registrar novo usuário
- `POST /auth/login` → Autenticar e receber token JWT

### Leads
- `GET /leads` → Listar leads (protegido)
- `POST /leads` → Criar lead
- `DELETE /leads/{id}` → Remover lead (protegido)

### E-mails
- `POST /email` → Enviar e-mail (público)
  ```json
  {
    "name": "Pedro Almeida",
    "email": "contato@exemplo.com",
    "phone": "(31) 99999-9999",
    "enterprise": "Minha Empresa",
    "foco": "POSGRADUCAO",
    "body": "Mensagem do contato..."
  }
  ```

---

## 🌱 Variáveis de Ambiente

Antes de rodar a aplicação, defina as seguintes variáveis de ambiente (por exemplo, em um arquivo `.env` ou no sistema operacional):

```bash
# Banco de Dados
DB_URL=jdbc:postgresql://localhost:5432/db
DB_USERNAME=postgres
DB_PASSWORD=postgres

# E-mail (SMTP)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_PROTOCOL=smtp
MAIL_USERNAME=seuemail@gmail.com
MAIL_PASSWORD=senha_de_app_gerada_no_provedor

# JWT
SECRET_KEY=sua_chave_secreta_segura
```

> ⚠️ **Atenção:**
> - O `MAIL_PASSWORD` deve ser uma **senha de aplicativo** gerada no Gmail (ou no provedor de e-mail que você utilizar).
> - Nunca versionar o `.env` com dados reais.
> - Em produção, configure essas variáveis no servidor ou container (ex.: Docker, Supabase, Railway, Heroku etc).

---

## ⚙️ Configuração e Execução

### Pré-requisitos
- **Java 17+**
- **Maven**
- **PostgreSQL** rodando localmente ou remoto

### Rodando a aplicação
```bash
# Clonar repositório
git clone https://github.com/seu-usuario/api-leads-and-emails.git
cd api-leads-and-emails

# Rodar aplicação (dev)
mvn spring-boot:run

# Buildar aplicação
mvn clean package
java -jar target/api-leads-and-emails-0.0.1-SNAPSHOT.jar
```

---

## 🔒 CORS

A configuração de CORS libera a origem do frontend (`http://localhost:3000`) para permitir que o site RJGLOBAL consuma a API sem erros de preflight.  
Quando for para produção, configure o domínio correto no `SecurityConfig`.

---

## 🐳 Docker

Um `Dockerfile` pode ser criado para facilitar o deploy:

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/api-leads-and-emails-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build & Run:
```bash
docker build -t rjglobal-api .
docker run -p 8080:8080 rjglobal-api
```

---

## 📜 Licença

Este projeto é distribuído sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
