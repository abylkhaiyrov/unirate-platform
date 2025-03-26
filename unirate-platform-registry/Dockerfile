FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/auth-service-0.0.1-SNAPSHOT.jar /app/unirate-platform-registry.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "/app/unirate-platform-registry.jar"]