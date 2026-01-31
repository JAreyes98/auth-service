# HealthConnect Auth Service

The **Auth Service** is a core microservice of the HealthConnect ecosystem. It handles user authentication, JWT generation, and sends audit logs to the monitoring system via RabbitMQ.

## 🚀 Features
* **Authentication & Authorization**: Secure login and JWT issuance.
* **Database Integration**: Persists user data in PostgreSQL.
* **Audit Logging**: Produces event messages to RabbitMQ (`audit_queue`) in JSON format for cross-language compatibility.
* **Dockerized**: Fully containerized for easy deployment and scaling.

---

## 🛠 Prerequisites
* **Docker** installed.
* **Docker Network**: Ensure the internal network exists before running.
  ```powershell
  docker network create healthconnect-net

  ```
docker build -t healthconnect-auth-service:1.0.0 . 

docker run -d 
  --name healthconnect-auth-service 
  --network healthconnect-net 
  -p 8083:8083 
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgresdb-prod-01:5432/iam_db 
  -e SPRING_RABBITMQ_HOST=healthconnect-queue 
  -e SPRING_RABBITMQ_PORT=5672 
  -e JWT_SECRET=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
  healthconnect-auth-service:1.0.0
  ```