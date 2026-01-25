# 🏨 The Majestic Haven - Hotel Room Booking System

> **A full stack desktop app hotel room booking system.**

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-007396?style=for-the-badge&logo=java&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

## 📖 Overview

**The Majestic Haven** is a comprehensive hotel management solution that bridges the gap between desktop performance and web scalability. 

The project consists of two distinct parts:

1. **Backend:** A robust RESTful API built with **Spring Boot** that handles logic, database transactions, authentication, and booking management.
2. **Frontend:** A **JavaFX** desktop app that consumes the API to provide a seamless user interface for booking rooms, managing profiles, and viewing hotel analytics.

## ✨ Key Features

### 🖥️ Frontend (Desktop Client)
- **Modern UI:** Built with JavaFX and styled with CSS for a premium look and feel.  
- **Live Booking:** Real-time checking of room availability and price calculation.  
- **Dashboard Analytics:** Visual data representation (Pie Charts & Bar Charts) for room types and quality stats.  
- **User Session:** Secure login/logout flows using JWT storage.  
- **Feedback System:** Integrated forms for "Contact Us" and Room Reviews.  

### ⚙️ Backend (REST API)
- **Secure Authentication:** Implementation of JWT (JSON Web Tokens) for stateless security.  
- **Transactional Operations:** Full management for Clients, Rooms, Bookings, and Reviews.  
- **Business Logic:** Automatic handling of booking dates, conflict resolution, and price calculation.   

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3+  
- **Security:** Spring Security & JWT  
- **Database:** MariaDB (HeidiSQL) via Spring Data JPA  
- **Build Tool:** Maven/Gradle  

### Frontend
- **Framework:** JavaFX 17+  
- **Design:** FXML & CSS  
- **Networking:** `java.net.http.HttpClient`  
- **JSON Processing:** `org.json`  

## 🚀 Getting Started

To run this project, you need to start the Backend server first, followed by the Frontend client.

### Prerequisites
- JDK 17 or higher  
- Maven or Gradle  
- A SQL Database (configure your `application.properties` in the backend folder)  

### 1️⃣ Run the Backend And Frontend
```bash
cd backend
mvn spring-boot:run

cd frontend
mvn javafx:run


