# 💰 Expense Tracker API

A production-ready REST API built with Spring Boot for managing personal expenses, categories, and budgets.  
This project demonstrates clean architecture, database relationships, validation, testing, Dockerization, CI/CD, and cloud deployment.

---

## 👥 Team Members

- Mikayil Guliyev
- Nihad Bagirzade
- Ali Safarli

---

## 🚀 Project Overview

Expense Tracker API allows users to:

- Track daily expenses
- Organize expenses by categories
- Set monthly budgets per category
- Monitor spending behavior
- Analyze financial activity

---

## 🧱 Tech Stack

| Layer       | Technology                        |
|-------------|-----------------------------------|
| Language    | Java 17                           |
| Framework   | Spring Boot                       |
| ORM         | Spring Data JPA + Hibernate       |
| Build Tool  | Maven                             |
| DB (local)  | MySQL                             |
| DB (test)   | H2 (in-memory)                    |
| DB (prod)   | PostgreSQL                        |
| Container   | Docker + Docker Compose           |
| CI/CD       | GitHub Actions                    |
| Deployment  | Render                            |

---

## 📦 Architecture

```
Controller → Service → Repository → Database
```

Clean layered architecture:

- **Controller** — API layer, handles HTTP requests/responses
- **Service** — Business logic and validations
- **Repository** — Database access via JPA
- **Entity** — Database models

---

## 🗄️ Database Design

### 👤 Account
| Field           | Type          | Notes                        |
|-----------------|---------------|------------------------------|
| id              | Long          | PK, auto-increment           |
| full_name       | varchar(255)  |                              |
| email           | varchar(255)  | Not null, valid email format |
| current_balance | decimal(10,2) |                              |
| created_at      | timestamp     | Set on persist               |

### 📂 Category
| Field       | Type          | Notes              |
|-------------|---------------|--------------------|
| id          | Long          | PK, auto-increment |
| name        | varchar(100)  | Not null, unique   |
| description | varchar(255)  |                    |
| created_at  | timestamp     |                    |
| updated_at  | timestamp     |                    |

### 📊 Budget
| Field        | Type          | Notes                           |
|--------------|---------------|---------------------------------|
| id           | Long          | PK, auto-increment              |
| category_id  | Long          | FK → categories.id              |
| month        | date          | First day of month (2026-01-01) |
| limit_amount | decimal(10,2) | Not null                        |
| created_at   | timestamp     |                                 |
| updated_at   | timestamp     |                                 |

> Unique constraint on `(category_id, month)` — one budget per category per month.

### 💰 Expense
| Field          | Type          | Notes                  |
|----------------|---------------|------------------------|
| id             | Long          | PK, auto-increment     |
| title          | varchar(255)  |                        |
| amount         | decimal(10,2) |                        |
| expense_date   | date          |                        |
| payment_method | varchar(50)   | Enum: PaymentMethod    |
| account_id     | Long          | FK → accounts.id       |
| category_id    | Long          | FK → categories.id     |
| budget_id      | Long          | FK → budgets.id        |
| created_at     | timestamp     | Set on persist         |

---

## 🔗 Relationships

- `Account` → `Expense` (One-to-Many) — each expense belongs to one account
- `Category` → `Budget` (One-to-Many) — each category can have a budget per month
- `Category` → `Expense` (One-to-Many) — each expense belongs to one category
- `Budget` → `Expense` (One-to-Many) — each expense is linked directly to a budget

---

## 📌 API Endpoints

### 👤 Account

| Method | Endpoint               | Description         |
|--------|------------------------|---------------------|
| POST   | `/api/accounts`        | Create account      |
| GET    | `/api/accounts`        | List all accounts   |
| GET    | `/api/accounts/{id}`   | Get account by ID   |
| PUT    | `/api/accounts/{id}`   | Update account      |
| DELETE | `/api/accounts/{id}`   | Delete account      |

### 📂 Category

| Method | Endpoint                | Description          |
|--------|-------------------------|----------------------|
| POST   | `/api/categories`       | Create category      |
| GET    | `/api/categories`       | List all categories  |
| GET    | `/api/categories/{id}`  | Get category by ID   |
| PUT    | `/api/categories/{id}`  | Update category      |
| DELETE | `/api/categories/{id}`  | Delete category      |

### 💰 Expense

| Method | Endpoint                          | Description              |
|--------|-----------------------------------|--------------------------|
| POST   | `/api/expenses`                   | Create expense           |
| GET    | `/api/expenses`                   | List all expenses        |
| GET    | `/api/expenses/{id}`              | Get expense by ID        |
| PUT    | `/api/expenses/{id}`              | Update expense           |
| DELETE | `/api/expenses/{id}`              | Delete expense           |

### 📊 Budget

| Method | Endpoint                        | Description             |
|--------|---------------------------------|-------------------------|
| POST   | `/api/budgets`                  | Create budget           |
| GET    | `/api/budgets`                  | List all budgets        |
| GET    | `/api/budgets/{id}`             | Get budget by ID        |
| PUT    | `/api/budgets/{id}`             | Update budget           |
| DELETE | `/api/budgets/{id}`             | Delete budget           |

---

## ⚙️ Local Setup

### 1. Clone the repository

```bash
git clone https://github.com/miko44quliyev/ExpenseTrackerAPI.git
cd expense-tracker
```

### 2. Configure environment variables

Copy the example env file and fill in your values:

```bash
cp .env.example .env
```

`.env.example`:
```
MYSQL_PASSWORD=your_password
MYSQL_DATABASE=expense_tracker
```

### 3. Create MySQL database

```sql
CREATE DATABASE expense_tracker;
```

### 4. Run the application

```bash
mvn clean install
mvn spring-boot:run
```

API runs at: `http://localhost:8080`

---

## 🧪 Testing

```bash
mvn test
```

Test coverage includes:

- Integration tests with H2 in-memory database
- Full CRUD operations
- Input validation checks
- Error handling (400 Bad Request, 404 Not Found)

---

## 🐳 Docker

### Build & run manually

```bash
docker build -t expense-tracker .
docker run -p 8080:8080 expense-tracker
```

### Docker Compose (recommended)

```bash
docker-compose up --build
```

`docker-compose.yml`:

```yaml
version: "3.8"

services:
  mysql:
    image: mysql:8
    container_name: expense-mysql
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: ${MYSQL_DATABASE}
    ports:
      - "3306:3306"

  app:
    build: .
    container_name: expense-app
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    environment:
      SPRING_PROFILES_ACTIVE: dev
```

> Never hardcode credentials. Use a `.env` file and add it to `.gitignore`.

---

## 🔄 CI/CD Pipeline

Automated with **GitHub Actions**.

Workflow steps:
1. Checkout code
2. Set up Java 17
3. Cache Maven dependencies
4. Build project (`mvn clean install`)
5. Run tests (`mvn test`)

**Triggers:**
- Push to `main` branch
- Pull requests to `main`

---

## ☁️ Deployment (Render)

Deployed using **Render** with a managed **PostgreSQL** database.

🌐 **Live URL:** [https://expensetrackerapi-1-aeen.onrender.com](https://expensetrackerapi-1-aeen.onrender.com)

📋 **Trello Board:** https://trello.com/b/1NTNbbSg/mini-group-project

Required environment variables on Render:

| Variable                  | Value                        |
|---------------------------|------------------------------|
| `SPRING_PROFILES_ACTIVE`  | `prod`                       |
| `DATABASE_URL`            | Your Render PostgreSQL URL   |

---

## ⚙️ Spring Profiles

| Profile | Database  | Config                                        |
|---------|-----------|-----------------------------------------------|
| `dev`   | MySQL     | `jdbc:mysql://localhost:3306/expense_tracker` |
| `test`  | H2        | `jdbc:h2:mem:testdb`                          |
| `prod`  | PostgreSQL| `${DATABASE_URL}`                             |

---

## 📊 Key Features

- Full CRUD for expenses, categories, budgets, and accounts
- Account management with balance tracking
- Category-based expense grouping
- Monthly budget per category with limit tracking
- Payment method tracking
- Recurring expense flag
- Validation with Spring annotations
- Global exception handling
- Clean REST API design
- Pagination and filtering support
- Swagger/OpenAPI documentation

---

## 📈 Future Improvements

- JWT Authentication & multi-user support
- Frontend dashboard (React)
- Expense analytics and charts
- Email notifications for budget limits
- Mobile app integration

---

## 🧠 What We Learned

- REST API design principles
- Spring Boot layered architecture
- JPA entity relationships
- Docker containerization
- CI/CD with GitHub Actions
- Cloud deployment with Render
- Team collaboration workflow

---

## 📌 License

This project is for educational purposes only.

---

> ⭐ Status: **Backend Complete · Docker Ready · CI/CD Active · Deployed on Render**
