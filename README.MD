# # 🛡️ HealthConnect Auth Service

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)

The **Auth Service** is the core security component of the HealthConnect ecosystem. It manages user identity, token issuance, and asynchronous audit event logging.



## ✨ Features

* **Authentication & Authorization**: Secure access control and JWT (JSON Web Token) generation.
* **Data Persistence**: Comprehensive user management powered by **PostgreSQL**.
* **Event Auditing**: Automatic event publishing to **RabbitMQ** (queue: `audit_queue`) in JSON format for cross-service compatibility.
* **Cloud Native**: Fully containerized and ready for immediate deployment via Docker.

---

## 🛠️ Prerequisites

Ensure you have the following installed:
* **Docker** and **Docker Compose**.
* A pre-configured Docker internal network:
    ```powershell
    docker network create healthconnect-net
    ```

---

## 🚀 Deployment

### 1. Build the Image
Run the following command in the project root:
```bash
docker build -t healthconnect-auth-service:1.0.0 .

### Run the Container
Use this command to start the service and connect it to the HealthConnect infrastructure:

```bash
docker run -d `
  --name healthconnect-auth-service `
  --network healthconnect-net `
  -p 8083:8083 `
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgresdb-prod-01:5432/iam_db `
  -e SPRING_RABBITMQ_HOST=healthconnect-queue `
  -e SPRING_RABBITMQ_PORT=5672 `
  -e JWT_SECRET=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437 `
  healthconnect-auth-service:1.0.0
