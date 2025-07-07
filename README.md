# Customer Management System

This project is a Spring Boot RESTful API for managing customer data. It supports full CRUD operations and includes validation, exception handling, observability, containerization, Kubernetes deployment support, CI/CD documentation, and API consumption from a CLI client.

---

## Step 1: Build an API

### ✅ Overview

This project implements a **RESTful API** using **Spring Boot** to perform CRUD operations on a `Customer` entity. The API is backed by an **embedded H2 database** for simplicity and fast prototyping. The database credentials are securely injected using a `.env` file.

---

### 🧱 Entity: `Customer`

| Field          | Type   | Constraints                  | Notes               |
|----------------|--------|------------------------------|---------------------|
| `id`           | UUID   | Primary Key (auto-generated) |                     |
| `firstName`    | String | Not null                     |                     |
| `middleName`   | String | Nullable                     | Optional field      |
| `lastName`     | String | Not null                     |                     |
| `emailAddress` | String | Not null, Unique             | Must be valid email |
| `phoneNumber`  | String | Not null, Pattern enforced   | 10-digit only       |

Validation is implemented using `jakarta.validation.constraints` annotations like `@NotBlank`, `@Email`, and `@Pattern`.

---

### 📡 API Endpoints

| Method | Endpoint              | Description               |
|--------|------------------------|---------------------------|
| `POST` | `/api/customers`       | Create a new customer     |
| `GET`  | `/api/customers/{id}`  | Retrieve customer by ID   |
| `GET`  | `/api/customers`       | Retrieve all customers    |
| `PUT`  | `/api/customers/{id}`  | Update existing customer  |
| `DELETE` | `/api/customers/{id}`| Delete customer by ID     |

All responses are returned in JSON format with appropriate status codes (e.g., `201 Created`, `200 OK`, `404 Not Found`).

---

### 🗄️ Database: H2 (Embedded)

We use **H2 in file mode** (`jdbc:h2:file`) to persist customer data between application restarts.

- Console available at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/testdb`
- Platform: `H2Dialect`

---

### 🔐 Securing Credentials with `.env`

To avoid hardcoding sensitive credentials, the application uses environment variables loaded from a `.env` file.

▶️ Load Environment Variables Before Running the App
Spring Boot does not automatically load .env files. You must manually export the values into the environment before running:

```bash
- export $(cat .env | xargs)
- mvn spring-boot:run
```


### Testing

* Unit tests for Service and Controller using JUnit + Mockito
* Example: `CustomerServiceTest`, `CustomerControllerTest`

---

## 🧪 Step 2: Integration and Acceptance Testing

### 🧪 Tools and Annotations Used

- `@SpringBootTest`: Boots the entire application context for full-stack integration testing.
- `@AutoConfigureMockMvc`: Automatically configures `MockMvc` for endpoint testing.
- `MockMvc`: Allows sending HTTP requests to the API without starting the server.
- `@Test`: From JUnit 5 for writing individual test methods.
- `@BeforeEach`: Prepares test data before each test case if needed.

### Running Tests

```bash
- ./mvnw test
```

⚙️ CI/CD Automation Potential
These tests are safe to run in CI/CD pipelines and can be triggered during the test phase in GitHub Actions, GitLab CI, Jenkins, etc.

Example Maven stage in a CI pipeline:

```bash
- name: Run Tests
  run: ./mvnw test
```

---

## 📊 Step 3: Observability & Instrumentation

* **Enabled Actuator endpoints** for health checks and metrics
* Exposed:

    * `/actuator/health`
    * `/actuator/metrics`
    * `/actuator/prometheus`

**Prometheus-compatible telemetry**:

```properties
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.prometheus.enabled=true
management.endpoint.health.show-details=always
```

Assumed integration with **Prometheus + Grafana** stack for observability in production.

---

## 📦 Step 4: Containerization

The application is fully containerized using Docker. This makes it portable and easy to deploy in any environment supporting containers.

### 🐳 Dockerfile Used
```dockerfile
FROM openjdk:17-jdk-slim
COPY ./target/customer-management-system.jar customer-management-system.jar
ENTRYPOINT ["java", "-jar", "customer-management-system.jar"]
```
⚙️ Required Inputs
The application relies on the following environment variables for database access:

* DB_USERNAME: H2 database username

* DB_PASSWORD: H2 database password

These are typically provided using a .env file during development or passed explicitly using -e flags when running the container.


### Build & Run

```bash
- export $(cat .env | xargs)
- docker build -t customer-management-system .
- docker run -d -p 8080:8080 --name customer-app customer-management-system
```

---



## 🔒 Validation & Error Handling

* Validation: `@NotBlank`, `@Pattern`, `@Email` used in the entity class
* Handled using a `@RestControllerAdvice` (`GlobalExceptionHandler`) to return meaningful error responses
* Example:

```json
{
  "firstName": "First name is mandatory",
  "lastName": "Last name is mandatory"
}
```

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com.customers/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── entity/
│   │       ├── dto/
│   │       └── exception/
│   └── resources/
│       ├── application.properties
│       └── Dockerfile
├── test/
│   └── java/ (unit & integration tests)
```

---

## 🛠️ Tech Stack

* Java 17, Spring Boot 3.5
* H2 Database
* Docker, Kubernetes (Minikube)
* JUnit 5, Mockito
* Prometheus, Spring Actuator
* CI/CD via GitHub Actions

---

## 💡 Future Improvements

* Swagger/OpenAPI documentation
* Integration with PostgreSQL for production
* Role-based security (Spring Security + OAuth)
* Deployment with Helm or ArgoCD
