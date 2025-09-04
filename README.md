Parking Lot Reservation System
Project Overview

This is a backend REST API for a Parking Lot Reservation system built with Java 17 + Spring Boot 3.
It allows administrators to manage floors and slots, and customers to reserve parking slots without conflicts.

Tech Stack

Java 17+

Spring Boot 3+

PostgreSQL (Relational Database)

Spring Data JPA (Hibernate)

Bean Validation (@Valid)

Swagger/OpenAPI for API documentation

JUnit + Mockito for unit testing

Features

POST /floors – Create a parking floor

POST /slots – Create parking slots for a floor

POST /reserve – Reserve a slot

Checks availability (no overlapping reservations)

Calculates cost based on vehicle type and duration

GET /availability – List available slots for a time range

GET /reservations/{id} – Fetch reservation details

DELETE /reservations/{id} – Cancel a reservation

Business Rules

Start time must be before end time.

Reservation duration cannot exceed 24 hours.

Vehicle number must match format XX00XX0000 (e.g., KA05MH1234).

Partial hours are charged as full hours.

Supports multiple vehicle types with configurable hourly rates.

Database Setup

Create a PostgreSQL database:

CREATE DATABASE parkinglot;


Create initial vehicle_types data:

INSERT INTO vehicle_types (name, hourly_rate) VALUES ('TWO_WHEELER', 20);
INSERT INTO vehicle_types (name, hourly_rate) VALUES ('FOUR_WHEELER', 30);


The tables are automatically created on application start using spring.jpa.hibernate.ddl-auto=update.

Configuration

Update src/main/resources/application.properties if your DB credentials differ:

spring.application.name=parking-lot
spring.datasource.url=jdbc:postgresql://localhost:5432/parkinglot
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

Running the Project

Clone the repository:

git clone <your-repo-link>
cd parking-lot


Build and run the project using Maven:

mvn clean install
mvn spring-boot:run


Access API documentation:

http://localhost:8080/swagger-ui.html

Testing

Run unit tests with:

mvn test


The project includes tests for service and controller layers.

Aim for 90%-100% code coverage.

API Endpoints
Floors

POST /floors – Create a new floor

Slots

POST /slots – Create slots for a floor

Reservations

POST /reserve – Reserve a slot

GET /availability?vehicleType={}&startTime={}&endTime={} – Check availability

GET /reservations/{id} – Get reservation details

DELETE /reservations/{id} – Cancel reservation

Notes

You can add new vehicle types or change rates in vehicle_types table.

Partial hours are rounded up.

Swagger UI is enabled for API exploration.

Folder Structure
src/
├─ main/
│   ├─ java/com/parkinglot/
│   │   ├─ controller/
│   │   ├─ dto/
│   │   ├─ model/
│   │   ├─ repository/
│   │   ├─ service/
│   │   └─ ParkingLotApplication.java
│   └─ resources/
│       ├─ application.properties
│       └─ data.sql
└─ test/java/com/parkinglot/
├─ controller/
└─ service/

Bonus Features Implemented

Pagination and sorting for available slots.

API documentation with Swagger.

Global exception handling with @ControllerAdvice.