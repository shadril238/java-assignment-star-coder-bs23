# Java Assignment - Star Coder Program at Brain Station 23

This repository hosts the codebase for the Java assignment as part of the Star Coder program by Brain Station 23. The project uses Spring Boot to create a RESTful API for user registration, authentication, and task management.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software:

- Java Development Kit (JDK) - Version 11 or above
- Maven - Dependency management
- MySQL - Database

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/java-assignment-star-coder-bs23.git
### Backend (Spring Boot 3)
1. Navigate to the backend directory:
   ```bash
   cd java-assignment-star-coder-bs23/backend
3. Install the Maven dependencies:
   ```bash
   mvn clean install
5. Create a MySQL database named task_management (or as configured in your application properties):
   ```bash
   CREATE DATABASE task_management;
7. Update the application.properties file with your MySQL username and password.
8. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run

### Frontend (React JS)
1. Navigate to the frontend directory from the root of the project:
   ```bash
   cd java-assignment-star-coder-bs23/frontend
3. Install the npm packages:
   ```bash
   npm install
   npm start

After following these steps, the backend should be running on http://localhost:8090, and the frontend on http://localhost:5173 (or respective ports if configured differently).



