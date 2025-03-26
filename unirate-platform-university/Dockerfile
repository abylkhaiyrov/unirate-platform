FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/auth-service-0.0.1-SNAPSHOT.jar /app/unirate-platform-university.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/unirate-platform-university.jar"]