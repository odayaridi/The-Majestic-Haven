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
```

---
## 📸 Screenshots

### Sign up and login

<img width="866" alt="Sign Up Screen" src="https://github.com/user-attachments/assets/500e4009-b271-49ab-b4e0-78758f4aeb41" />
<br /><br />
<img width="862" alt="Login Screen" src="https://github.com/user-attachments/assets/b7d7f139-ce42-4d15-943d-4dba3645683c" />
<br /><br />

### Other Features
<img width="1246" alt="Dashboard Overview" src="https://github.com/user-attachments/assets/27378293-e9b5-4966-885a-07c629f8fd6f" />
<br /><br />
<img width="1245" alt="Room Management" src="https://github.com/user-attachments/assets/403be08e-053a-4806-a078-ea91267f8c01" />
<br /><br />
<img width="1247" alt="Analytics Dashboard" src="https://github.com/user-attachments/assets/a6569d29-44dd-42f9-bb5e-dfe86e83f0f9" />
<br /><br />
<img width="1248" alt="Room Booking Interface" src="https://github.com/user-attachments/assets/12305c0a-44e2-4dd2-b9ac-a6e997f682cb" />
<br /><br />
<img width="1240" alt="Booking Management" src="https://github.com/user-attachments/assets/dfc1a598-e314-4949-8fd1-7cf476e30faf" />
<br /><br />
<img width="1243" alt="User Profile" src="https://github.com/user-attachments/assets/913ab990-9371-47c8-98ae-29cd78ca4b23" />
<br /><br />
<img width="1250" alt="Reviews System" src="https://github.com/user-attachments/assets/26dead88-e91d-4280-ad4b-09fd9d21b684" />
<br /><br />
<img width="1248" alt="Contact Us Form" src="https://github.com/user-attachments/assets/fd77487f-df74-4a23-a58c-e4b031c8bffd" />
<br /><br />
<img width="1246" alt="Room Details View" src="https://github.com/user-attachments/assets/88f999b5-c91c-4b9e-8d0b-ff2287f48143" />
<br /><br />
<img width="1250" alt="Booking Confirmation" src="https://github.com/user-attachments/assets/e6bdd185-a2fb-4e88-9557-ce3535c11ad5" />
