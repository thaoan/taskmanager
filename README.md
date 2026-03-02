# 🚀 TaskManager API

Aplicação RESTful de gerenciamento de tarefas construída com **Spring Boot 3**. Este projeto serve como exemplo de arquitetura básica, uso de Spring Data JPA, segurança e orquestração via Docker.

---

## 🛠 Tecnologias

- Java 17
- Spring Boot 3
- Spring Data JPA (Hibernate)
- MySQL 8.0 (containerizado via Docker)
- Spring Security (configuração mínima)
- Maven (build e dependências)
- Lombok (redução de boilerplate)

---

## 🚀 Iniciando o projeto

### Pré-requisitos

- Docker & Docker Compose
- JDK 17
- Maven (ou use o wrapper `./mvnw`)

### 1. Configurar variáveis de ambiente

Crie um arquivo `.env` na raiz com as configurações de banco:

```env
DB_USER=root
DB_PASSWORD=senha_forte_aqui
DB_NAME=taskmanager_db
```

> **ATENÇÃO:** `.env` está listado em `.gitignore`. Não compartilhe credenciais sensíveis.

### 2. Subir banco de dados MySQL

```bash
docker-compose up -d
```

A imagem cria o banco definido pelo `.env` e abre a porta padrão (`3306`).

### 3. Executar a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação rodará em `http://localhost:8080`.

---
## 📖 Documentação da API
Com a aplicação rodando, acesse o Swagger UI para testar os endpoints:
http://localhost:8080/swagger-ui/index.html

## 📦 Endpoints disponíveis

| Método | URI                    | Descrição                     |
|--------|------------------------|-------------------------------|
| GET    | `/api/tasks`           | Listar todas as tarefas       |
| POST   | `/api/tasks`           | Criar nova tarefa             |
| PUT    | `/api/tasks/{id}`      | Atualizar tarefa por ID       |
| DELETE | `/api/tasks/{id}`      | Remover tarefa por ID         |

Teste com Postman, Insomnia ou curl.

---

## 🧪 Testes

O projeto inclui testes básicos de integração/união no pacote `com.thaoan.taskmanager`.
Execute:

```bash
./mvnw test
```

---

## 📁 Estrutura de pastas

```
src/main/java/com/thaoan/taskmanager/  (código-fonte)
src/main/resources/                   (configurações e templates)
src/test/java/...                     (testes)
```

---

## 🤝 Contribuições

Sinta-se à vontade para enviar pull requests ou abrir issues.

---

Desenvolvido por Thaoan Zamboni 👨‍💻
