# LuxProtect Alert System - Backend 🛡️

A robust Spring Boot backend service powering real-time geolocation alerts with PostgreSQL and PostGIS support.

## ✨ Key Features

### 🔐 Authentication & Security

JWT-based authentication
Role-based authorization (ADMIN/USER)
Password encryption with BCrypt
Token blacklisting

### 📍 Geospatial Features

PostGIS integration for location queries
Proximity-based alert detection
Spatial indexing for performance
Coordinate validation

### 🔄 Real-time Communication

WebSocket/STOMP implementation
Alert broadcasting system
Connection state management
Session handling

### 📊 Data Management

PostgreSQL with PostGIS extension
Flyway migrations
JPA repositories
Transaction management

## 🚀 Quick Start

##### Clone repository

`git clone https://github.com/yourusername/luxprotect-backend.git`

##### Start PostgreSQL container

`docker-compose up -d`

##### Run Spring Boot application

`./mvnw spring-boot:run`

## 📦 Docker Setup

``version: '3.8'
services:
postgres:
image: postgis/postgis:15-3.3
environment:
POSTGRES_DB: luxprotect
POSTGRES_USER: postgres
POSTGRES_PASSWORD: password
ports:
- "5432:5432"
volumes:
- postgres_data:/var/lib/postgresql/data

volumes:
postgres_data:``

## 🚀 Deployment
##### Build Docker image
docker build -t luxprotect-backend .

##### Run container
docker run -p 8080:8080 luxprotect-backend

## 🗄️ Project Structure

src/
├── main/
│   ├── java/
│   │   └── com/luxprotect/
│   │       ├── config/          # Configuration classes
│   │       ├── controller/      # REST endpoints
│   │       ├── model/          # Domain entities
│   │       ├── repository/     # Data access layer
│   │       ├── service/        # Business logic
│   │       └── security/       # Security config
│   └── resources/
│       └── db/migration/      # Flyway migrations

## 🛠️ Tech Stack

Spring Boot 3.x
PostgreSQL 15 with PostGIS
Flyway Migration
Spring Security
Spring WebSocket
JPA/Hibernate Spatial
