FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/unirate-platform-api-gateway-0.0.1-SNAPSHOT.jar /app/unirate-platform-api-gateway.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/unirate-platform-api-gateway.jar"]