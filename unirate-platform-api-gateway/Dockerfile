FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/auth-service-0.0.1-SNAPSHOT.jar /app/unirate-platform-api-gateway.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/unirate-platform-api-gateway.jar"]