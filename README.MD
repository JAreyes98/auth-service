`
docker build -t healthconnect-auth-service:1.0.0 . 
`
`
docker run -d 
  --name healthconnect-auth-service 
  --network healthconnect-net 
  -p 8083:8083 
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgresdb-prod-01:5432/iam_db 
  -e SPRING_RABBITMQ_HOST=healthconnect-queue 
  -e SPRING_RABBITMQ_PORT=5672 
  -e JWT_SECRET=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
  healthconnect-auth-service:1.0.0
  `