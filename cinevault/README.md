# 🎬 CineVault

## Overview

CineVault is a web application for movie enthusiasts where users can browse movies, create personal watchlists, rate and review movies, and manage their profiles. Administrators have access to a dedicated admin panel for managing the movie catalog.

This project was developed as an Individual Project Assignment for the Spring Fundamentals course at SoftUni.

---

## Tech Stack

### Backend

* Java 17
* Spring Boot 3
* Spring MVC
* Spring Data JPA
* Hibernate

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

## Features

### Authentication & Authorization

* User Registration
* User Login
* User Logout
* Password encryption using BCrypt
* Session-based authentication
* Role-based access control (USER / ADMIN)

### Movie Management

* Browse movies
* Search movies
* Add movie (Admin only)
* Edit movie (Admin only)
* Delete movie (Admin only)

### Reviews

* Add reviews
* Edit reviews
* Delete reviews
* View personal reviews
* Calculate average movie ratings

### Watchlist

* Add movie to watchlist
* Remove movie from watchlist
* Mark movie as watched
* View personal watchlist

### User Profile

* View profile
* Edit profile information

---

## User Roles

### Guest

* Register
* Login
* Browse public pages

### User

* Browse movies
* Manage watchlist
* Create and manage reviews
* Edit profile

### Admin

* All user permissions
* Add movies
* Edit movies
* Delete movies

---

## Domain Entities

### User

Stores user account information and role.

### Movie

Stores movie details such as title, director, genre, release year, description, and poster URL.

### Review

Stores movie ratings and comments submitted by users.

### Watchlist

Stores movies saved by users for future viewing.

---

## Entity Relationships

* User → Review (One-to-Many)
* Movie → Review (One-to-Many)
* User → Watchlist (One-to-Many)
* Movie → Watchlist (One-to-Many)

---

## Validation

The application includes server-side validation for:

* User registration
* User login
* Profile editing
* Movie creation and editing
* Review creation and editing

Validation errors are displayed directly on the corresponding forms.

---

## Security

* Passwords are stored encrypted using BCrypt.
* Session-based authentication is implemented using HttpSession.
* Protected pages require authentication.
* Administrative operations are restricted to ADMIN users.

---

## Running the Application

### Requirements

* Java 17+
* MySQL
* Maven

### Setup

1. Clone the repository

```bash
git clone https://github.com/ralitsadzhukeva/CineVault.git
```

2. Create a MySQL database:

```sql
CREATE DATABASE cinevault;
```

3. Configure your database credentials in:

```properties
application.properties
```

4. Run the application:

```bash
mvn spring-boot:run
```

5. Open:

```text
http://localhost:8080
```

---

## Project Structure

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
        └── application.properties
```

---

## Author

Ralitsa Dzhukeva

Developed as an Individual Project Assignment for the Spring Fundamentals Course at SoftUni.

