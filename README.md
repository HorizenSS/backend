# LuxProtect Alert System - Backend 🛡️

A robust Spring Boot backend service powering real-time geolocation alerts with PostgreSQL and PostGIS support.

## ✨ Key Features

### 🔐 Authentication & Security

* JWT-based authentication
* Role-based authorization (ADMIN/USER)
* Password encryption with BCrypt
* Token blacklisting

### 📍 Geospatial Features

* PostGIS integration for location queries
* Proximity-based alert detection
* Spatial indexing for performance
* Coordinate validation

### 🔄 Real-time Communication

* WebSocket/STOMP implementation
* Alert broadcasting system
* Connection state management
* Session handling

### 📊 Data Management

* PostgreSQL with PostGIS extension
* Flyway migrations
* JPA repositories
* Transaction management

### 🔍 Testing Features

* Unit tests with JUnit 5
* Integration tests with TestContainers
* WebSocket client tests
* Security tests

## 🚀 Quick Start

#### Clone repository

`git clone https://github.com/yourusername/luxprotect-backend.git`

#### Start PostgreSQL container

`docker-compose up -d`

#### Run Spring Boot application

`./mvnw spring-boot:run`

## 🚀 Deployment
#### Build Docker image
docker build -t luxprotect-backend .

#### Run container
docker run -p 8080:8080 luxprotect-backend

## 🗄️ Project Structure

* src/
* ├── main/
* │   ├── java/
* │   │   └── com/luxprotect/
* │   │       ├── config/          # Configuration classes
* │   │       ├── controller/      # REST endpoints
* │   │       ├── model/          # Domain entities
* │   │       ├── repository/     # Data access layer
* │   │       ├── service/        # Business logic
* │   │       └── security/       # Security config
* │   └── resources/
* │       └── db/migration/      # Flyway migrations

## 🛠️ Tech Stack

* Spring Boot 3.x
* PostgreSQL 15 with PostGIS
* Flyway Migration
* Spring Security
* Spring WebSocket
* JPA/Hibernate Spatial

## 📚 API Documentation

### 🎯 Alert Controller (/api/v1/alerts)

**##### Create Alert**

**POST** /api/v1/alerts
**Authorization**: Bearer {token}
**Content-Type**: application/json

**Request:**
`{
"latitude": 40.7128,
"longitude": -74.0060,
"description": "Emergency situation"
}`

**Response:**
`{
"data": {
"id": 1,
"latitude": 40.7128,
"longitude": -74.0060,
"status": "ACTIVE"
},
"message": "Alert created successfully"
}`

##### **Get Alert by ID**

**GET** /api/v1/alerts/{id}

**Response**:
`{
"data": {
"id": 1,
"latitude": 40.7128,
"longitude": -74.0060,
"status": "ACTIVE"
},
"message": "Alert retrieved successfully"
}`

##### **Get All Alerts**

**GET** /api/v1/alerts

**Response**:
`{
"data": [
{
"id": 1,
"latitude": 40.7128,
"longitude": -74.0060,
"status": "ACTIVE"
}
],
"message": "All alerts retrieved successfully"
}`

##### **Update Alert**

**PUT** /api/v1/alerts/{id}
**Authorization**: Bearer {token}
**Content-Type:** application/json

**Request**:
`{
"latitude": 40.7128,
"longitude": -74.0060,
"description": "Updated emergency"
}`

##### **Get Nearby Alerts**

**GET** /api/v1/alerts/nearby?latitude=40.7128&longitude=-74.0060&radius=10.0

**Response**:
`{
"data": [
{
"id": 1,
"distance": 0.5,
"status": "ACTIVE"
}
],
"message": "Nearby alerts retrieved successfully"
}`

#### 👤 Customer Controller (/api/v1/customers)

##### **Register Customer**

**POST** /api/v1/customers
**Content-Type:** application/json

**Request**:
`{
"email": "user@example.com",
"password": "securePassword",
"name": "John Doe"
}`

**Response**:
`Headers: {
"Authorization": "Bearer {token}"
}
{
"message": "Customer registered successfully"
}
`
##### **Get Customer Profile**

**GET** /api/v1/customers/{customerId}
**Authorization:** Bearer {token}

**Response**:
`{
"data": {
"id": 1,
"name": "John Doe",
"email": "user@example.com"
},
"message": "Customer retrieved successfully"
}`

##### **Upload Profile Image**

**POST** /api/v1/customers/{customerId}/profile-image
**Authorization**: Bearer {token}
**Content-Type:** multipart/form-data

**Form-Data:**
file: [image file]

**Response**:
`{
"message": "Profile image uploaded successfully"
}`

### 🔄 WebSocket Controller

##### **Location Updates**

**CONNECT** ws://localhost:8080/ws-alerts
**Headers**:
**Authorization**: Bearer {token}

**SUBSCRIBE** /topic/nearby-alerts

**SEND** /app/location/{userId}
`{
"latitude": 40.7128,
"longitude": -74.0060
}`

##### **Receive Notifications**

**SUBSCRIBE** /topic/nearby-alerts

**Received Message:**
`{
"id": 1,
"type": "EMERGENCY",
"location": {
"latitude": 40.7128,
"longitude": -74.0060
},
"distance": 0.5
}`

##   🔗 Links

1. [ ] API Documentation
2. [ ] Database Schema
3. [ ] Security Guide