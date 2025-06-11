# Charity Auction System - Spring Boot Backend

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![MySQL](https://img.shields.io/badge/MySQL-8.x-orange)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A real-time auction system backend for community charity events built with Spring Boot and MySQL.

## Table of Contents
- [Features](#-features)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [API Documentation](#-api-documentation)
- [Endpoints](#-endpoints)
- [Examples](#-examples)
- [Project Structure](#-project-structure)
- [Database Schema](#-database-schema)
- [License](#-license)

## ✨ Features
- Create and manage auction items
- Place bids on active auctions
- Automatic auction closing
- Winner determination
- Swagger API documentation
- MySQL persistence
- Robust validation

## 🛠 Prerequisites
- Java 17 or higher
- MySQL 8.x
- Maven 3.6+

## 💻 Installation

1. Clone the repository:
```bash
git clone https://github.com/ThushankaPramuditha/Real-TIme-Auction-System.git
cd Real-TIme-Auction-System

Set up the database:

bash
mysql -u root -p1234 -e "CREATE DATABASE IF NOT EXISTS auction_db"
Build and run:

bash
mvn clean install
mvn spring-boot:run
🔧 Configuration
The application uses the following default configuration (modify in application.yml):

Setting	Default Value
Server Port	8088
Database URL	jdbc:mysql://localhost:3306/auction_db
Database Username	root
Database Password	1234
Hibernate DDL	update
Swagger UI Path	/swagger-ui.html
📚 API Documentation
Interactive API documentation is available at:
🔗 http://localhost:8088/swagger-ui.html

🌐 Endpoints
Item Management
Method	Endpoint	Description
POST	/api/items	Create new auction item
GET	/api/items	List all active auction items
GET	/api/items/{id}	Get details of specific auction item
Bidding
Method	Endpoint	Description
POST	/api/items/{id}/bids	Place bid on auction item
Results
Method	Endpoint	Description
GET	/api/items/{id}/winner	Get winning bid details
