# 🎬 CineVault

## Overview

CineVault is a web application for movie enthusiasts where users can browse movies, create personal watchlists, rate and review movies, and manage their profiles. Administrators have access to a dedicated admin panel for managing the movie catalog and user roles.

The project also includes a separate recommendation microservice that generates personalized movie recommendations based on user preferences and movie data.

This project was developed as an Individual Project Assignment for the Spring Advanced Course at SoftUni.

---

## 📌 Project Repositories

### Main Application

[CineVault GitHub Repository](https://github.com/ralitsadzhukeva/CineVault)

### Recommendation Microservice

[CineVault Recommendation Service GitHub Repository](https://github.com/ralitsadzhukeva/CineVault-recommendation-service)

The recommendation service is maintained in a **separate repository** and uses its **own database**. The main CineVault application communicates with the recommendation microservice through REST APIs using **Spring Cloud OpenFeign**.

---

# 🛠️ Tech Stack

## Main Application

### Backend

* Java 17+
* Spring Boot
* Spring MVC
* Spring Data JPA
* Hibernate
* Spring Security
* Spring Cloud OpenFeign
* Jakarta Bean Validation

### Frontend

* Thymeleaf
* HTML5
* CSS3

### Database

* MySQL

### Build Tool

* Maven

### Version Control

* Git & GitHub

---

## Recommendation Microservice

### Backend

* Java 17+
* Spring Boot
* Spring MVC
* Spring Data JPA
* Hibernate
* Jakarta Bean Validation

### API

* REST API
* JSON
* Spring Cloud-compatible REST communication

### Database

* MySQL
* Separate database from the main CineVault application

### Build Tool

* Maven

---

# ✨ Features

## 🔐 Authentication & Authorization

* User registration
* User login
* User logout
* Password encryption using BCrypt
* Session-based authentication
* Role-based access control
* USER and ADMIN roles
* Administrators can change user roles
* Protected pages and administrative operations

---

## 🎬 Movie Management

* Browse movies
* Search movies
* View movie details
* Add movies
* Edit movies
* Delete movies
* Movie management restricted to administrators
* Movie genre classification
* Movie release year
* Movie descriptions
* Movie poster URLs

---

## ⭐ Reviews

* Add movie reviews
* Edit personal reviews
* Delete personal reviews
* View personal reviews
* Movie ratings
* Rating validation
* Average movie rating calculation

---

## 📋 Watchlist

* Add movies to a personal watchlist
* Remove movies from a watchlist
* Mark movies as watched
* View personal watchlist
* Prevent duplicate movie entries
* Track when movies were added

---

## 👤 User Profile

* View user profile
* Edit profile information
* Manage personal information
* Administrators can manage user roles

---

## 🤖 Movie Recommendations

CineVault integrates with a separate recommendation microservice.

The recommendation functionality includes:

* Personalized movie recommendations
* Generation of recommendations based on user preferences
* Use of watched movies and movie data
* Recommendation scoring
* Recommendation reasons
* Retrieval of recommendations for a specific user
* Regeneration/deletion of recommendations
* Communication between the main application and the recommendation service through REST APIs
* Separate database for recommendation data

The main application uses **Spring Cloud OpenFeign** to communicate with the recommendation service.

---

# 👥 User Roles

## Guest

Guests can:

* Browse public pages
* View available movies
* Register
* Log in

## User

Authenticated users can:

* Browse movies
* Search movies
* View movie details
* Manage their watchlist
* Mark movies as watched
* Create reviews
* Edit their reviews
* Delete their reviews
* View their reviews
* Manage their profile
* Receive personalized recommendations

## Admin

Administrators have all user permissions and can additionally:

* Add movies
* Edit movies
* Delete movies
* Manage users
* Change user roles

---

# 🏗️ Domain Entities

## User

Stores user account information, profile information, authentication data, and role.

## Movie

Stores movie information such as:

* Title
* Director
* Genre
* Release year
* Description
* Poster URL

## Review

Stores:

* User
* Movie
* Rating
* Comment
* Review information

## Watchlist

Stores:

* User
* Movie
* Watched status
* Date added

---

# 🔗 Entity Relationships

* User → Review — One-to-Many
* Movie → Review — One-to-Many
* User → Watchlist — One-to-Many
* Movie → Watchlist — One-to-Many

The recommendation microservice maintains its own recommendation-related entities and database independently from the main application.

---

# ✅ Validation

The application uses server-side validation for:

* User registration
* User login
* Profile editing
* Movie creation
* Movie editing
* Review creation
* Review editing
* Recommendation requests

Validation errors are handled and displayed appropriately.

The recommendation microservice also validates incoming REST API requests using Jakarta Bean Validation.

---

# 🔒 Security

The application implements:

* Spring Security
* Session-based authentication
* BCrypt password hashing
* Role-based authorization
* Protected authenticated pages
* Admin-only operations
* USER and ADMIN roles
* User role management by administrators

The recommendation microservice also protects its REST endpoints through authentication.

---

# 🌐 REST API & Microservice Communication

The CineVault application communicates with the recommendation microservice using REST.

The main application uses **Spring Cloud OpenFeign** to call the recommendation service.

The recommendation microservice exposes endpoints for operations such as:

```text
GET    /api/recommendations/user/{userId}
POST   /api/recommendations/generate
DELETE /api/recommendations/user/{userId}
```

The two applications run independently and use separate databases.

---

# 🧪 Testing

The project contains automated tests for the main application and recommendation microservice.

Testing includes:

* Unit tests
* Repository tests
* Service tests
* Controller/API tests
* Validation tests
* Security tests
* Integration-related tests

Both applications have been tested and the test suites pass successfully.

The test coverage for both applications is above **70%**.

---

# 📝 Logging

Application logging is implemented to provide information about application activity and assist with troubleshooting and debugging.

Logging is used across the application where appropriate, including service and application operations.

---

# 🗄️ Database

The project uses **MySQL** as its relational database.

The main CineVault application and recommendation microservice use **separate databases**.

This separation allows the recommendation service to operate independently from the main application's data.

---

# 📂 Main Application Structure

```text
src
└── main
    ├── java
    │   └── bg.softuni.cinevault
    │       ├── dto
    │       ├── entities
    │       ├── enums
    │       ├── repository
    │       ├── service
    │       ├── web
    │       └── config
    │
    └── resources
        ├── static
        │   └── css
        ├── templates
        ├── application-dev.properties
        └── application.properties
```

---

# 📂 Recommendation Microservice Structure

```text
src
└── main
    ├── java
    │   └── bg.softuni.cinevaultrecommendationservice
    │       ├── dto
    │       ├── exception
    │       ├── mapper
    │       ├── model
    │       ├── repository
    │       ├── service
    │       └── web
    │
    └── resources
        ├── application-dev.properties
        └── application.properties
```

The recommendation microservice is maintained independently in its own GitHub repository.

---

# ▶️ Running the Main Application

## Requirements

* Java 17+
* MySQL
* Maven
* Git

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/ralitsadzhukeva/CineVault.git
```

### 2. Create the database

Create a MySQL database for the main application:

```sql
CREATE DATABASE cinevault;
```

### 3. Configure database credentials

Configure the database connection in:

```text
application.properties
```

### 4. Start the application

```bash
mvn spring-boot:run
```

The application will be available at:

```text
http://localhost:8080
```

---

# ▶️ Running the Recommendation Microservice

### 1. Clone the repository

```bash
git clone https://github.com/ralitsadzhukeva/CineVault-recommendation-service.git
```

### 2. Create the recommendation database

Create a separate MySQL database for the recommendation service.

### 3. Configure database credentials

Configure the recommendation service's database connection in its `application.properties`.

### 4. Start the microservice

```bash
mvn spring-boot:run
```

The recommendation service runs independently from the main application.

The main CineVault application must be configured to communicate with the recommendation service URL.

---

# 🔄 Application Architecture

The project consists of two Spring Boot applications:

```text
┌──────────────────────────────┐
│       CineVault App          │
│                              │
│  Spring Boot                 │
│  Spring MVC                  │
│  Spring Security             │
│  Thymeleaf                   │
│  Spring Data JPA             │
│                              │
│        MySQL DB              │
└──────────────┬───────────────┘
               │
               │ REST / OpenFeign
               ▼
┌──────────────────────────────┐
│ Recommendation Microservice │
│                              │
│  Spring Boot                 │
│  REST API                    │
│  Spring Data JPA             │
│                              │
│     Separate MySQL DB        │
└──────────────────────────────┘
```

The main application is responsible for users, movies, reviews, watchlists, authentication, authorization, and the user-facing application.

The recommendation microservice is responsible for recommendation generation and recommendation-related data.

---

# 📌 Project Repositories

**Main application:**
https://github.com/ralitsadzhukeva/CineVault

**Recommendation microservice:**
https://github.com/ralitsadzhukeva/CineVault-recommendation-service

---

# 👩‍💻 Author

**Ralitsa Dzhukeva**

Developed as an Individual Project Assignment for the Spring Advanced Course at SoftUni.
