# Grocery Delivery System Microservices

This project is a Spring Boot microservices-based Grocery Delivery System that provides functionalities for inventory management, order processing, and delivery scheduling. The system is designed with scalability and flexibility in mind, leveraging various Spring technologies and modern security practices.

## Table of Contents
- [Overview](#overview)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [Services](#services)
  - [Inventory Service](#inventory-service)
  - [Order Service](#order-service)
  - [Delivery Service](#delivery-service)
  - [API Gateway](#api-gateway)
  - [Service Discovery](#service-discovery)
- [Security](#security)


## Overview
The Grocery Delivery System is composed of several microservices that communicate with each other to handle different aspects of the grocery delivery process. The system includes:

- **Inventory Service**: Manages product information and stock levels.
- **Order Service**: Handles order creation, cart management, and order status.
- **Delivery Service**: Manages delivery slot selection and scheduling.
- **API Gateway**: Acts as a single entry point for all client requests.
- **Service Discovery**: Enables dynamic discovery of services.

## Technologies Used
- **Spring Boot**
  - Spring Boot Starter Web
  - Spring Boot Starter Data JPA
  - Spring Boot Starter Security
  - Spring Boot Starter Actuator
  - Spring Boot Starter Test
- **Spring Cloud**
  - Spring Cloud Netflix Eureka (Service Discovery)
  - Spring Cloud Gateway (API Gateway)
- **JPA/Hibernate ORM**
- **Databases**
  - **MongoDB** (for storing inventory and delivery data)
  - **MySQL** (for storing order and user data)
- **Security**
  - JSON Web Tokens (JWT)
- **Others**
  - Lombok (for reducing boilerplate code)
  - Maven (for dependency management)

## Architecture
The architecture follows a microservices design pattern where each service is independently deployable and scalable. The services communicate with each other using REST APIs and are registered with the Eureka server for service discovery. The API Gateway routes requests to the appropriate services.

## Services

### Inventory Service
**Base URL: `/inventory`**
- **Add Products**
  - **POST** `/add`
- **Get Product by Product Code**
  - **GET** `/view/{productCode}`
- **Get All Products**
  - **GET** `/view`
- **Delete Products**
  - **DELETE** `/delete`
- **Check Stock**
  - **GET** `/stock`

### Order Service
**Base URL: `/order`**
- **Save Order**
  - **POST** `/user/{userEmail}`
- **Get Orders by User**
  - **GET** `/user/{userEmail}`
- **Get Order Status**
  - **GET** `/{orderNumber}/order-status`
- **Choose Delivery Slot**
  - **POST** `/{orderNumber}/choose-slot`

**Cart Operations under Order Service**
**Base URL: `/cart`**
- **View Cart**
  - **GET** `/view`
- **Add Item to Cart**
  - **POST** `/add`
- **Modify Items in Cart**
  - **PUT** `/modify`
- **Remove Item from Cart**
  - **DELETE** `/delete`

### Delivery Service
**Base URL: `/deliveries`**
- **Get Available Delivery Slots**
  - **GET** `/{orderNumber}/available-slots`
- **Save Delivery**
  - **POST** `/confirm`

### API Gateway
- **Responsibilities**: Route client requests to the appropriate services, handle authentication and authorization.
- **Endpoints**: Exposes a unified API for all services.

### Service Discovery
- **Responsibilities**: Register and discover services dynamically.
- **Technologies**: Eureka Server

## Security
The application uses JWT for securing endpoints. Each service validates the JWT token to ensure the authenticity and authorization of requests.

