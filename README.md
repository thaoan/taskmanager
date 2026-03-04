# TaskManager API

RESTful API for task management built with Spring Boot 3.4.3 and Java 21. Features JWT authentication, complete task and category management, with Swagger documentation.

## 🛠 Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.4.3 |
| Authentication | Spring Security + JWT |
| Database | MySQL 8.0 (Docker) |
| Documentation | Swagger/OpenAPI 3 |
| Build | Maven |

## ✨ Features

- **JWT Authentication** - Secure token-based user login
- **User Management** - Registration, profile updates, listing
- **Task Management** - Full CRUD with filtering by user, category, status, and title
- **Categories** - Organize tasks with custom categories
- **Pagination & Sorting** - Efficient data retrieval
- **JPA Auditing** - Automatic timestamp tracking (createdAt, updatedAt)
- **Global Exception Handling** - Standardized error responses
- **Swagger UI** - Interactive API documentation
- **Environment Security** - Credentials managed via .env file

## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose
- JDK 21
- Maven

### Setup

1. Create `.env` file:
```env
DB_URL=jdbc:mysql://localhost:3306/taskmanager_db
DB_USER=root
DB_PASSWORD=your_password
JWT_SECRET=your_jwt_secret_key_minimum_32_characters
```

2. Start database:
```bash
docker-compose up -d
```

3. Run application:
```bash
./mvnw spring-boot:run
```

API available at `http://localhost:8080`

## 📚 API Endpoints

### Authentication
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/login` | User login | No |

### Users
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/users` | Register new user | No |
| GET | `/api/users` | List users | Yes |
| PUT | `/api/users/{id}` | Update user | Yes |
| DELETE | `/api/users/{id}` | Delete user | Yes |

### Tasks
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/tasks` | List tasks (with filters) | Yes |
| POST | `/api/tasks` | Create task | Yes |
| PUT | `/api/tasks/{id}` | Update task | Yes |
| DELETE | `/api/tasks/{id}` | Delete task | Yes |

**Task Filters:** userId, categoryId, completed, title, page, size

### Categories
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/categories` | List categories | Yes |
| POST | `/api/categories` | Create category | Yes |
| PUT | `/api/categories/{id}` | Update category | Yes |
| DELETE | `/api/categories/{id}` | Delete category | Yes |

## 🔐 Authentication

1. Login via `POST /api/auth/login` to get JWT token
2. Add token to requests: `Authorization: Bearer <token>`
3. All endpoints require authentication except user registration and login

**Using Swagger UI:**
- Click "Authorize" button
- Enter: `Bearer YOUR_TOKEN`

## 📂 Project Structure

```
src/main/java/com/thaoan/taskmanager/
├── controller/     # REST endpoints
├── service/        # Business logic
├── repository/     # Data access (JPA)
├── models/         # JPA entities
├── dto/            # Data transfer objects
├── security/       # JWT & security config
├── exception/      # Exception handling
└── config/         # Application config

src/test/java/     # Unit & integration tests
```

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run specific test
./mvnw test -Dtest=TaskServiceTest

# With coverage
./mvnw test jacoco:report
```

## 📦 Production Build

```bash
./mvnw clean package -DskipTests
java -jar target/taskmanager-0.0.1-SNAPSHOT.jar
```

## 📖 Documentation

**Swagger UI:** http://localhost:8080/swagger-ui/index.html
**API Docs:** http://localhost:8080/v3/api-docs
**Detailed Technical Specs:** [DOCUMENTATION.md](./DOCUMENTATION.md)
## 👨‍💻 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/your-feature`
3. Commit: `git commit -m 'Add feature'`
4. Push: `git push origin feature/your-feature`
5. Open Pull Request

## 📄 License

MIT License - see LICENSE file for details

---

**Developed by Thaoan Zamboni** 👨‍💻