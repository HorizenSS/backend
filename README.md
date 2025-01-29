# LuxProtect Alert System - Backend 🛡️

A robust Spring Boot backend service powering real-time geolocation alerts with PostgreSQL and PostGIS support.

## ✨ Key Features

### 🔐 Authentication & Security
* JWT-based authentication
* Role-based authorization (ADMIN/USER)
* Password encryption with BCrypt
* Token blacklisting

### 📍 Geospatial Features
* Proximity-based alert detection
* Spatial indexing for performance
* Coordinate validation

### 🔄 Real-time Communication
* WebSocket/STOMP implementation
* Alert broadcasting system
* Connection state management
* Session handling

### 📊 Data Management
* PostgreSQL with data indexing for alert locations
* Flyway migrations
* JPA repositories

### 🔍 Testing Features
* Unit tests with JUnit 5
* WebSocket client tests
* Security tests

## 🛠️ Tech Stack

* Spring Boot
* PostgreSQL 14
* Flyway Migration
* Spring Security
* Spring WebSocket
* JPA/Hibernate Spatial
* Mockito
* Junit5
* Jib
* Docker

## 🚀 Running the Project Locally
To run the project on your local environment, follow these steps:

### **_SETUP DATABASE_**
### Run PostgreSQL in Docker
#### **1. Install Docker (If Not Installed)**
#### **Windows & macOS:**
Download and install **Docker Desktop** from [Docker's official site](https://www.docker.com/get-started/).
#### **Linux:**
Install Docker using:

`sudo apt update && sudo apt install -y docker.io
`
Ensure Docker is running:

`sudo systemctl start docker`

`sudo systemctl enable docker`

#### **2. Pull & Run PostgreSQL**

`docker pull postgres`

`docker run --name lux_protect_db \
  -e POSTGRES_DB=lux_protect \
  -e POSTGRES_USER=ines \
  -e POSTGRES_PASSWORD=ines \
  -p 5432:5432 \
  -d postgres`

#### **3. Verify & Access Container**
Check running containers 

`docker ps`

Restart if stopped

`docker start lux_protect_db`

`docker exec -it lux_protect_db psql -U ines -d lux_protect`

#### **4. Check Database & User**
If missing:
```sql
CREATE DATABASE lux_protect;
CREATE USER ines WITH ENCRYPTED PASSWORD 'ines';
GRANT ALL PRIVILEGES ON DATABASE lux_protect TO ines;
```
#### **5. Connect Locally**
- **Host:** `localhost`
- **Port:** `5432`
- **Database:** `lux_protect`
- **Username:** `ines`
- **Password:** `ines`
✅ PostgreSQL is now running locally! 🚀

### **_SETUP BACKEND_**
#### Clone repository

`git clone https://github.com/yourusername/luxprotect-backend.git`

#### Navigate to the backend directory
#### Run Spring Boot application

`./mvnw spring-boot:run`

### **_SETUP FRONTEND_**
#### Clone repository

`git clone https://github.com/HorizenSS/lux-protect-client.git`

#### Navigate to the frontend directory.
#### Install dependencies

`npm install`

#### Run the Angular app

`ng serve`

## 🚀 Project Roadmap

Below is a breakdown of the main development tasks for the project. These tasks will help structure the project as it moves through development:
### 🛠 Backend Setup
✅ **[DONE]** Initialize **Spring Boot** and configure dependencies.  
✅ **[DONE]** Set up **database connections** for PostgreSQ using Docker.  
✅ **[DONE]** Create **User** and **Incident** models and configure **Spring Security**.  
✅ **[DONE]** Implement **role-based access control** for different user types.
---
### 🎨 Frontend Setup
✅ **[DONE]** Initialize **Angular project** and set up core and shared modules.  
✅ **[DONE]** Configure **routing** for major sections, such as **Map, alert Reporting...**.  
✅ **[DONE]** Integrate **LeafletJS Map** for interactive map features.
---
### 🌟 Core Features
🔐 **User Authentication**: ✅ **[DONE]** Implement user registration, login, and authentication (**JWT-based**).  
🗺 **Map Display**: ✅ **[DONE]** Configure **LeafletJS** for displaying incident markers.  
📢 **Incident Reporting**: ✅ **[DONE]** Create forms for users to submit incidents and view details.  
📲 **Notifications**: ✅ **[DONE]** Integrate **Websocket** for real-time notifications.
---
### 🔍 Testing and Deployment
✅ **[DONE]** Set up **unit and integration tests** for frontend and backend.  
🚀 **[IN PROGRESS]** Deploy the backend and frontend to **AWS** (or other cloud services).  
⚙️ **[NOT STARTED]** Configure **CI/CD pipelines** for automatic deployment.

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


## 📚 API Documentation

### 🎯 Alert Controller (/api/v1/alerts)

* ##### **Create Alert**
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

* ##### **Get Alert by ID**
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

* ##### **Get All Alerts**
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

* ##### **Update Alert**
**PUT** /api/v1/alerts/{id}
**Authorization**: Bearer {token}
**Content-Type:** application/json
**Request**:
`{
"latitude": 40.7128,
"longitude": -74.0060,
"description": "Updated emergency"
}`

* ##### **Get Nearby Alerts**
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

### 👤 Customer Controller (/api/v1/customers)

* ##### **Register Customer**
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
* ##### **Get Customer Profile**
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

### 🔄 WebSocket Controller

* ##### **Location Updates**
**CONNECT** ws://localhost:8080/ws-alerts
**Headers**:
**Authorization**: Bearer {token}
**SUBSCRIBE** /topic/nearby-alerts
**SEND** /app/location/{userId}
`{
"latitude": 40.7128,
"longitude": -74.0060
}`

* ##### **Receive Notifications**
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
Built with ❤️ by [[Ines Akez](https://www.linkedin.com/in/ines-akez-a69996110/)]