<div align="center">

# 🏥 GRH – Hospital Appointment Management System  
### *A Spring Boot + MongoDB full-stack mini-project*

---

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-6.0-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Maven](https://img.shields.io/badge/Maven-Build-CC0000?style=for-the-badge&logo=apachemaven&logoColor=white)
![HTML](https://img.shields.io/badge/HTML5-Front--End-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS](https://img.shields.io/badge/CSS3-UI-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)

---

</div>

## 📌 Overview

**GRH – Gestionnaire de Rendez-vous Hospitaliers** is a complete backend application designed to manage:

- 👨‍⚕️ Doctors  
- 🧑‍🤝‍🧑 Patients  
- 📅 Appointments  
- 📊 Statistical Reports  

Built using **Spring Boot**, **Java**, **MongoDB**, and a minimal **HTML/CSS/JS** front-end for demonstration purposes.

The system includes:

- 🔹 Full REST API  
- 🔹 Doctor schedule management (WorkSlots)  
- 🔹 Appointment availability checking  
- 🔹 Conflict detection for overlapping appointments  
- 🔹 Automatic appointment status updates  
- 🔹 Swagger documentation  
- 🔹 A simple UI to manage doctors  

---

## 🧱 Project Architecture

```

GRH (Root)
│── src
│   └── main
│       ├── java/com/grh
│       │   ├── controller        → REST endpoints
│       │   ├── service           → Business logic
│       │   ├── repository        → MongoDB repositories
│       │   ├── model             → Domain models
│       │   ├── dto               → Request & Response DTOs
│       │   ├── mapper            → Converters between DTO & Model
│       │   ├── exception         → Globalized exception handling
│       │   └── config            → OpenAPI, Jackson, DataSeeder
│       └── resources
│           └── application.properties
└── README.md

```

---

## 🚀 Features

### 👨‍⚕️ **Doctor Management**
- Create, update, delete doctors  
- Manage **WorkSlots** (weekday + start/end time)  
- Search by **name** or **specialty**  
- View available time slots for a specific date  

### 🧑‍🤝‍🧑 **Patient Management**
- Full CRUD functionality  
- Partial name search  
- Validation for phone, email, gender, etc.

### 📅 **Appointment Management**
- Create an appointment only when:
  - Doctor is available in WorkSlot  
  - No time conflicts  
- Update appointments  
- Cancel appointments  
- Auto-change status to *Terminated* for past dates  

### 📊 **Reports (MongoDB Aggregation)**
- Appointments per day  
- Appointments per doctor  
- Appointments per specialty  
- Frequent patients  
- Doctor availability  

### 🧩 **Front-End Demo Interface**
- Simple HTML/CSS/JS interface to manage doctors  
- Uses Fetch API to call backend  

---

## 🔗 REST API Endpoints

### 🧑‍🤝‍🧑 Patients
```

POST   /patients
GET    /patients
GET    /patients/{id}
PUT    /patients/{id}
DELETE /patients/{id}

```

### 👨‍⚕️ Doctors
```

POST   /doctors
GET    /doctors
GET    /doctors/{id}
PUT    /doctors/{id}
DELETE /doctors/{id}
GET    /doctors/search?name=x&specialty=y
GET    /doctors/{id}/available-slots?date=YYYY-MM-DD

```

### 📅 Appointments
```

POST   /appointments
GET    /appointments
GET    /appointments/{id}
PUT    /appointments/{id}
DELETE /appointments/{id}
GET    /appointments/day?date=YYYY-MM-DD
GET    /appointments/doctor/{doctorId}
GET    /appointments/patient/{patientId}

```

### 📊 Reports
```

GET /reports/frequent-patients?days=60
GET /reports/appointments-per-doctor
GET /reports/appointments-per-specialty
GET /reports/day?date=YYYY-MM-DD

````

---

## 🛠️ Technologies Used

| Technology | Role |
|------------|------|
| **Java 17** | Core programming language |
| **Spring Boot** | Backend REST framework |
| **Spring Data MongoDB** | Database access layer |
| **MongoDB** | NoSQL document database |
| **Maven** | Dependency management & build |
| **Swagger / OpenAPI** | API documentation |
| **HTML / CSS / JavaScript** | Front-end demo interface |

---

## ⚙️ Installation & Setup

### 1️⃣ Clone the project
```bash
git clone https://github.com/user/grh-project.git
cd grh-project
````

### 2️⃣ Configure MongoDB (default)

```
mongodb://localhost:27017/grh
```

### 3️⃣ Run the backend

```bash
./mvnw spring-boot:run
```

App starts on:

```
http://localhost:8081
```

### 4️⃣ Open Swagger UI

```
http://localhost:8081/swagger-ui.html
```

---

## 🗄️ Database Structure (MongoDB)

### ✔ Doctors

```json
{
  "fullName": "Dr. Alice Martin",
  "specialty": "Cardiologie",
  "phone": "...",
  "email": "...",
  "workSlots": [
    { "weekday": 1, "startTime": "09:00", "endTime": "12:00" }
  ]
}
```

### ✔ Patients

```json
{
  "fullName": "John Doe",
  "dateOfBirth": "1990-05-15",
  "gender": "M",
  "phone": "+33600000",
  "email": "...",
  "address": "..."
}
```

### ✔ Appointments

```json
{
  "patientId": "...",
  "doctorId": "...",
  "date": "2025-01-30",
  "time": "10:00",
  "durationMinutes": 30,
  "status": "Scheduled"
}
```

---

## 🖼 Screenshots

> Put your screenshots inside a `/screenshots` folder and replace the references below.

```
screenshots/
│── swagger_api.png
│── doctors_ui.png
│── mongodb_view.png
│── appointments_ui.png
```

---

## 👨‍💻 Authors

* **Mohammed Abdulatef Azan**
* **Imad Eddine Boubkari**

---

## 📄 License

This project is for academic purposes (ENS Tétouan).
All rights reserved to the authors.