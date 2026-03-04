# TaskManager API - Complete Documentation

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Installation & Setup](#installation--setup)
4. [Authentication](#authentication)
5. [API Endpoints](#api-endpoints)
6. [Data Models](#data-models)
7. [Request/Response Examples](#requestresponse-examples)
8. [Error Handling](#error-handling)
9. [Best Practices](#best-practices)
10. [Troubleshooting](#troubleshooting)

---

## Overview

**TaskManager API** is a production-ready RESTful API designed for managing tasks, users, and categories. It provides secure authentication via JWT tokens, advanced filtering capabilities, and comprehensive API documentation through Swagger/OpenAPI 3.

### Key Characteristics

- **Type**: RESTful Web Service
- **Architecture**: Layered (Controller → Service → Repository)
- **Authentication**: JWT (JSON Web Tokens) via Auth0 Java-JWT
- **Database**: MySQL 8.0 with Hibernate JPA
- **Documentation**: Swagger UI / OpenAPI 3.0
- **Language**: Java 21 (LTS)
- **Framework**: Spring Boot 3.4.3

### Supported Operations

- User registration and management
- Task creation, retrieval, update, and deletion
- Category creation and organization
- Advanced task filtering and pagination
- Role-based access control (authentication required)

---

## Architecture

### High-Level Design

```
┌─────────────────────────────────────────────────────────┐
│                    Client Application                    │
└────────────────────────┬────────────────────────────────┘
                         │ HTTP/JSON
                         ▼
┌─────────────────────────────────────────────────────────┐
│              Spring Boot REST API Layer                  │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Controllers (REST Endpoints)                   │    │
│  ├─────────────────────────────────────────────────┤    │
│  │  Filters (JWT Validation)                       │    │
│  ├─────────────────────────────────────────────────┤    │
│  │  Services (Business Logic)                      │    │
│  ├─────────────────────────────────────────────────┤    │
│  │  Repositories (Data Access)                     │    │
│  └─────────────────────────────────────────────────┘    │
└────────────────────────┬────────────────────────────────┘
                         │ JDBC/Hibernate
                         ▼
┌─────────────────────────────────────────────────────────┐
│           MySQL 8.0 Database (Docker)                   │
│            • users                                      │
│            • tasks                                      │
│            • categories                                 │
└─────────────────────────────────────────────────────────┘
```

### Layer Responsibilities

#### Controller Layer
- Handles HTTP requests and responses
- Validates input using Jakarta Validation annotations
- Returns appropriate HTTP status codes
- Routes requests to service layer

#### Service Layer
- Contains business logic
- Manages transactions
- Performs complex validations
- Coordinates between repositories

#### Repository Layer
- Interfaces with database via Spring Data JPA
- Provides query methods
- Manages entity lifecycle
- Uses Hibernate as ORM

#### Security Layer
- JWT token generation and validation
- User authentication and authorization
- Password encryption with BCrypt
- Token-based session management

---

## Installation & Setup

### System Requirements

- **OS**: Linux, macOS, or Windows
- **Java**: JDK 21 or later
- **Docker**: Latest version with Docker Compose
- **Maven**: 3.6+ or use provided wrapper
- **RAM**: Minimum 2GB available for Docker and application

### Step-by-Step Installation

#### 1. Clone Repository
```bash
git clone https://github.com/thaoan/taskmanager.git
cd taskmanager
```

#### 2. Environment Configuration

Create `.env` file in project root:
```env
# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/taskmanager_db
DB_USER=root
DB_PASSWORD=secure_password_here

# JWT Configuration
JWT_SECRET=your_super_secret_jwt_key_minimum_32_characters_required
JWT_EXPIRATION=86400000

# Spring Configuration
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false
```

**Important:** 
- `JWT_SECRET` must be at least 32 characters long
- `JWT_EXPIRATION` is in milliseconds (86400000 = 24 hours)
- Keep `.env` file secure and never commit to version control

#### 3. Start Database

```bash
# Start MySQL container
docker-compose up -d

# Verify database is running
docker ps | grep taskmanager-db

# Check database logs
docker logs taskmanager-db
```

#### 4. Build Application

```bash
# Clean build
./mvnw clean package -DskipTests

# Or just compile
./mvnw clean compile
```

#### 5. Run Application

```bash
# Option 1: Use Maven wrapper
./mvnw spring-boot:run

# Option 2: Run compiled JAR
java -jar target/taskmanager-0.0.1-SNAPSHOT.jar

# Option 3: Docker container
docker-compose up --build
```

#### 6. Verify Installation

```bash
# Check if API is running
curl http://localhost:8080/swagger-ui/index.html

# Check health endpoint (if available)
curl http://localhost:8080/actuator/health
```

### Initial Data Setup

The database will be created automatically with the configured JPA schema generation. To add initial data:

```sql
-- Example: Add initial users via API

curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "SecurePassword123",
    "name": "Admin User"
  }'
```

---

## Authentication

### JWT Token-Based Authentication

#### How It Works

1. **User Submits Credentials** → POST `/api/auth/login`
2. **Server Validates** → Checks email and password against database
3. **Token Generated** → Creates signed JWT token
4. **Client Stores Token** → Saves in local storage or session
5. **Client Sends Token** → Includes in Authorization header for protected requests
6. **Server Validates Token** → Checks signature and expiration
7. **Access Granted/Denied** → Proceeds or returns 401 Unauthorized

#### JWT Token Structure

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

**Decoded parts:**
- **Header**: Algorithm (HS256) and type (JWT)
- **Payload**: User subject, email, issued time
- **Signature**: Verification hash

#### Login Endpoint

**Request:**
```http
POST /api/auth/login HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePassword123"
}
```

**Success Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjgzMTQyNTAwLCJleHAiOjE2ODMyMjg5MDB9.signature"
}
```

**Failure Response (401 Unauthorized):**
```json
{
  "timestamp": "2026-03-04T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid credentials",
  "path": "/api/auth/login"
}
```

#### Using Tokens in Requests

**cURL Example:**
```bash
# Store token
TOKEN="your_jwt_token_here"

# Use in request
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/tasks
```

**JavaScript/Fetch Example:**
```javascript
const token = localStorage.getItem('jwtToken');

fetch('http://localhost:8080/api/tasks', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
.then(response => response.json())
.then(data => console.log(data));
```

**Axios Example:**
```javascript
const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

api.get('/api/tasks').then(response => console.log(response.data));
```

#### Protected Routes

Routes requiring authentication:
- `GET /api/tasks` - List tasks
- `POST /api/tasks` - Create task
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task
- `GET /api/users` - List users
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/categories` - List categories
- `POST /api/categories` - Create category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

#### Public Routes

Routes without authentication requirement:
- `POST /api/users` - User registration
- `POST /api/auth/login` - User login
- `/swagger-ui/**` - Swagger documentation
- `/v3/api-docs/**` - OpenAPI schema

#### Token Expiration

Tokens expire after the duration specified in `JWT_EXPIRATION` environment variable:

```env
# Token valid for 24 hours
JWT_EXPIRATION=86400000
```

When token expires:
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Token expired"
}
```

**Solution:** User must log in again to receive a new token.

---

## API Endpoints

### Authentication Endpoints

#### POST /api/auth/login
Authenticates user and returns JWT token.

**Request:**
```json
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Error Responses:**
- `400 Bad Request` - Invalid input format
- `401 Unauthorized` - Invalid credentials
- `500 Internal Server Error` - Server error

---

### User Endpoints

#### POST /api/users
Register a new user account.

**Request:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword123"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

**Validation Rules:**
- `name`: Required, 2-100 characters
- `email`: Required, valid email format, must be unique
- `password`: Required, minimum 8 characters, must contain uppercase, lowercase, and number

---

#### GET /api/users
List all registered users. **Requires authentication.**

**Query Parameters:** None

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane@example.com"
  }
]
```

---

#### GET /api/users/{id}
Retrieve a specific user by ID. **Requires authentication.**

**Path Parameters:**
- `id` (Long): User ID

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

**Error Response (404 Not Found):**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "User with ID 999 not found"
}
```

---

#### PUT /api/users/{id}
Update user information. **Requires authentication.**

**Path Parameters:**
- `id` (Long): User ID

**Request:**
```json
{
  "name": "John Updated",
  "email": "john.updated@example.com",
  "password": "NewSecurePassword123"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Updated",
  "email": "john.updated@example.com"
}
```

---

#### DELETE /api/users/{id}
Delete a user account. **Requires authentication.**

**Path Parameters:**
- `id` (Long): User ID

**Response (204 No Content):**
Empty response body with status code 204.

---

### Task Endpoints

#### GET /api/tasks
List all tasks with optional filters. **Requires authentication.**

**Query Parameters:**
- `userId` (Long, optional): Filter by user ID
- `categoryId` (Long, optional): Filter by category ID
- `completed` (Boolean, optional): Filter by completion status
- `title` (String, optional): Search by task title (partial match)
- `page` (Integer, default: 0): Page number for pagination
- `size` (Integer, default: 10): Number of items per page
- `sort` (String, default: "id"): Field to sort by

**Example Request:**
```
GET /api/tasks?userId=1&categoryId=2&completed=false&title=Shopping&page=0&size=10
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Buy groceries",
      "description": "Milk, eggs, bread, butter",
      "completed": false,
      "userId": 1,
      "categoryId": 2,
      "createdAt": "2026-03-04T10:30:00",
      "updatedAt": "2026-03-04T10:30:00"
    },
    {
      "id": 2,
      "title": "Call dentist",
      "description": "Schedule appointment",
      "completed": true,
      "userId": 1,
      "categoryId": 3,
      "createdAt": "2026-03-03T14:20:00",
      "updatedAt": "2026-03-04T09:15:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    }
  },
  "totalElements": 2,
  "totalPages": 1,
  "last": true,
  "numberOfElements": 2,
  "first": true,
  "empty": false
}
```

---

#### POST /api/tasks
Create a new task. **Requires authentication.**

**Request:**
```json
{
  "title": "Buy groceries",
  "description": "Milk, eggs, bread, butter",
  "completed": false,
  "userId": 1,
  "categoryId": 2
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "Buy groceries",
  "description": "Milk, eggs, bread, butter",
  "completed": false,
  "userId": 1,
  "categoryId": 2,
  "createdAt": "2026-03-04T10:30:00",
  "updatedAt": "2026-03-04T10:30:00"
}
```

**Validation Rules:**
- `title`: Required, 3-200 characters
- `description`: Optional, maximum 1000 characters
- `userId`: Required, must reference existing user
- `categoryId`: Required, must reference existing category

---

#### GET /api/tasks/{id}
Retrieve a specific task by ID. **Requires authentication.**

**Path Parameters:**
- `id` (Long): Task ID

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Buy groceries",
  "description": "Milk, eggs, bread, butter",
  "completed": false,
  "userId": 1,
  "categoryId": 2,
  "createdAt": "2026-03-04T10:30:00",
  "updatedAt": "2026-03-04T10:30:00"
}
```

---

#### PUT /api/tasks/{id}
Update an existing task. **Requires authentication.**

**Path Parameters:**
- `id` (Long): Task ID

**Request:**
```json
{
  "title": "Buy groceries - Updated",
  "description": "Milk, eggs, bread, butter, cheese",
  "completed": true,
  "userId": 1,
  "categoryId": 2
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Buy groceries - Updated",
  "description": "Milk, eggs, bread, butter, cheese",
  "completed": true,
  "userId": 1,
  "categoryId": 2,
  "createdAt": "2026-03-04T10:30:00",
  "updatedAt": "2026-03-04T11:45:00"
}
```

---

#### DELETE /api/tasks/{id}
Delete a task. **Requires authentication.**

**Path Parameters:**
- `id` (Long): Task ID

**Response (204 No Content):**
Empty response body with status code 204.

---

### Category Endpoints

#### GET /api/categories
List all categories. **Requires authentication.**

**Query Parameters:** None

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Work"
  },
  {
    "id": 2,
    "name": "Home"
  },
  {
    "id": 3,
    "name": "Health"
  }
]
```

---

#### POST /api/categories
Create a new category. **Requires authentication.**

**Request:**
```json
{
  "name": "Work"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Work"
}
```

**Validation Rules:**
- `name`: Required, 2-50 characters, must be unique

---

#### PUT /api/categories/{id}
Update a category name. **Requires authentication.**

**Path Parameters:**
- `id` (Long): Category ID

**Request:**
```json
{
  "name": "Professional Work"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Professional Work"
}
```

---

#### DELETE /api/categories/{id}
Delete a category. **Requires authentication.**

**Path Parameters:**
- `id` (Long): Category ID

**Response (204 No Content):**
Empty response body with status code 204.

---

## Data Models

### User Entity

**Database Table:** `users`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier |
| email | VARCHAR(255) | UNIQUE, NOT NULL | User email address |
| password | VARCHAR(255) | NOT NULL | Hashed password (BCrypt) |
| name | VARCHAR(100) | NOT NULL | User full name |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**UserResponse Record:**
```json
{
  "id": 1,
  "email": "user@example.com",
  "name": "John Doe"
}
```

---

### Task Entity

**Database Table:** `tasks`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier |
| title | VARCHAR(200) | NOT NULL | Task title |
| description | VARCHAR(1000) | NULLABLE | Task description |
| completed | BOOLEAN | DEFAULT FALSE | Completion status |
| user_id | BIGINT | FOREIGN KEY | References user.id |
| category_id | BIGINT | FOREIGN KEY | References category.id |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**TaskResponse Record:**
```json
{
  "id": 1,
  "title": "Buy groceries",
  "description": "Milk, eggs, bread",
  "completed": false,
  "userId": 1,
  "categoryId": 2,
  "createdAt": "2026-03-04T10:30:00",
  "updatedAt": "2026-03-04T10:30:00"
}
```

---

### Category Entity

**Database Table:** `categories`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier |
| name | VARCHAR(50) | UNIQUE, NOT NULL | Category name |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Category Record:**
```json
{
  "id": 1,
  "name": "Work"
}
```

---

## Request/Response Examples

### Complete Workflow Example

#### Step 1: Register New User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePass123"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

#### Step 2: Login to Get Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNjgzMTQyNTAwLCJleHAiOjE2ODMyMjg5MDB9.signature"
}
```

#### Step 3: Create Category
```bash
TOKEN="your_token_here"

curl -X POST http://localhost:8080/api/categories \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Work"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Work"
}
```

#### Step 4: Create Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete project report",
    "description": "Finish Q1 financial report",
    "completed": false,
    "userId": 1,
    "categoryId": 1
  }'
```

**Response:**
```json
{
  "id": 1,
  "title": "Complete project report",
  "description": "Finish Q1 financial report",
  "completed": false,
  "userId": 1,
  "categoryId": 1,
  "createdAt": "2026-03-04T10:30:00",
  "updatedAt": "2026-03-04T10:30:00"
}
```

#### Step 5: List Tasks with Filters
```bash
curl -X GET "http://localhost:8080/api/tasks?userId=1&completed=false&page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Complete project report",
      "description": "Finish Q1 financial report",
      "completed": false,
      "userId": 1,
      "categoryId": 1,
      "createdAt": "2026-03-04T10:30:00",
      "updatedAt": "2026-03-04T10:30:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

#### Step 6: Update Task
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete project report - URGENT",
    "description": "Finish Q1 financial report by EOD",
    "completed": true,
    "userId": 1,
    "categoryId": 1
  }'
```

**Response:**
```json
{
  "id": 1,
  "title": "Complete project report - URGENT",
  "description": "Finish Q1 financial report by EOD",
  "completed": true,
  "userId": 1,
  "categoryId": 1,
  "createdAt": "2026-03-04T10:30:00",
  "updatedAt": "2026-03-04T11:45:00"
}
```

---

## Error Handling

### Error Response Format

All error responses follow a consistent format:

```json
{
  "timestamp": "2026-03-04T10:30:00.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/tasks"
}
```

### HTTP Status Codes

| Code | Meaning | Example Scenario |
|------|---------|------------------|
| 200 | OK | Successful GET, PUT request |
| 201 | Created | Successful POST request |
| 204 | No Content | Successful DELETE request |
| 400 | Bad Request | Invalid input, validation error |
| 401 | Unauthorized | Missing or invalid JWT token |
| 403 | Forbidden | User lacks permission |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Unique constraint violation (duplicate email) |
| 500 | Internal Server Error | Server error |

### Common Error Responses

#### 400 Bad Request - Validation Error
```json
{
  "timestamp": "2026-03-04T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Field validation failed: email must be valid",
  "path": "/api/users"
}
```

**Solutions:**
- Check field format and constraints
- Verify required fields are provided
- Review validation rules in API documentation

#### 401 Unauthorized - Missing Token
```json
{
  "timestamp": "2026-03-04T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Missing Authorization header",
  "path": "/api/tasks"
}
```

**Solutions:**
- Include `Authorization: Bearer <token>` header
- Login again to get fresh token
- Check token format (should be `Bearer eyJ...`)

#### 401 Unauthorized - Invalid Token
```json
{
  "timestamp": "2026-03-04T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or expired JWT token",
  "path": "/api/tasks"
}
```

**Solutions:**
- Verify token hasn't expired
- Check token signature matches server secret
- Login again to obtain new token

#### 404 Not Found
```json
{
  "timestamp": "2026-03-04T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Task with ID 999 not found",
  "path": "/api/tasks/999"
}
```

**Solutions:**
- Verify resource ID is correct
- Check if resource has been deleted
- Review endpoint path

#### 409 Conflict - Duplicate Email
```json
{
  "timestamp": "2026-03-04T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Email already exists",
  "path": "/api/users"
}
```

**Solutions:**
- Use a different email address
- Check if user already exists
- Reset password if you forgot credentials

#### 500 Internal Server Error
```json
{
  "timestamp": "2026-03-04T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Database connection failed",
  "path": "/api/tasks"
}
```

**Solutions:**
- Check server logs for detailed error
- Verify database is running
- Restart application
- Contact API administrator

---

## Best Practices

### API Usage

#### 1. Always Include Content-Type Header
```bash
curl -H "Content-Type: application/json" \
  -d '{"email": "user@example.com"}' \
  http://localhost:8080/api/users
```

#### 2. Use Pagination for List Endpoints
Instead of fetching all records:
```bash
# Bad: May return thousands of records
GET /api/tasks

# Good: Fetch in manageable chunks
GET /api/tasks?page=0&size=20
```

#### 3. Filter Results When Possible
```bash
# Instead of fetching all and filtering client-side
GET /api/tasks?userId=1&completed=false&page=0&size=10
```

#### 4. Store Tokens Securely
```javascript
// Good: Store in secure httpOnly cookie
document.cookie = "token=" + authToken + "; httpOnly; secure; sameSite=strict";

// Avoid: Storing in localStorage (vulnerable to XSS)
// localStorage.setItem('token', authToken);  // Not recommended
```

#### 5. Handle Errors Gracefully
```javascript
fetch('/api/tasks', {
  headers: { 'Authorization': `Bearer ${token}` }
})
.then(response => {
  if (!response.ok) {
    throw new Error(`HTTP Error: ${response.status}`);
  }
  return response.json();
})
.catch(error => {
  console.error('Request failed:', error);
  // Show user-friendly error message
});
```

#### 6. Use Appropriate HTTP Methods
```bash
GET     /api/tasks        # Retrieve data
POST    /api/tasks        # Create resource
PUT     /api/tasks/1      # Update entire resource
DELETE  /api/tasks/1      # Delete resource
```

#### 7. Implement Token Refresh Logic
```javascript
// Check if token is about to expire
if (tokenExpiresIn < 5 * 60) {  // Less than 5 minutes
  loginAgainToGetFreshToken();
}
```

### Security

#### 1. Never Log Sensitive Data
```bash
# Bad: Logs entire response with passwords
console.log(response);

# Good: Log only necessary information
console.log('User created:', response.data.id);
```

#### 2. Implement Rate Limiting on Client
```javascript
// Prevent multiple rapid requests
let lastRequestTime = 0;
function makeRequest(url) {
  const now = Date.now();
  if (now - lastRequestTime < 1000) {  // 1 second minimum between requests
    return Promise.reject('Too many requests');
  }
  lastRequestTime = now;
  return fetch(url);
}
```

#### 3. Validate Input Client-Side
```javascript
function validateEmail(email) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
}

if (!validateEmail(userEmail)) {
  throw new Error('Invalid email format');
}
```

#### 4. Use HTTPS in Production
```bash
# Development (HTTP OK)
http://localhost:8080/api/tasks

# Production (HTTPS required)
https://api.taskmanager.com/api/tasks
```

### Performance

#### 1. Implement Caching
```javascript
// Cache task list for 5 minutes
const cache = new Map();
async function getCachedTasks() {
  const cached = cache.get('tasks');
  if (cached && Date.now() - cached.time < 5 * 60 * 1000) {
    return cached.data;
  }
  // Fetch from API if not cached or expired
}
```

#### 2. Use Pagination Limits
```bash
# Reasonable page sizes
GET /api/tasks?page=0&size=20   # Good
GET /api/tasks?page=0&size=50   # Acceptable
GET /api/tasks?page=0&size=1000 # Risky - may timeout
```

#### 3. Request Only Needed Fields
```bash
# If API supports field selection (future enhancement)
GET /api/tasks?fields=id,title,completed
```

---

## Troubleshooting

### Database Connection Issues

#### Problem: "Cannot connect to database"

**Causes:**
- MySQL container not running
- Wrong database credentials in `.env`
- Port 3306 already in use

**Solutions:**
```bash
# Check if container is running
docker ps | grep taskmanager-db

# If not running, start it
docker-compose up -d

# Check database logs
docker logs taskmanager-db

# Verify credentials in .env
cat .env | grep DB_

# Check if port is available
lsof -i :3306
```

### JWT Token Issues

#### Problem: "Invalid or expired JWT token"

**Causes:**
- Token has expired
- Token signature doesn't match
- Wrong token format in header

**Solutions:**
```bash
# Get new token by logging in
curl -X POST http://localhost:8080/api/auth/login \
  -d '{"email":"user@example.com","password":"password"}'

# Verify token format (should have 3 parts separated by dots)
echo "your_token" | grep -o '\.' | wc -l  # Should be 2

# Check header format
# Correct: Authorization: Bearer eyJhbGc...
# Wrong:   Authorization: eyJhbGc...
```

### Application Startup Issues

#### Problem: "Port 8080 already in use"

**Solutions:**
```bash
# Find process using port 8080
lsof -i :8080

# Kill process (if safe)
kill -9 <PID>

# Or run on different port
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

#### Problem: "Class not found after changes"

**Solutions:**
```bash
# Clean rebuild
./mvnw clean compile

# Clear Maven cache if needed
rm -rf ~/.m2/repository
./mvnw clean install
```

### Data Issues

#### Problem: "Duplicate entry for key 'email'"

**Causes:**
- User with same email already exists

**Solutions:**
```bash
# Use different email
{
  "email": "newemail@example.com",
  "password": "SecurePassword123",
  "name": "User Name"
}

# Or delete existing user first (if you have access)
DELETE /api/users/{id}
```

#### Problem: "Foreign key constraint fails"

**Causes:**
- Referenced user or category doesn't exist
- Wrong ID in request

**Solutions:**
```bash
# Verify user exists
GET /api/users

# Verify category exists
GET /api/categories

# Use valid IDs from responses
{
  "title": "Task",
  "userId": 1,      # Verify this user exists
  "categoryId": 1   # Verify this category exists
}
```

---

## Support & Additional Resources

### Documentation Links
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI Schema**: `http://localhost:8080/v3/api-docs`
- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **Spring Security**: https://spring.io/projects/spring-security
- **JWT.io**: https://jwt.io/

### Reporting Issues
For bugs or feature requests, open an issue on the GitHub repository:
https://github.com/thaoan/taskmanager/issues

### Contributing
See CONTRIBUTING.md for development guidelines and contribution procedures.

---

**Last Updated:** March 4, 2026
**API Version:** 1.0.0
**Status:** Production Ready
