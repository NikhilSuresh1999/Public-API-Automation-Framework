# 🚀 Enterprise REST API Test Automation Framework

[![CI/CD Pipeline](https://github.com/your-username/publicApiAutomation/actions/workflows/api-test-automation.yml/badge.svg)](https://github.com/your-username/publicApiAutomation/actions)
[![Java 17](https://img.shields.io/badge/Java-17%20LTS-orange.svg)](https://www.oracle.com/java/)
[![REST Assured](https://img.shields.io/badge/REST%20Assured-5.4.0-blue.svg)](https://rest-assured.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.9.0-green.svg)](https://testng.org/)
[![Allure Report](https://img.shields.io/badge/Report-Allure%202-red.svg)](https://qameta.io/allure-report/)
[![Maven](https://img.shields.io/badge/Build-Maven%203.9-lightgrey.svg)](https://maven.apache.org/)

An enterprise-grade, multi-service **REST API Test Automation Framework** engineered with **Java 17**, **REST Assured**, **TestNG**, **Jackson**, **Datafaker**, and **Allure Report**.

Built specifically for high-reliability CI/CD pipelines, this framework covers **over 150+ real-world test scenarios** across realistic public microservices without relying on a dedicated private QA environment.

---

## 🏛 Framework Architecture

```
                                  +---------------------------------------+
                                  |         GitHub Actions (CI/CD)        |
                                  +-------------------+-------------------+
                                                      |
                                                      v
                                  +---------------------------------------+
                                  |           TestNG Suite Runner         |
                                  |   (Parallel Execution / Retries)      |
                                  +-------------------+-------------------+
                                                      |
                         +----------------------------+----------------------------+
                         |                                                         |
                         v                                                         v
         +-------------------------------+                         +-------------------------------+
         |     Service Client Layer      |                         |       Utility & Helpers       |
         |   (Service Object Pattern)    |                         |  - Datafaker Test Data        |
         |  - BookerAuthClient           |                         |  - JsonUtils (Jackson)        |
         |  - BookingClient              |                         |  - AssertionUtils             |
         |  - DummyAuthClient            |                         |  - ConfigurationManager       |
         |  - ProductClient              |                         +-------------------------------+
         |  - UserClient / CartClient    |
         |  - ReqresClient / HttpBin     |
         +---------------+---------------+
                         |
                         v
         +-------------------------------+
         |   REST Assured Core Engine    |
         |  - RequestSpecBuilderFactory  |
         |  - CustomLoggingFilter        |
         |  - AllureRestAssured Filter   |
         |  - JSON Schema Validator      |
         +---------------+---------------+
                         |
                         v
+---------------------------------------------------------------------------------------------+
|                                Target Public Microservices                                  |
|   • Restful-Booker (Auth, Bookings)           • DummyJSON (Auth, Products, Users, Carts)     |
|   • ReqRes (Users, Resources, Delay SLA)      • HttpBin (Status Codes, Echo, Headers, Auth) |
+---------------------------------------------------------------------------------------------+
```

---

## ✨ Enterprise Design Patterns & Features

* **Service Object Pattern (Client Layer)**: Complete abstraction between HTTP requests and test cases for maintainability.
* **Strong POJO Modeling with Lombok & Jackson**: Type-safe request serialization and response deserialization.
* **Contract & Schema Validation**: Automated JSON Schema compliance checking against schema drafts in `src/main/resources/schemas`.
* **Zero Hardcoded Data**: Realistic dynamic test generation with **Datafaker** ensuring zero collision or state pollution.
* **Thread-Safe Parallel Execution**: Multi-threaded execution (`parallel="classes"`, `thread-count="4"`).
* **Automatic Retry Analyzer**: Dynamic transient network failure retry mechanism with `IRetryAnalyzer`.
* **Security & Negative Testing**: Comprehensive coverage of SQL injection attempts, XSS payloads, missing headers, 401/403 security gates, and expired tokens.
* **Interactive Allure Reporting**: Rich step-by-step reporting with automatic request/response body, headers, and cURL attachments.
* **Continuous Integration (GitHub Actions)**: Automated runs on Push/PR and nightly scheduled execution, with automated Allure report deployment to GitHub Pages.

---

## 📊 Test Coverage Breakdown (150+ Scenarios)

| Microservice | Functional Domain | Key Scenarios Covered |
| :--- | :--- | :--- |
| **Restful-Booker** | **Authentication** | Valid login, Bad credentials, Missing username/password, SQLi injection payloads, Malformed JSON, Server health check (`/ping`). |
| **Restful-Booker** | **Booking CRUD** | Dynamic random booking creation, Persona-driven data sets, Missing mandatory fields, Zero/negative price boundaries, XSS resilience, Inverted dates. |
| **Restful-Booker** | **Query & Search** | Get all booking IDs, Filter by checkin/checkout dates, Filter by name, Non-existent search, Single booking ID, 404 handling, XML/JSON content negotiation. |
| **Restful-Booker** | **Update (PUT/PATCH)** | Full update with token, Basic auth update, Unauthorized update (403), Invalid token (403), Partial PATCH of names, Partial PATCH of deposit flag. |
| **Restful-Booker** | **Deletion** | Successful deletion (201), Verify post-delete 404, Unauthorized delete, Invalid token delete, Double deletion idempotency. |
| **DummyJSON** | **Auth & JWT** | Valid login, Access token extraction, Invalid password/user (400), Token expiration, Profile retrieval (`/auth/me`), Token refresh flow (`/auth/refresh`). |
| **DummyJSON** | **Product Catalog** | Product listing, Pagination boundary offsets, Product by ID, 404 handling, Search queries, Category listing, Filter by category, Add/Update/Patch/Delete product, Sorting by price. |
| **DummyJSON** | **Data Integrity** | Rating scale range check `[0.0, 5.0]`, Non-negative inventory validation, Price boundary values, Large payload stress validation. |
| **DummyJSON** | **User Management** | Full user listing, Single user profile with nested geo-coordinates and bank details, User search, Filter by gender and blood group, Email regex validation. |
| **DummyJSON** | **Shopping Carts** | Cart listing, Cart retrieval, Calculation verification (`total == sum(items)`), User cart query, Add products to cart, Delete cart. |
| **ReqRes** | **User Lifecycle** | Paginated users, Contract schema validation, Single user 404, Data-driven user creation, PUT/PATCH updates, Delete user (204), Latency handling (delay=2). |
| **ReqRes** | **Auth & Resources** | User registration (Success & Missing password/email), Login (Success & Failure), Resource listing and single resource lookup. |
| **HttpBin** | **Protocol & Status** | Data-driven validation of 14+ standard HTTP status codes (`200`, `201`, `204`, `400`, `401`, `403`, `404`, `405`, `415`, `422`, `500`, `502`, `503`). |
| **HttpBin** | **Headers & Security** | Header reflection, User-Agent inspection, Basic Auth validation, Bearer token authentication with valid and missing credentials. |
| **HttpBin** | **HTTP Verbs Echo** | GET query params, POST JSON body, PUT replacement, PATCH partial, DELETE request echoing. |
| **End-to-End (E2E)** | **Booking Lifecycle** | Full sequential flow: Auth -> Create Booking -> Verify -> Full PUT -> Partial PATCH -> Delete -> Verify 404. |
| **End-to-End (E2E)** | **E-Commerce Order** | Full consumer journey: Login -> Get Profile -> Browse Catalog -> Add to Cart -> Verify Math -> Delete Cart. |

---

## 🛠 Local Setup & Execution

### Prerequisites
* **Java JDK 17+**
* **Apache Maven 3.8+**
* **Git**

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/publicApiAutomation.git
cd publicApiAutomation
```

### 2. Run All Tests (Regression Suite)
```bash
mvn clean test
```

### 3. Run Smoke Suite
```bash
mvn clean test -DsuiteFile=testng-smoke.xml
```

### 4. Run with Custom Configuration / Environment
```bash
mvn clean test -Dbooker.base.url=https://restful-booker.herokuapp.com -Dsla.max.response.time.ms=4000
```

### 5. Generate and Open Allure Report
```bash
mvn allure:serve
```

---

## 📁 Project Structure

```
publicApiAutomation/
├── .github/
│   └── workflows/
│       └── api-test-automation.yml   # GitHub Actions CI/CD Pipeline
├── src/
│   ├── main/
│   │   ├── java/com/enterprise/api/
│   │   │   ├── clients/              # Service Client Layer (Service Object Pattern)
│   │   │   │   ├── booker/           # Restful-Booker API Clients
│   │   │   │   ├── dummyjson/        # DummyJSON API Clients
│   │   │   │   ├── httpbin/          # HttpBin Protocol Clients
│   │   │   │   └── reqres/           # ReqRes Mock Clients
│   │   │   ├── config/               # Owner Type-Safe Configuration
│   │   │   ├── constants/            # Endpoints & HTTP Status Code Constants
│   │   │   ├── filters/              # Custom Logging & Sensitive Header Masking
│   │   │   ├── models/               # POJOs with Jackson & Lombok
│   │   │   ├── spec/                 # Reusable Request/Response Specifications
│   │   │   └── utils/                # Datafaker, JsonUtils, AssertionUtils
│   │   └── resources/
│   │       ├── config.properties     # Multi-environment properties
│   │       ├── logback.xml           # Structured logging configuration
│   │       └── schemas/              # JSON Schema validation files
│   └── test/
│       └── java/com/enterprise/api/
│           ├── base/                 # BaseTest Suite setup and lifecycle
│           ├── dataproviders/        # Parameterized TestNG DataProviders
│           ├── listeners/            # TestListener & Dynamic RetryAnalyzer
│           └── tests/                # Test suites (150+ scenarios)
│               ├── booker/           # Restful-Booker test classes
│               ├── dummyjson/        # DummyJSON test classes
│               ├── e2e/              # End-to-end integration workflows
│               ├── httpbin/          # HTTP protocol & status code tests
│               └── reqres/           # ReqRes mock API tests
├── pom.xml                           # Maven dependencies and build plugins
├── testng.xml                        # Master TestNG suite (Parallel execution)
├── testng-smoke.xml                  # Sanity and smoke suite
└── README.md                         # Portfolio documentation
```

---

## 💼 Resume Description & Interview Talking Points

### Resume Bullet Points (Copy & Paste)
> * **Enterprise REST API Automation Framework | Java, REST Assured, TestNG, Allure, GitHub Actions**
>   * Designed and built an enterprise-level, multi-service API automation framework in **Java 17 & REST Assured** implementing the **Service Object Pattern** and **POJO serialization (Jackson)**.
>   * Developed **150+ automated test scenarios** covering functional, negative, boundary, security (SQLi/XSS), and **JSON Schema contract validation** across multiple distributed microservices.
>   * Implemented custom **RestAssured logging filters** with credential masking, dynamic **Datafaker** test data generators, and a custom **IRetryAnalyzer** for flakiness mitigation.
>   * Configured **GitHub Actions CI/CD pipeline** with automated multi-threaded parallel execution, publishing interactive **Allure Reports to GitHub Pages** with historical trend tracking.

### Key Interview Questions This Project Prepares You For
1. **"How do you handle API test automation when there is no QA environment?"**
   * *Answer*: Leverage containerized local microservices via Docker Compose and high-uptime public contract APIs (like Restful-Booker, DummyJSON, HttpBin) designed with dynamic data generators (Datafaker) and contract schema validations.
2. **"How do you structure an enterprise API framework?"**
   * *Answer*: Separate concern layers: Configuration (Owner), Clients (Service Object Pattern), Models (POJOs with Jackson/Lombok), Specifications (RequestSpec/ResponseSpec builders), Filters (Logging & Auth masking), and Test Classes with TestNG listeners.
3. **"How do you prevent flaky tests in network calls?"**
   * *Answer*: Implement dynamic retries (`IRetryAnalyzer` + `IAnnotationTransformer`), generous connection/socket timeout configurations, SLA assertion guards, and fully independent test datasets via dynamic generators.

