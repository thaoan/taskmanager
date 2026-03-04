# TaskManager API

A robust and scalable RESTful API for task management built with Spring Boot 3.4.3 and Java 21. This project follows industry best practices, including DTO patterns, automated auditing, and comprehensive error handling.

## 🛠 Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 21 (LTS) |
| Framework | Spring Boot 3.4.3 |
| Persistence | Spring Data JPA (Hibernate) |
| Database | MySQL 8.0 (Dockerized) |
| Security | Spring Security (Basic Auth) |
| Validation | Jakarta Bean Validation |
| Documentation | Swagger / OpenAPI 3 |
| Build Tool | Maven |
| Testing | JUnit 5 & Mockito |

## 🌟 Key Features

- **Java 21 Records**: Used for immutable and clean Data Transfer Objects (DTOs)
- **Pagination & Sorting**: Efficient data fetching using Pageable to handle large datasets
- **Automated Auditing**: JPA Auditing tracks createdAt and updatedAt timestamps automatically
- **Global Exception Handling**: Centralized error management returning standardized JSON responses (404, 400, 500)
- **Environment Security**: Integration with dotenv-java to keep sensitive credentials out of the source code
- **Unit Testing**: Business logic validated with Mockito to ensure reliability

## 📁 Project Structure

```
src/main/java/com/thaoan/taskmanager/
  ├─ config/         # Security and Audit configurations
  ├─ controller/     # REST Endpoints
  ├─ dto/            # Request/Response Records (Data Transfer Objects)
  ├─ exception/      # Custom exceptions and Global Handler
  ├─ models/         # JPA Entities
  ├─ repository/     # Spring Data JPA interfaces
  └─ service/        # Business logic and transaction management

src/test/java/       # Unit and Integration tests
```

## 🚀 Getting Started

### Prerequisites

- Docker & Docker Compose
- JDK 21
- Maven (or use the provided `./mvnw` wrapper)

### Environment Setup

Create a `.env` file in the root directory:

```
DB_URL=jdbc:mysql://localhost:3306/taskmanager_db
DB_USER=root
DB_PASSWORD=your_secure_password
```

### Spin up the Database

```bash
docker-compose up -d
```

### Run the Application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## 🌐 API Documentation

Once the app is running, explore and test the endpoints via Swagger UI:

👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Endpoints Overview

| Method | Endpoint | Description | Query Params |
|--------|----------|-------------|--------------|
| GET | `/api/tasks` | List all tasks (Paginated) | page, size, sort |
| GET | `/api/tasks/filter` | Filter by status (Paginated) | completed, page, size |
| POST | `/api/tasks` | Create a new task | - |
| PUT | `/api/tasks/{id}` | Update existing task | - |
| DELETE | `/api/tasks/{id}` | Remove task (Safe delete) | - |

## 🧪 Testing

Run the automated test suite (Unit + Integration tests):

```bash
./mvnw test
```

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

### Upcoming Roadmap

- [ ] Implement JWT (JSON Web Token) Authentication
- [ ] Add Redis caching for frequently accessed tasks
- [ ] Develop a React/Next.js Frontend

---

Developed with  by Thaoan Zamboni 👨‍💻